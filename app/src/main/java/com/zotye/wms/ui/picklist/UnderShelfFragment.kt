package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.PickListPullOffShelf
import com.zotye.wms.data.api.model.under.shelf.PrMobileConfirmRequest
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemPickListInfoUnderShelfBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_pick_list_under_shelf.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.collections.forEachByIndex
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.onUiThread
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/25
 */
class UnderShelfFragment : BaseFragment(), UnderShelfContract.UnderShelfView, ScannerDelegate {

    companion object {
        fun newInstance(title: String): UnderShelfFragment {
            val fragment = UnderShelfFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: UnderShelfContract.UnderShelfPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pick_list_under_shelf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_under_shelf)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        pickListScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@UnderShelfFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        pickListInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.picklist_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_picklist_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.pick_list_under_shelf_list_empty)
        val adapter = PickListOffShelfAdapter()
        adapter.emptyView = emptyView
        pickListRecyclerView.layoutManager = LinearLayoutManager(context)
        pickListRecyclerView.adapter = adapter
        adapter.setOnItemChildClickListener { _, _, position ->
            val pickListPullOffShelf = adapter.getItem(position)
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(object : ScannerDelegate {
                override fun succeed(result: String) {
                    if (pickListPullOffShelf?.storageUnitInfoCode == result) {
                        if (pickListPullOffShelf.checkFlag) {
                            presenter.getStorageUnitMaterialTotalNumber(position, pickListPullOffShelf.storageUnitInfoCode!!, pickListPullOffShelf.spDetailId!!)
                        } else {
                            pickListPullOffShelf.isAddedPackage = true
                            adapter.notifyItemChanged(position)
                        }
                    } else {
                        AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(R.string.not_match_under_shelf_package).setNegativeButton(R.string.ok, null).show()
                    }
                }
            })
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        underShelfButton.onClick {
            if (adapter.data.isEmpty()) {
                showMessage(R.string.picklist_pull_off_shelf_empty_error)
            } else {
                showProgressDialog(R.string.loading_create_data)
                val request = PrMobileConfirmRequest()
                request.confirmDetail = ArrayList()
                doAsync {
                    adapter.data.forEachByIndex { pickListPullOffShelf ->
                        request.prNo = pickListPullOffShelf.pickListCode
                        if (pickListPullOffShelf.checkFlag && !pickListPullOffShelf.isAddedPackage) {
                            onUiThread {
                                showMessage(R.string.picklist_pull_off_shelf_no_add_error)
                                hideProgressDialog()
                            }
                            return@doAsync
                        }
                        if (pickListPullOffShelf.checkFlag) {
                            val prDto = PrMobileConfirmRequest.PrCheckInfoDto()
                            prDto.id = pickListPullOffShelf.id
                            prDto.checkNum = if (pickListPullOffShelf.isChecked()) pickListPullOffShelf.checkCount else null
                            prDto.actualOffshelfNum = pickListPullOffShelf.actulOffShellNumber
                            request.confirmDetail?.add(prDto)
                        }
                    }
                    onUiThread {
                        hideProgressDialog()
                        presenter.underShelfConfirm(request)
                    }
                }
            }
        }
    }

    override fun getStorageUnitMaterialTotalNumber(position: Int, totalNumber: Long) {
        val pickListPullOffShelf = (pickListRecyclerView.adapter as PickListOffShelfAdapter).getItem(position)
        var checkDialog: AlertDialog? = null
        pickListPullOffShelf?.let { it ->
            val codeInputView = LayoutInflater.from(context!!).inflate(R.layout.dialog_under_shelf_package, null)
            val editText = codeInputView.findViewById<EditText>(R.id.underShelfEditText)
            val underCountEditText = codeInputView.findViewById<EditText>(R.id.underShelfNumber)
            codeInputView.findViewById<TextView>(R.id.packageCount).text = "$totalNumber"
            codeInputView.findViewById<TextView>(R.id.underShelfNumber).text = "${it.totalNum}"
            codeInputView.findViewById<View>(R.id.checkLayout).visibility = if (it.checkFlag) View.VISIBLE else View.GONE
            codeInputView.find<View>(R.id.cancelButton).onClick {
                checkDialog?.dismiss()
            }
            codeInputView.find<View>(R.id.okButton).onClick { _ ->
                val underCount = if (TextUtils.isEmpty(underCountEditText.text.toString())) 0 else underCountEditText.text.toString().toLong()
                val checkCount = if (TextUtils.isEmpty(editText.text.toString())) 0 else editText.text.toString().toLong()
                if (underCount > totalNumber || underCount > it.totalNum) {
                    codeInputView.findViewById<EditText>(R.id.underShelfNumber).error = getString(R.string.error_under_shelf_count)
                    return@onClick
                }
                if (underCount <= 0) {
                    codeInputView.findViewById<EditText>(R.id.underShelfNumber).error = getString(R.string.error_under_shelf_count_less_than_zero)
                    return@onClick
                }
                it.actulOffShellNumber = underCount
                if (it.checkFlag) {
                    if (checkCount != (if ((totalNumber - underCount) >= 0) (totalNumber - underCount) else 0)) {
                        AlertDialog.Builder(getContext()!!).setTitle(R.string.info).setMessage(R.string.under_shelf_no_match_count)
                                .setPositiveButton(R.string.ok) { _, _ ->
                                    checkDialog?.show()
                                }.setNegativeButton(R.string.cancel) { _, _ ->
                                    it.isAddedPackage = true
                                    pickListRecyclerView.adapter.notifyItemChanged(position)
                                }.show()
                    } else
                        it.isAddedPackage = true
                } else
                    it.isAddedPackage = true
                it.checkCount = checkCount
                pickListRecyclerView.adapter.notifyItemChanged(position)
                hideKeyboard(editText)
                checkDialog?.dismiss()
            }
            checkDialog = AlertDialog.Builder(context!!).setTitle(R.string.under_shelf_package_or_pallet_info).setView(codeInputView).create()
            checkDialog?.show()
            showKeyboard(editText)
        }
    }

    override fun succeed(result: String) {
        presenter.getPickListInfoByCode(result)
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    private fun updateTitle() {
        when (viewFlipper.displayedChild) {
            0 -> {
                toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_storage_unit_info)
            }
            1 -> {
                toolbar_base.titleResource = R.string.title_pick_list_add_package
            }
        }
    }

    override fun getPickListPullOffShelfList(pickListPullOffShelfList: List<PickListPullOffShelf>) {
        if (pickListPullOffShelfList.isEmpty())
            showMessage(R.string.pick_list_under_shelf_list_empty)
        else
            (pickListRecyclerView.adapter as PickListOffShelfAdapter).setNewData(pickListPullOffShelfList)
    }

    override fun underShelfSucceed() {
        (pickListRecyclerView.adapter as PickListOffShelfAdapter).setNewData(null)
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(R.string.under_shelf_succeed).setNegativeButton(R.string.ok, null).create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun canBackPressed(): Boolean {
        return if (viewFlipper.displayedChild != 0) {
            viewFlipper.showPrevious()
            updateTitle()
            false
        } else
            true
    }

    class PickListOffShelfAdapter : BaseQuickAdapter<PickListPullOffShelf, BaseViewHolder>(R.layout.item_pick_list_info_under_shelf) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: PickListPullOffShelf) {
            val dataBind = DataBindingUtil.bind<ItemPickListInfoUnderShelfBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind.info = item
            helper.addOnClickListener(R.id.packageCodeScanner)
        }
    }
}