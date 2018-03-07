package com.zotye.wms.ui.goods

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.zotye.wms.R
import com.zotye.wms.data.api.model.receipt.DeliveryNoteInfoResponse
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_delivery_note_receive.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
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

        val adapter = StrictReceiveFragment.PickReceiptListAdapter(null)
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.delivery_note_info_empty)
        adapter.emptyView = emptyView
        deliveryNoteInfoRecyclerView.layoutManager = LinearLayoutManager(context)
        deliveryNoteInfoRecyclerView.adapter = adapter
        adapter.bindToRecyclerView(deliveryNoteInfoRecyclerView)
        adapter.setOnItemChildClickListener { _, _, position ->
        }
    }

    override fun succeed(result: String) {
        presenter.getDeliveryNoteInfoByCode(result)
    }

    override fun getDeliveryNoteInfoByCode(data: DeliveryNoteInfoResponse?) {

    }
}