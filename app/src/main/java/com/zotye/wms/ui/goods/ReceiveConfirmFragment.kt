package com.zotye.wms.ui.goods

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.PackageInfo
import com.zotye.wms.data.api.model.PickListInfo
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemPickListInfoBinding
import com.zotye.wms.databinding.ItemUnReceivePackageBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_receive_confirm.*
import kotlinx.android.synthetic.main.layout_code_scanner.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/24
 */
class ReceiveConfirmFragment : BaseFragment(), ScannerDelegate, ReceiveConfirmContract.ReceiveConfirmView {

    @Inject
    lateinit var presenter: ReceiveConfirmContract.ReceiveConfirmPresenter
    private var barCode: String = ""

    companion object {
        fun newInstance(title: String): ReceiveConfirmFragment {
            val fragment = ReceiveConfirmFragment()
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
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_receive_confirm)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
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
            fragment.setScannerDelegate(this@ReceiveConfirmFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        finishButton.onClick {
            fragmentManager?.beginTransaction()?.remove(this@ReceiveConfirmFragment)?.commit()
        }
        confirmButton.onClick {
            AlertDialog.Builder(getContext()!!).setTitle(R.string.info).setMessage(R.string.action_receive_confirm_info).setNegativeButton(R.string.ok) { _, _ ->
                presenter.logisticsReceiveConfirm(barCode)
            }.setPositiveButton(R.string.cancel, null).show()
        }
    }

    override fun succeed(result: String) {
        barCode = result
        presenter.logisticsReceiveConfirmInfoByCode(result)
    }

    override fun getUnReceivePackageList(packageInfoList: List<PackageInfo>) {
        viewSwitcher.showNext()
        toolbar_base.titleResource = R.string.un_receive_package_list
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = PackageAdapter()
        recyclerView.adapter = adapter
        adapter.setNewData(packageInfoList)
    }

    override fun getUnReceivePickInfoList(pickInfoList: List<PickListInfo>) {
        viewSwitcher.showNext()
        toolbar_base.titleResource = R.string.un_receive_pick_list
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = PickInfoAdapter()
        recyclerView.adapter = adapter
        adapter.setNewData(pickInfoList)
    }

    override fun packageReceiveSucceed(message: String) {
        viewSwitcher.showPrevious()
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_receive_confirm)
        AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(message).setNegativeButton(R.string.ok, null).show()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    class PackageAdapter : BaseQuickAdapter<PackageInfo, BaseViewHolder>(R.layout.item_un_receive_package) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: PackageInfo) {
            val dataBind = DataBindingUtil.bind<ItemUnReceivePackageBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind?.info = item
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