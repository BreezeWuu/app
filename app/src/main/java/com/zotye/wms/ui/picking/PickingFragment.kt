package com.zotye.wms.ui.picking

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.CostCenter
import com.zotye.wms.data.api.model.StoragePackageMaterialInfo
import com.zotye.wms.databinding.ItemStorageMaterialInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_picking.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.onUiThread
import java.math.BigDecimal
import javax.inject.Inject


/**
 * Created by hechuangju on 2018/02/07
 */
class PickingFragment : BaseFragment(), PickingContract.PickingView, ScannerDelegate, ChooseCostCenterFragment.ChooseCostCenterDelegate {

    companion object {
        fun newInstance(title: String): PickingFragment {
            val fragment = PickingFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: PickingContract.PickingPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_picking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_picking)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        pickingScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@PickingFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        pickingInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.package_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        val adapter = StoragePackageMaterialInfoAdapter()
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.picking_empty)
        adapter.emptyView = emptyView
        pickingRecyclerView.layoutManager = LinearLayoutManager(context)
        pickingRecyclerView.adapter = adapter
        adapter.bindToRecyclerView(pickingRecyclerView)
        adapter.setOnItemChildClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as StoragePackageMaterialInfo
            val useNumEditText = adapter.getViewByPosition(position, R.id.useNumEditText) as EditText
            if (item.isEditMode) {
                val useNum: BigDecimal = if (TextUtils.isEmpty(useNumEditText.text.toString())) BigDecimal.ZERO else useNumEditText.text.toString().toBigDecimal()
                if (useNum <= BigDecimal.ZERO) {
                    useNumEditText.error = getString(R.string.error_use_number)
                    return@setOnItemChildClickListener
                } else if (useNum > item.availableNum) {
                    useNumEditText.error = getString(R.string.error_use_number_bigger)
                    return@setOnItemChildClickListener
                }
                item.useNum = useNum
                hideKeyboard(useNumEditText)
            } else
                useNumEditText.requestFocus()
            item.isEditMode = !item.isEditMode
            adapter.notifyItemChanged(position)
        }
        pickingConfirmButton.onClick {
            if (adapter.data.isEmpty()) {
                showMessage(R.string.error_material_list_empty)
                return@onClick
            } else {
                if (adapter.data.any { it.isEditMode }) {
                    showMessage(R.string.error_save_use_number)
                } else {
                    val fragment = ChooseCostCenterFragment()
                    fragment.delegate = this@PickingFragment
                    fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
                }
            }
        }
    }

    override fun selected(costCenter: CostCenter) {
        AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(getString(R.string.picking_confirm_format, costCenter.name)).setNegativeButton(R.string.ok) { _, _ ->
            presenter.createPDAProduceAcquire(costCenter.code!!, (pickingRecyclerView.adapter as StoragePackageMaterialInfoAdapter).data)
        }.setPositiveButton(R.string.cancel, null).show()
    }

    override fun createPDAProduceAcquireSucceed(message: String) {
        (pickingRecyclerView.adapter as StoragePackageMaterialInfoAdapter).setNewData(null)
        AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(message).setNegativeButton(R.string.ok, null).show()
    }

    override fun succeed(result: String) {
        if ((pickingRecyclerView.adapter as StoragePackageMaterialInfoAdapter).data.contains(StoragePackageMaterialInfo(result))) {
            showMessage(R.string.error_same_bar_code)
        } else
            presenter.getPickingBarCodeInfo(result)
    }

    override fun getPickingBarCodeInfo(storagePackageMaterialInfoList: List<StoragePackageMaterialInfo>) {
        doAsync {
            storagePackageMaterialInfoList.forEach {
                it.useNum = it.availableNum
            }
            onUiThread {
                (pickingRecyclerView.adapter as StoragePackageMaterialInfoAdapter).addData(storagePackageMaterialInfoList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    class StoragePackageMaterialInfoAdapter : BaseQuickAdapter<StoragePackageMaterialInfo, BaseViewHolder>(R.layout.item_storage_material_info) {

        override fun convert(helper: BaseViewHolder, item: StoragePackageMaterialInfo) {
            val dataBind = DataBindingUtil.bind<ItemStorageMaterialInfoBinding>(helper.itemView)
            dataBind?.info = item
            helper.addOnClickListener(R.id.editButton)
            helper.getView<EditText>(R.id.useNumEditText).error = null
        }
    }
}