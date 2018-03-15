package com.zotye.wms.ui.goods

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zotye.wms.R
import com.zotye.wms.data.api.model.PickListInfo
import com.zotye.wms.data.api.model.PickListMaterialInfo
import com.zotye.wms.data.api.model.picking.PdaPickReceiptDetailDto
import com.zotye.wms.data.api.model.picking.PickReceiptDto
import com.zotye.wms.databinding.ItemPickReceiptInfoBinding
import com.zotye.wms.databinding.ItemPickReceiptMaterialInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_strict_receive.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.onUiThread
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/03/05
 */
class StrictReceiveFragment : BaseFragment(), ScannerDelegate, StrictReceiveContract.StrictReceiveView {

    @Inject
    lateinit var presenter: StrictReceiveContract.StrictReceivePresenter

    companion object {
        fun newInstance(title: String): StrictReceiveFragment {
            val fragment = StrictReceiveFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_strict_receive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: ""
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        pickScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@StrictReceiveFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        pickInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.picklist_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_picklist_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }

        val adapter = PickReceiptListAdapter(null)
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.pick_receipt_info_empty)
        adapter.emptyView = emptyView
        pickInfoRecyclerView.layoutManager = LinearLayoutManager(context)
        pickInfoRecyclerView.adapter = adapter
        adapter.bindToRecyclerView(pickInfoRecyclerView)
        adapter.setOnItemChildClickListener { _, _, position ->
            val item = adapter.getItem(position) as PdaPickReceiptDetailDto
            if (item.isEditEnable) {
                val reciprocalNumString = (adapter.getViewByPosition(position, R.id.reciprocalNumber) as TextView).text.toString()
                val lackNumberString = (adapter.getViewByPosition(position, R.id.lackNumber) as TextView).text.toString()
                val unqualifyNumberString = (adapter.getViewByPosition(position, R.id.unqualifyNumber) as TextView).text.toString()
                item.reciprocalNum = if (reciprocalNumString.isNullOrBlank()) 0 else reciprocalNumString.toLong()
                item.lackNum = if (lackNumberString.isNullOrBlank()) 0 else lackNumberString.toLong()
                item.unqualifyNum = if (unqualifyNumberString.isNullOrBlank()) 0 else unqualifyNumberString.toLong()
                if ((item.reciprocalNum + item.unqualifyNum + item.lackNum) != item.deliveryCount) {
                    Toast.makeText(context, R.string.delivery_num_error, Toast.LENGTH_SHORT).show()
                    return@setOnItemChildClickListener
                }
            }
            item.isEditEnable = !item.isEditEnable
            adapter.notifyItemChanged(position)
            if (item.isEditEnable) {
                (adapter.getViewByPosition(position, R.id.reciprocalNumber) as EditText).requestFocus()
            } else {
                hideKeyboard((adapter.getViewByPosition(position, R.id.reciprocalNumber) as EditText))
            }
        }
        confirmButton.onClick {
            if (adapter.data.isEmpty())
                showMessage(R.string.error_no_available_pick_info)
            else {
                doAsync {
                    adapter.data.forEach {
                        val pickReceiptDto = it as PickReceiptDto
                        pickReceiptDto.pickReceiptDetail?.forEach {
                            if (it.isEditEnable) {
                                showMessage(R.string.error_no_save_receive_number)
                                return@doAsync
                            }
                        }
                        onUiThread {
                            presenter.truckReceive(adapter.data)
                        }
                    }
                }
            }
        }
    }

    override fun succeed(result: String) {
        presenter.getPickReceiptInfoByCode(result)
    }


    override fun getPickReceiptInfoByCode(pickReceiptDto: PickReceiptDto?) {
        pickReceiptDto?.let { pickInfo ->
            val adapter = pickInfoRecyclerView.adapter as PickReceiptListAdapter
            pickInfo.pickReceiptDetail?.map {
                if (pickInfo.pickReceiptSource == "3") {
                    it.reciprocalNum = it.deliveryCount
                } else
                    it.reciprocalNum = it.deliveryCount - it.lackNum - it.unqualifyNum
                it.parent = pickInfo
            }
            pickInfo.subItems = pickInfo.pickReceiptDetail
            adapter.addData(pickInfo)
            adapter.expandAll()
        }
    }

    override fun truckReceiveSucceed() {
        (pickInfoRecyclerView.adapter as PickReceiptListAdapter).setNewData(null)
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(R.string.strict_receive_succeed).setNegativeButton(R.string.ok, null).create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    class PickReceiptListAdapter(data: MutableList<MultiItemEntity>?) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

        init {
            addItemType(PickListInfo.TYPE_PICK_LIST, R.layout.item_pick_receipt_info)
            addItemType(PickListMaterialInfo.TYPE_PICK_LIST_MATERIAL_INFO, R.layout.item_pick_receipt_material_info);
        }

        override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
            when (helper.itemViewType) {
                PickListInfo.TYPE_PICK_LIST -> {
                    val dataBind = DataBindingUtil.bind<ItemPickReceiptInfoBinding>(helper.itemView)
                    dataBind?.info = item as PickReceiptDto
                    helper.getView<Button>(R.id.expandButton).onClick { _ ->
                        if (item.isExpanded) {
                            collapse(helper.adapterPosition)
                        } else {
                            expand(helper.adapterPosition)
                        }
                    }
                    helper.getView<Button>(R.id.expandButton).setText(if (item.isExpanded) R.string.picklist_material_collapse else R.string.picklist_material_expand)
                }
                PickListMaterialInfo.TYPE_PICK_LIST_MATERIAL_INFO -> {
                    val dataBind = DataBindingUtil.bind<ItemPickReceiptMaterialInfoBinding>(helper.itemView)
                    dataBind?.info = item as PdaPickReceiptDetailDto
                    val pickReceiptDto = item.parent
                    if (pickReceiptDto?.pickReceiptSource != "3") {
                        helper.getView<View>(R.id.editButton).visibility = View.VISIBLE
                        helper.addOnClickListener(R.id.editButton)
                    } else {
                        helper.getView<View>(R.id.editButton).visibility = View.GONE
                    }
                }
            }
        }
    }
}