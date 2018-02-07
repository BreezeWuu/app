package com.zotye.wms.ui.picking

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
import com.zotye.wms.data.api.model.StoragePackageMaterialInfo
import com.zotye.wms.data.api.model.checkbad.PickReceiptShelfDetail
import com.zotye.wms.databinding.ItemStorageMaterialInfoBinding
import com.zotye.wms.databinding.ItemStorageUnitInfoMaterialBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_picking.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/02/07
 */
class PickingFragment : BaseFragment(), PickingContract.PickingView, ScannerDelegate {

    companion object {
        fun newInstance(title: String): PickingFragment {
            val fragment = PickingFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: PickingContract.PickingPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_picking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_picking)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        pickingScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@PickingFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        pickingInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.package_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        val adapter = StoragePackageMaterialInfoAdapter()
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.picking_empty)
        adapter.emptyView = emptyView
        pickingRecyclerView.layoutManager = LinearLayoutManager(context)
        pickingRecyclerView.adapter = adapter
    }

    override fun succeed(result: String) {
        presenter.getPickingBarCodeInfo(result)
    }

    override fun getPickingBarCodeInfo(storagePackageMaterialInfoList: List<StoragePackageMaterialInfo>) {
        (pickingRecyclerView.adapter as StoragePackageMaterialInfoAdapter).addData(storagePackageMaterialInfoList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    class StoragePackageMaterialInfoAdapter : BaseQuickAdapter<StoragePackageMaterialInfo, BaseViewHolder>(R.layout.item_storage_material_info) {

        override fun convert(helper: BaseViewHolder, item: StoragePackageMaterialInfo) {
            val dataBind = DataBindingUtil.bind<ItemStorageMaterialInfoBinding>(helper.itemView)
            dataBind.info = item
        }
    }

}