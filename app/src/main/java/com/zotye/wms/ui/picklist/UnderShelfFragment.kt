package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.system.Os.remove
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.PickListPullOffShelf
import com.zotye.wms.data.api.model.under.shelf.PrMobileConfirmRequest
import com.zotye.wms.data.api.model.under.shelf.SUMaterialInfo
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
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.onUiThread
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/25
 */
class UnderShelfFragment : BaseFragment(), UnderShelfContract.UnderShelfView, ScannerDelegate {

    private var prNo: String = ""
    private var addPackage: ArrayList<String> = arrayListOf()
    var underShelfCallBack: UnderShelfCallBack? = null

    interface UnderShelfCallBack {
        fun underShelfSucceed(code: String)
    }

    companion object {
        fun newInstance(title: String): UnderShelfFragment {
            val fragment = UnderShelfFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(title: String, pickCode: String): UnderShelfFragment {
            val fragment = UnderShelfFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("pickCode", pickCode)
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
        arguments?.getString("pickCode", "")?.apply {
            if (!isNullOrBlank()) {
                pickListScanner.visibility = View.GONE
                pickListInput.visibility = View.GONE
                succeed(this)
            }
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
        val adapter = PickListOffShelfAdapter(this)
        adapter.emptyView = emptyView
        pickListRecyclerView.layoutManager = LinearLayoutManager(context)
        pickListRecyclerView.adapter = adapter
        adapter.setOnItemChildClickListener { _, _, position ->
            adapter.getItem(position)?.apply {
                AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(R.string.delete_item_confirm).setNegativeButton(R.string.ok) { _, _ ->
                    presenter.deletePackage(pickListCode ?: "", storageUnitInfoCode ?: "")
                }.setPositiveButton(R.string.cancel, null).show()
            }
        }
        addPackageInput.setOnClickListener {
            val codeInputView = LayoutInflater.from(context!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.package_code)
            AlertDialog.Builder(context!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                val result = editText.text.toString()
                var hasAdded = false
                adapter.data.forEachByIndex {
                    val stCode = if (TextUtils.isEmpty(it.parentStorageUnitInfoCode)) it.storageUnitInfoCode else it.parentStorageUnitInfoCode
                    if (result == stCode) {
                        hasAdded = true
                        if (!addPackage.contains(result))
                            addPackage.add(result)
                        adapter.notifyDataSetChanged()
                    }
                }
                if (!hasAdded)
                    AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(R.string.confirm_add_package).setNegativeButton(R.string.ok) { _, _ ->
                        presenter.addPackage(prNo, result)
                    }.setPositiveButton(R.string.cancel, null).show()
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        addPackageButton.setOnClickListener {
            if (TextUtils.isEmpty(prNo)) {
                showMessage("请先添加拣配单")
            } else {
                val fragment = BarCodeScannerFragment()
                fragment.setScannerDelegate(object : ScannerDelegate {
                    override fun succeed(result: String) {
                        adapter.data.forEachByIndex {
                            val stCode = if (TextUtils.isEmpty(it.parentStorageUnitInfoCode)) it.storageUnitInfoCode else it.parentStorageUnitInfoCode
                            if (result == stCode) {
                                if (!addPackage.contains(result))
                                    addPackage.add(result)
                                adapter.notifyDataSetChanged()
                                return
                            }
                        }
                        AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(R.string.confirm_add_package).setNegativeButton(R.string.ok) { _, _ ->
                            presenter.addPackage(prNo, result)
                        }.setPositiveButton(R.string.cancel, null).show()
////                    /**拆分出包装号*/
////                    if (pickListPullOffShelf?.storageUnitInfoCode == result) {
////                        if (pickListPullOffShelf.pullOffConfirm) {
////                            presenter.getStorageUnitMaterialTotalNumber(position, pickListPullOffShelf.storageUnitInfoCode!!, pickListPullOffShelf.spDetailId!!)
////                        } else {
////                            pickListPullOffShelf.isAddedPackage = true
////                            adapter.notifyItemChanged(position)
////                        }
////                    } else {
////                         }
                    }
                })
                fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
            }
        }
        underShelfButton.setOnClickListener {
            if (adapter.data.isEmpty()) {
                showMessage(R.string.picklist_pull_off_shelf_empty_error)
            } else {
                showProgressDialog(R.string.loading_create_data)
                val request = PrMobileConfirmRequest()
                request.stCodes = arrayListOf()
                request.prNo = prNo
                doAsync {
                    adapter.data.forEachByIndex { pickListPullOffShelf ->
                        request.stCodes?.add(pickListPullOffShelf.storageUnitInfoCode ?: "")
                    }
                    onUiThread {
                        hideProgressDialog()
                        presenter.underShelfConfirm(request)
                    }
                }
            }
        }
    }

    override fun getStorageUnitMaterialTotalNumber(position: Int, info: SUMaterialInfo) {
        val pickListPullOffShelf = (pickListRecyclerView.adapter as PickListOffShelfAdapter).getItem(position)
        var checkDialog: AlertDialog? = null
        pickListPullOffShelf?.let { it ->
            it.lockNumber = info.lockNumber
            val needCheckCount = it.checkFlag && (it.totalNum == it.lockNumber)
            val codeInputView = LayoutInflater.from(context!!).inflate(R.layout.dialog_under_shelf_package, null)
            val editText = codeInputView.findViewById<EditText>(R.id.underShelfEditText)
            val underCountEditText = codeInputView.findViewById<EditText>(R.id.underShelfNumber)
            codeInputView.findViewById<TextView>(R.id.packageCount).text = "${info.totalNumber}"
            codeInputView.findViewById<TextView>(R.id.underShelfNumber).text = "${it.totalNum}"
            codeInputView.findViewById<View>(R.id.checkLayout).visibility = if (needCheckCount) View.VISIBLE else View.GONE
            codeInputView.find<View>(R.id.cancelButton).onClick {
                checkDialog?.dismiss()
            }
            codeInputView.find<View>(R.id.okButton).onClick { _ ->
                val underCount = if (TextUtils.isEmpty(underCountEditText.text.toString())) BigDecimal.ZERO else underCountEditText.text.toString().toBigDecimal()
                val checkCount = if (TextUtils.isEmpty(editText.text.toString())) BigDecimal.ZERO else editText.text.toString().toBigDecimal()
                if (underCount.compareTo(info.totalNumber) == 1 || underCount.compareTo(it.totalNum) == 1) {
                    codeInputView.findViewById<EditText>(R.id.underShelfNumber).error = getString(R.string.error_under_shelf_count)
                    return@onClick
                }
                if (underCount.compareTo(BigDecimal.ZERO) < 1) {
                    codeInputView.findViewById<EditText>(R.id.underShelfNumber).error = getString(R.string.error_under_shelf_count_less_than_zero)
                    return@onClick
                }
                it.actulOffShellNumber = underCount
                if (needCheckCount) {
                    if (checkCount.compareTo(info.totalNumber.minus(underCount)) != 0) {
                        AlertDialog.Builder(getContext()!!).setTitle(R.string.info).setMessage(R.string.under_shelf_no_match_count)
                                .setPositiveButton(R.string.ok) { _, _ ->
                                    checkDialog?.show()
                                }.setNegativeButton(R.string.cancel) { _, _ ->
                                    it.isAddedPackage = true
                                    pickListRecyclerView.adapter?.notifyItemChanged(position)
                                }.show()
                    } else
                        it.isAddedPackage = true
                } else
                    it.isAddedPackage = true
                it.checkCount = checkCount
                pickListRecyclerView.adapter?.notifyItemChanged(position)
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

    override fun addPackageSucceed(prNo: String, stCode: String) {
        if (!addPackage.contains(stCode))
            addPackage.add(stCode)
//        (pickListRecyclerView.adapter as PickListOffShelfAdapter).apply {
//            data.forEachWithIndex { i, pickListPullOffShelf ->
//                if (prNo == pickListPullOffShelf.pickListCode) {
//                    pickListPullOffShelf.packageCodes.add(stCode)
//                    notifyItemChanged(i)
//                }
//            }
//        }
        succeed(prNo)
    }

    override fun deletePackageSucceed(prNo: String, stCode: String) {
        addPackage.remove(stCode)
        (pickListRecyclerView.adapter as PickListOffShelfAdapter).apply {
            var index = -1
            data.forEachWithIndex { i, pickListPullOffShelf ->
                if (prNo == pickListPullOffShelf.pickListCode && stCode == pickListPullOffShelf.storageUnitInfoCode) {
                    index = i
                }
            }
            if (index >= 0)
                remove(index)
        }
    }

    override fun getPickListPullOffShelfListFailed() {
        if (arguments?.containsKey("pickCode") == true)
            activity?.onBackPressed()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    private fun updateTitle() {
        when (viewFlipper.displayedChild) {
            0 -> {
                toolbar_base.title = arguments?.getString("title")
                        ?: getString(R.string.title_storage_unit_info)
            }
            1 -> {
                toolbar_base.titleResource = R.string.title_pick_list_add_package
            }
        }
    }

    override fun getPickListPullOffShelfList(barCode: String, pickListPullOffShelfList: List<PickListPullOffShelf>) {
        prNo = barCode
        (pickListRecyclerView.adapter as PickListOffShelfAdapter).setNewData(pickListPullOffShelfList)
    }

    override fun underShelfSucceed() {
        (pickListRecyclerView.adapter as PickListOffShelfAdapter).setNewData(null)
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(R.string.under_shelf_succeed).setNegativeButton(R.string.ok){
            _,_->
            underShelfCallBack?.underShelfSucceed(prNo)
            activity?.onBackPressed()
        }.create()
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

    class PickListOffShelfAdapter(val fragment: UnderShelfFragment) : BaseQuickAdapter<PickListPullOffShelf, BaseViewHolder>(R.layout.item_pick_list_info_under_shelf) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: PickListPullOffShelf) {
            val stCode = if (TextUtils.isEmpty(item.parentStorageUnitInfoCode)) item.storageUnitInfoCode else item.parentStorageUnitInfoCode
            item.isAddedPackage = fragment.addPackage.contains(stCode)
            val dataBind = DataBindingUtil.bind<ItemPickListInfoUnderShelfBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind?.info = item
            helper.addOnClickListener(R.id.deletePackageButton)
        }
    }
}