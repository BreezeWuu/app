package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.gson.Gson
import com.zotye.wms.R
import com.zotye.wms.data.api.model.*
import com.zotye.wms.databinding.LayoutPickListInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_pick_list.*
import kotlinx.android.synthetic.main.layout_code_scanner.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/25
 */
class CheckBadProductFragment : BaseFragment(), ScannerDelegate {
    companion object {
        fun newInstance(title: String): CheckBadProductFragment {
            val fragment = CheckBadProductFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var pickListInfo: PickListInfo? = null
    @Inject
    lateinit var presenter: UnderShelfContract.UnderShelfPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pick_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_check_bad_product)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        codeScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@CheckBadProductFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        codeInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.picklist_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_picklist_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
    }

    override fun succeed(result: String) {
        presenter.getPickListInfoByCode(result)
    }

    private fun updateTitle() {
        when (viewFlipper.displayedChild) {
            0 -> {
                toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_check_bad_product)
            }
            1 -> {
                toolbar_base.titleResource = R.string.title_pick_list_info
            }
        }
    }

    override fun canBackPressed(): Boolean {
        return if (viewFlipper.displayedChild != 0) {
            viewFlipper.showPrevious()
            updateTitle()
            if (viewFlipper.displayedChild == 0) {
                if (viewFlipper.childCount == 4) {
                    viewFlipper.removeViewAt(3)
                    viewFlipper.removeViewAt(2)
                }
                viewFlipper.removeViewAt(1)
            }
            false
        } else
            true
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

}