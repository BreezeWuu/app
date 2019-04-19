package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.ManualBoardDeliveryDto
import com.zotye.wms.data.api.model.PutAwayInfo
import com.zotye.wms.databinding.ItemManualBoardBinding
import com.zotye.wms.databinding.ItemStoragePositionInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import com.zotye.wms.ui.manualboard.ManualBoardOutFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_manual_board_out.*
import kotlinx.android.synthetic.main.fragment_manual_board_out.kbCodeInput
import kotlinx.android.synthetic.main.fragment_manual_board_out.kbCodeScanner
import kotlinx.android.synthetic.main.fragment_storage_position_info.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/04/19
 */
class StoragePositionInfoFragment : BaseFragment(), StoragePositionInfoContract.StoragePositionInfoView, ScannerDelegate {

    companion object {
        fun newInstance(title: String): StoragePositionInfoFragment {
            val fragment = StoragePositionInfoFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: StoragePositionInfoContract.StoragePositionInfoPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_storage_position_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title")
                ?: getString(R.string.title_storage_position_info)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        spRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = PutAwayInfoAdapter()
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.manual_board_empty)
        adapter.emptyView = emptyView
        spRecyclerView.adapter = adapter
        kbCodeScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@StoragePositionInfoFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        kbCodeInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.package_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
    }

    override fun succeed(result: String) {
        presenter.getStoragePositionInfo(result)
    }

    override fun getStoragePositionInfo(list: List<PutAwayInfo>?) {

    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    class PutAwayInfoAdapter : BaseQuickAdapter<PutAwayInfo, BaseViewHolder>(R.layout.item_storage_position_info) {

        override fun convert(helper: BaseViewHolder, item: PutAwayInfo) {
            val dataBind = DataBindingUtil.bind<ItemStoragePositionInfoBinding>(helper.itemView)
            dataBind?.info = item
        }
    }
}