package com.zotye.wms.ui.goods

import android.app.DatePickerDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zotye.wms.R
import com.zotye.wms.data.api.model.ValidSlInfoDto
import com.zotye.wms.data.api.model.receipt.DeliveryNoteInfoDto
import com.zotye.wms.data.api.model.receipt.DeliveryNoteInfoResponse
import com.zotye.wms.data.api.model.receipt.ReceiveDetailDto
import com.zotye.wms.databinding.ItemDeliveryNoteReceiptInfoBinding
import com.zotye.wms.databinding.ItemDeliveryNoteReceiptMaterialInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_delivery_note_receive.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*
import javax.inject.Inject


/**
 * Created by hechuangju on 2018/03/07
 */
class DeliveryNoteReceiveFragment : BaseFragment(), ScannerDelegate, DeliveryNoteReceiveContract.DeliveryNoteReceiveView {

    @Inject
    lateinit var presenter: DeliveryNoteReceiveContract.DeliveryNoteReceivePresenter

    companion object {
        fun newInstance(title: String): DeliveryNoteReceiveFragment {
            val fragment = DeliveryNoteReceiveFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_delivery_note_receive, container, false)
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
        deliveryNoteScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@DeliveryNoteReceiveFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        deliveryNoteInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.picklist_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_picklist_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }

        val adapter = DeliveryNoteReceiptListAdapter(null)
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.delivery_note_info_empty)
        adapter.emptyView = emptyView
        deliveryNoteInfoRecyclerView.layoutManager = LinearLayoutManager(context)
        deliveryNoteInfoRecyclerView.adapter = adapter
        adapter.bindToRecyclerView(deliveryNoteInfoRecyclerView)
        adapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.editButton -> {
                    val item = adapter.getItem(position) as ReceiveDetailDto
                    if (item.isEditEnable) {
                        val batchNumString = (adapter.getViewByPosition(position, R.id.batchNumber) as TextView).text.toString()
                        val reciprocalNumString = (adapter.getViewByPosition(position, R.id.reciprocalNumber) as TextView).text.toString()
                        val lackNumberString = (adapter.getViewByPosition(position, R.id.lackNumber) as TextView).text.toString()
                        val unqualifyNumberString = (adapter.getViewByPosition(position, R.id.unqualifyNumber) as TextView).text.toString()
                        item.batchNum = batchNumString
                        item.receiveNum = if (reciprocalNumString.isNullOrBlank()) 0 else reciprocalNumString.toLong()
                        item.lackNum = if (lackNumberString.isNullOrBlank()) 0 else lackNumberString.toLong()
                        item.unqualifyNum = if (unqualifyNumberString.isNullOrBlank()) 0 else unqualifyNumberString.toLong()
                        if (item.isBatch!! && TextUtils.isEmpty(batchNumString)) {
                            Toast.makeText(context, R.string.delivery_batch_num_error, Toast.LENGTH_SHORT).show()
                            return@setOnItemChildClickListener
                        }
                        if ((item.receiveNum + item.unqualifyNum + item.lackNum) != item.requireNum) {
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
                R.id.viewDetailButton->{
                    val item = adapter.getItem(position) as ReceiveDetailDto
                    fragmentManager?.beginTransaction()?.add(R.id.main_content, DeliveryNoteChildDetailFragment.newInstance(item))?.addToBackStack(null)?.commit()
                }
                R.id.storageLocationText -> {
                    val item = adapter.getItem(position) as DeliveryNoteInfoDto
                    presenter.getSlInfoForDeliveryNote(item)
                }
            }
        }
    }

    override fun succeed(result: String) {
        presenter.getDeliveryNoteInfoByCode(result)
    }

    override fun getDeliveryNoteInfoByCode(data: DeliveryNoteInfoResponse?) {
        data?.let {
            val adapter = deliveryNoteInfoRecyclerView.adapter as DeliveryNoteReceiptListAdapter
            it.deliveryNoteInfo?.subItems = it.receiveDetailList
            adapter.setNewData(null)
            adapter.addData(it.deliveryNoteInfo!!)
            adapter.expandAll()
        }
    }

    override fun getSlInfoForDeliveryNote(note: DeliveryNoteInfoDto, data: List<ValidSlInfoDto>?) {
        val adapter = deliveryNoteInfoRecyclerView.adapter as DeliveryNoteReceiptListAdapter
        val arrayAdapter: ArrayAdapter<ValidSlInfoDto> = ArrayAdapter(context, android.R.layout.simple_list_item_single_choice)
        arrayAdapter.addAll(data)
        AlertDialog.Builder(context!!).setTitle(R.string.choose_storage_location).setAdapter(arrayAdapter) { dialog, which ->
            val validSlInfoDto = arrayAdapter.getItem(which)
            (adapter.getItem(0) as DeliveryNoteInfoDto).storageLocationCode = validSlInfoDto.slCode
            (adapter.getItem(0) as DeliveryNoteInfoDto).storageLocationName = validSlInfoDto.slName
            adapter.notifyItemChanged(0)
        }.show()
    }

    class DeliveryNoteReceiptListAdapter(data: MutableList<MultiItemEntity>?) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

        init {
            addItemType(DeliveryNoteInfoDto.TYPE_NOTE_INFO, R.layout.item_delivery_note_receipt_info)
            addItemType(ReceiveDetailDto.TYPE_RECEIVE_DETAIL, R.layout.item_delivery_note_receipt_material_info);
        }

        override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
            when (helper.itemViewType) {
                DeliveryNoteInfoDto.TYPE_NOTE_INFO -> {
                    val dataBind = DataBindingUtil.bind<ItemDeliveryNoteReceiptInfoBinding>(helper.itemView)
                    dataBind?.info = item as DeliveryNoteInfoDto
                    helper.getView<Button>(R.id.expandButton).onClick { _ ->
                        if (item.isExpanded) {
                            collapse(helper.adapterPosition)
                        } else {
                            expand(helper.adapterPosition)
                        }
                    }
                    helper.getView<Button>(R.id.expandButton).setText(if (item.isExpanded) R.string.picklist_material_collapse else R.string.picklist_material_expand)
                    helper.getView<TextView>(R.id.postTimeText).onClick {
                        val calendar = Calendar.getInstance()
                        val yy = calendar.get(Calendar.YEAR)
                        val mm = calendar.get(Calendar.MONTH)
                        val dd = calendar.get(Calendar.DAY_OF_MONTH)
                        val datePicker = DatePickerDialog(mContext, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                            val date = (year.toString() + "-" + (monthOfYear + 1).toString()
                                    + "-" + dayOfMonth.toString())
                            helper.getView<TextView>(R.id.postTimeText).text = date
                        }, yy, mm, dd)
                        datePicker.show()
                    }
                    helper.addOnClickListener(R.id.storageLocationText)
                }
                ReceiveDetailDto.TYPE_RECEIVE_DETAIL -> {
                    val dataBind = DataBindingUtil.bind<ItemDeliveryNoteReceiptMaterialInfoBinding>(helper.itemView)
                    dataBind?.info = item as ReceiveDetailDto
                    helper.addOnClickListener(R.id.editButton)
                    helper.addOnClickListener(R.id.viewDetailButton)
                }
            }
        }
    }
}