package com.zotye.wms.ui.picklist

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.zotye.wms.R
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_shelf_confirm.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/04/11
 */
class ShelfConfirmFragment : BaseFragment(), ShelfConfirmContract.ShelfConfirmView {

    @Inject
    lateinit var presenter: ShelfConfirmContract.ShelfConfirmPresenter

    companion object {
        fun newInstance(title: String): ShelfConfirmFragment {
            val fragment = ShelfConfirmFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shelf_confirm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title")
                ?: getString(R.string.title_shelf_confirm)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        codeScanner.setOnClickListener {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(object : ScannerDelegate {
                override fun succeed(result: String) {
                    pickListCode.text = result
                }
            })
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        codeInput.setOnClickListener {
            val codeInputView = LayoutInflater.from(context!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.package_code)
            editText.setText(pickListCode.text)
            AlertDialog.Builder(context!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                pickListCode.text = (editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        stCodeScanner.setOnClickListener {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(object : ScannerDelegate {
                override fun succeed(result: String) {
                    stCode.text = result
                }
            })
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        stCodeInput.setOnClickListener {
            val codeInputView = LayoutInflater.from(context!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.storage_unit_code)
            editText.setText(stCode.text)
            AlertDialog.Builder(context!!).setTitle(R.string.action_input_storage_unit_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                stCode.text = (editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        onlineSubmit.setOnClickListener {
            when {
                TextUtils.isEmpty(pickListCode.text.toString()) -> showMessage(R.string.error_empty_package)
                TextUtils.isEmpty(stCode.text.toString()) -> showMessage(R.string.error_empty_st_code)
                else -> presenter.shelfConfirm(pickListCode.text.toString(), stCode.text.toString())
            }
        }
    }

    override fun shelfConfirmSucceed() {
        pickListCode.text = ""
        stCode.text = ""
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }
}