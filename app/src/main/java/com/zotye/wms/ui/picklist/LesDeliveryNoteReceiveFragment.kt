package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.RecLesDto
import com.zotye.wms.data.api.model.PackageInfo
import com.zotye.wms.data.api.model.PickListInfo
import com.zotye.wms.data.api.model.RecLesDetail
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemPickListInfoBinding
import com.zotye.wms.databinding.ItemUnReceivePackageBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import com.zotye.wms.ui.goods.ReceiveConfirmContract
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_receive_confirm.*
import kotlinx.android.synthetic.main.layout_code_scanner.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textResource
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/05/04
 */
class LesDeliveryNoteReceiveFragment : BaseFragment(), ScannerDelegate, ReceiveConfirmContract.ReceiveConfirmView {

    @Inject
    lateinit var presenter: ReceiveConfirmContract.ReceiveConfirmPresenter

    private var barCode: String = ""

    companion object {
        fun newInstance(title: String): LesDeliveryNoteReceiveFragment {
            val fragment = LesDeliveryNoteReceiveFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_receive_confirm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title")
                ?: getString(R.string.title_receive_confirm)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        confirmButton.textResource = R.string.receive_confirm
        codeInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.delivery_note_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_delivery_note_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        codeScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@LesDeliveryNoteReceiveFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        finishButton.onClick {
            fragmentManager?.beginTransaction()?.remove(this@LesDeliveryNoteReceiveFragment)?.commit()
        }
        confirmButton.onClick {
            val packList = ArrayList<RecLesDetail>()
            (recyclerView.adapter as PackageAdapter).data.forEachIndexed { index, packageInfo ->
                (recyclerView.adapter as PackageAdapter).getViewByPosition(recyclerView, index, R.id.receiveNumberText)?.let {
                    if (it is EditText) {
                        val count = if (TextUtils.isEmpty(it.text)) 0 else it.text.toString().toInt()
                        if (count < 0 || count > packageInfo.deliveryNum?.toInt() ?: 0) {
                            showMessage("请输入有效收货数量")
                            return@onClick
                        } else {
                            packList.add(RecLesDetail(packageInfo.code, BigDecimal(count)))
                        }
                    }
                }
            }
            val resLesDto = RecLesDto(barCode,packList)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.info).setMessage("是否确定整单收货").setNegativeButton(R.string.ok) { _, _ ->
                presenter.reliveryForLesDeliveryNote(resLesDto)
            }.setPositiveButton(R.string.cancel, null).show()
        }
    }

    override fun succeed(result: String) {
        barCode = result
        presenter.reliveryForLesDeliveryNoteByCode(result)
    }

    override fun getUnReceivePackageList(packageInfoList: List<PackageInfo>) {
        viewSwitcher.showNext()
        toolbar_base.titleResource = R.string.un_receive_package_list

        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = PackageAdapter(true)
        recyclerView.adapter = adapter

        receiveRecyclerView.layoutManager = LinearLayoutManager(context)
        val receiveAdapter = PackageAdapter(false)
        receiveRecyclerView.adapter = receiveAdapter


        totalFormatTextView.text = getString(R.string.total_receive_format, packageInfoList.size, packageInfoList.filter {
            it.eState == "2"
        }.size)
        receiveAdapter.setNewData(packageInfoList.filter {
            it.eState == "2"
        })
        adapter.setNewData(packageInfoList.filter {
            it.eState != "2"
        })
    }

    override fun getUnReceivePickInfoList(pickInfoList: List<PickListInfo>) {
        viewSwitcher.showNext()
        toolbar_base.titleResource = R.string.un_receive_pick_list


        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = PickInfoAdapter()
        recyclerView.adapter = adapter


        receiveRecyclerView.layoutManager = LinearLayoutManager(context)
        val receiveAdapter = PickInfoAdapter()
        receiveRecyclerView.adapter = receiveAdapter

        totalFormatTextView.text = getString(R.string.total_receive_format, pickInfoList.size, pickInfoList.filter {
            it.state != 1
        }.size)
        receiveAdapter.setNewData(pickInfoList.filter {
            it.state != 1
        })
        adapter.setNewData(pickInfoList.filter {
            it.state == 1
        })
    }

    override fun packageReceiveSucceed(message: String?) {
        viewSwitcher.showPrevious()
        toolbar_base.title = arguments?.getString("title")
                ?: getString(R.string.title_receive_confirm)
        AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(message).setNegativeButton(R.string.ok, null).show()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    class PackageAdapter(val showReceiveNumber: Boolean) : BaseQuickAdapter<PackageInfo, BaseViewHolder>(R.layout.item_un_receive_package) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: PackageInfo) {
            val dataBind = DataBindingUtil.bind<ItemUnReceivePackageBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind?.info = item
            helper.getView<View>(R.id.receiveNumberLayout).visibility = if (showReceiveNumber) View.VISIBLE else View.GONE
        }
    }

    class PickInfoAdapter : BaseQuickAdapter<PickListInfo, BaseViewHolder>(R.layout.item_pick_list_info) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: PickListInfo) {
            val dataBind = DataBindingUtil.bind<ItemPickListInfoBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind?.info = item
            helper.getView<View>(R.id.actionLayout).visibility = View.GONE
        }
    }
}