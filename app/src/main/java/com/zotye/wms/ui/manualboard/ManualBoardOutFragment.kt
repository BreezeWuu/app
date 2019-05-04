package com.zotye.wms.ui.manualboard

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.ManualBoardDeliveryDto
import com.zotye.wms.data.api.model.MaterialPullResult
import com.zotye.wms.data.api.model.StoragePackageMaterialInfo
import com.zotye.wms.databinding.ItemManualBoardBinding
import com.zotye.wms.databinding.ItemStorageMaterialInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import com.zotye.wms.ui.picking.PickingFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_manual_board_out.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/04/12
 */
class ManualBoardOutFragment : BaseFragment(), ManualBoardOutContract.ManualBoardOutView, ScannerDelegate {

    @Inject
    lateinit var presenter: ManualBoardOutContract.ManualBoardOutPresenter

    companion object {
        fun newInstance(title: String): ManualBoardOutFragment {
            val fragment = ManualBoardOutFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manual_board_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title")
                ?: getString(R.string.title_manual_board_out)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        manualBoardRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ManualBoardInfoAdapter()
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.manual_board_empty)
        adapter.emptyView = emptyView
        manualBoardRecyclerView.adapter = adapter
        kbCodeScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@ManualBoardOutFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        kbCodeInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.kan_ban_id)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_kan_ban_id).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        materialIdInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.material_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_material_id).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                presenter.getManualBoardList("", editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        outConfirm.setOnClickListener {
            val endTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()).toString()
            if ((manualBoardRecyclerView.adapter as ManualBoardInfoAdapter).data.size == 0) {
                showMessage("请先添加看板！")
            } else {
                AlertDialog.Builder(context!!).setTitle(R.string.is_urgent_out).setNegativeButton(R.string.`is`) { _, _ ->
                    (manualBoardRecyclerView.adapter as ManualBoardInfoAdapter).data.apply {
                        forEach {
                            it.demandTime = endTime
                            it.urgentFlag = "1"
                        }
                        presenter.saveManualBoardOut(this)
                    }
                }.setPositiveButton(R.string.isnot) { _, _ ->
                    (manualBoardRecyclerView.adapter as ManualBoardInfoAdapter).data.apply {
                        forEach {
                            it.demandTime = endTime
                            it.urgentFlag = "0"
                        }
                        presenter.saveManualBoardOut(this)
                    }
                }.show()

//                val calendar = Calendar.getInstance()
//                var dateDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { dateDialogView, year, month, dayOfMonth ->
//                    if(dateDialogView.isShown) {
//                        var dialog = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { dialogView, hourOfDay, minute ->
//                            if(dialogView.isShown) {
//                                val selectDate = Date(year - 1900, month, dayOfMonth, hourOfDay, minute)
//                                val endTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(selectDate).toString()
//                                (manualBoardRecyclerView.adapter as ManualBoardInfoAdapter).data.apply {
//                                    forEach {
//                                        it.demandTime = endTime
//                                    }
//                                    presenter.saveManualBoardOut(this)
//                                }
//                            }
//                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
//                        dialog.show()
//                        dialog.setCanceledOnTouchOutside(false)
//                    }
//                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
//                dateDialog.show()
//                dateDialog.setCanceledOnTouchOutside(false)
            }
        }
    }

    override fun succeed(result: String) {
        presenter.getManualBoardList(result, "")
    }

    override fun getManualBoardList(showWarnings: Boolean, warnMessage: String, manualBoardList: List<ManualBoardDeliveryDto>?) {
        if(showWarnings){
            AlertDialog.Builder(context!!).setTitle(R.string.warn).setMessage(warnMessage).setNegativeButton(R.string.goon) { _, _ ->
                (manualBoardRecyclerView.adapter as ManualBoardInfoAdapter).setNewData(manualBoardList)
            }.setPositiveButton(R.string.cancel, null).show()
        }else{
            (manualBoardRecyclerView.adapter as ManualBoardInfoAdapter).setNewData(manualBoardList)
        }
    }

    override fun saveManualBoardOutSucceed(message: String, result: List<MaterialPullResult>) {
        showMessage(message)
        (manualBoardRecyclerView.adapter as ManualBoardInfoAdapter).setNewData(listOf())
    }

    class ManualBoardInfoAdapter : BaseQuickAdapter<ManualBoardDeliveryDto, BaseViewHolder>(R.layout.item_manual_board) {

        override fun convert(helper: BaseViewHolder, item: ManualBoardDeliveryDto) {
            val dataBind = DataBindingUtil.bind<ItemManualBoardBinding>(helper.itemView)
            dataBind?.info = item
            helper.getView<View>(R.id.deleteButton).setOnClickListener {
                AlertDialog.Builder(it.context).setTitle(R.string.delete_item_confirm).setNegativeButton(R.string.ok) { _, _ ->
                    this@ManualBoardInfoAdapter.remove(helper.adapterPosition)
                }.setPositiveButton(R.string.cancel, null).show()
            }
        }
    }
}