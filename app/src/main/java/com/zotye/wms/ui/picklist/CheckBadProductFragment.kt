package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zotye.wms.R
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.PickListInfo
import com.zotye.wms.data.api.model.PickListMaterialInfo
import com.zotye.wms.data.api.model.checkbad.GetPickReceiptShelfDetailRequestDto
import com.zotye.wms.data.api.model.checkbad.PickReceiptShelfDetail
import com.zotye.wms.databinding.ItemPickListInfoBinding
import com.zotye.wms.databinding.ItemPickListMaterialInfoBinding
import com.zotye.wms.databinding.ItemStorageUnitInfoMaterialBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_check_bad_product.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.collections.forEachByIndex
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/25
 */
class CheckBadProductFragment : BaseFragment(), ScannerDelegate, CheckBadProductContract.CheckBadProductView {

    companion object {
        fun newInstance(title: String): CheckBadProductFragment {
            val fragment = CheckBadProductFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: CheckBadProductContract.CheckBadProductPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_check_bad_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_check_bad_product)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        pickListScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@CheckBadProductFragment)
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
        val adapter = PickListAdapter(null)
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.pick_list_empty)
        adapter.emptyView = emptyView
        pickListRecyclerView.layoutManager = LinearLayoutManager(context)
        pickListRecyclerView.adapter = adapter
        adapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, _, position ->
            val item = adapter.getItem(position) as PickListMaterialInfo
            val parentItem = adapter.data[0] as PickListInfo
            val codeInputView = LayoutInflater.from(context).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.setHint(R.string.count_format)
            AlertDialog.Builder(context!!).setTitle(R.string.action_input_under_shelf_count).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                val count = if (TextUtils.isEmpty(editText.text.toString())) 0 else editText.text.toString().toInt()
                if (count <= 0) {
                    showMessage(R.string.under_shelf_count_error)
                } else {
                    val request = ArrayList<GetPickReceiptShelfDetailRequestDto>()
                    val getPickReceiptShelfDetailRequestDto = GetPickReceiptShelfDetailRequestDto(item.materialId, parentItem.outSlId, parentItem.supplierId, count.toLong())
                    request.add(getPickReceiptShelfDetailRequestDto)
                    presenter.getPickReceiptShelfDetail(request)
                }
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        confirmButton.onClick {
            if (materialSpRecyclerView.adapter is MaterialSpAdapter) {
                (materialSpRecyclerView.adapter as MaterialSpAdapter).data.forEachByIndex { pickReceiptShelfDetail ->
                    if (!pickReceiptShelfDetail.confirmed && !TextUtils.isEmpty(pickReceiptShelfDetail.unitCode)) {
                        showMessage(R.string.not_confirm_storage_unit_code)
                        return@onClick
                    }
                }
                presenter.externalCheckPickReceiptConfirm((materialSpRecyclerView.adapter as MaterialSpAdapter).data)
            }
        }
    }

    override fun externalCheckPickReceiptConfirmSucceed() {
        viewFlipper.showPrevious()
        (pickListRecyclerView.adapter as PickListAdapter).setNewData(null)
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(R.string.under_shelf_succeed).setNegativeButton(R.string.ok, null).create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun externalCheckPickReceiptConfirmFailed(message:String) {
        viewFlipper.showPrevious()
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(message).setNegativeButton(R.string.ok, null).create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun succeed(result: String) {
        presenter.getPickListInfoByCode(result)
    }

    override fun getPickListInfo(pickListInfo: PickListInfo) {
        val adapter = pickListRecyclerView.adapter as PickListAdapter
        pickListRecyclerView.post {
            pickListInfo.subItems = pickListInfo.materialInfoList
            adapter.setNewData(null)
            adapter.addData(pickListInfo)
            adapter.expandAll()
        }
    }

    override fun getPickReceiptShelfDetailList(pickReceiptShelfDetails: List<PickReceiptShelfDetail>?) {
        pickReceiptShelfDetails?.let {
            val adapter = MaterialSpAdapter()
            val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
            emptyView.find<TextView>(R.id.text_error).text = getString(R.string.material_storage_unit_empty)
            adapter.emptyView = emptyView
            materialSpRecyclerView.layoutManager = LinearLayoutManager(context)
            materialSpRecyclerView.adapter = adapter
            adapter.setNewData(pickReceiptShelfDetails)
            adapter.setOnItemChildClickListener { _, _, position ->
                val item = adapter.getItem(position)
                val fragment = BarCodeScannerFragment()
                fragment.setScannerDelegate(object : ScannerDelegate {
                    override fun succeed(result: String) {
                        if (item?.unitCode == result) {
                            item.confirmed = true
                            adapter.notifyItemChanged(position)
                        } else {
                            showMessage(R.string.not_match_storage_unit_code_id)
                        }
                    }
                })
                fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
            }
            viewFlipper.showNext()
        }
    }

    override fun getBarCodeInfo(barCodeInfo: BarcodeInfo?) {

    }

    private fun updateTitle() {
        when (viewFlipper.displayedChild) {
            0 -> {
                toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_check_bad_product)
            }
            1 -> {
                toolbar_base.titleResource = R.string.material_storage_unit_info
            }
        }
    }

    override fun canBackPressed(): Boolean {
        return if (viewFlipper.displayedChild != 0) {
            viewFlipper.showPrevious()
            updateTitle()
            false
        } else
            true
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    class MaterialSpAdapter : BaseQuickAdapter<PickReceiptShelfDetail, BaseViewHolder>(R.layout.item_storage_unit_info_material) {

        override fun convert(helper: BaseViewHolder, item: PickReceiptShelfDetail) {
            val dataBind = DataBindingUtil.bind<ItemStorageUnitInfoMaterialBinding>(helper.itemView)
            dataBind.info = item
            helper.addOnClickListener(R.id.confirmCodeButton)
        }
    }

    class PickListAdapter(data: MutableList<MultiItemEntity>?) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

        init {
            addItemType(PickListInfo.TYPE_PICK_LIST, R.layout.item_pick_list_info)
            addItemType(PickListMaterialInfo.TYPE_PICK_LIST_MATERIAL_INFO, R.layout.item_pick_list_material_info);
        }

        override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
            when (helper.itemViewType) {
                PickListInfo.TYPE_PICK_LIST -> {
                    val dataBind = DataBindingUtil.bind<ItemPickListInfoBinding>(helper.itemView)
                    dataBind.info = item as PickListInfo
                    helper.getView<Button>(R.id.expandButton).onClick { view ->
                        if (item.isExpanded) {
                            collapse(helper.adapterPosition)
                        } else {
                            expand(helper.adapterPosition)
                        }
                    }
                    helper.getView<Button>(R.id.deleteButton).visibility = View.GONE
                    helper.getView<Button>(R.id.expandButton).setText(if (item.isExpanded) R.string.picklist_material_collapse else R.string.picklist_material_expand)
                }
                PickListMaterialInfo.TYPE_PICK_LIST_MATERIAL_INFO -> {
                    helper.getView<Button>(R.id.underShelfButton).visibility = View.VISIBLE
                    helper.addOnClickListener(R.id.underShelfButton)
                    val dataBind = DataBindingUtil.bind<ItemPickListMaterialInfoBinding>(helper.itemView)
                    dataBind.info = item as PickListMaterialInfo
                }
            }
        }
    }
}