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
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.PickListInfo
import com.zotye.wms.data.api.model.PickListMaterialInfo
import com.zotye.wms.data.api.model.StoragePackageMaterialInfo
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemPickListMaterialInfoBinding
import com.zotye.wms.databinding.ItemStorageUnitPackageMaterialInfoBinding
import com.zotye.wms.databinding.ItemStorageUnitPalletInfoBinding
import com.zotye.wms.databinding.LayoutPickListInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import com.zotye.wms.ui.storageunit.StorageUnitInfoFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_pick_list.*
import kotlinx.android.synthetic.main.fragment_storage_unit_info.*
import kotlinx.android.synthetic.main.layout_code_scanner.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/25
 */
class UnderShelfFragment : BaseFragment(), UnderShelfContract.UnderShelfView, ScannerDelegate {
    companion object {
        fun newInstance(title: String): UnderShelfFragment {
            val fragment = UnderShelfFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: UnderShelfContract.UnderShelfPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pick_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_under_shelf)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        codeScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@UnderShelfFragment)
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

    override fun getPickListInfo(pickListInfo: PickListInfo) {
        val pickListInfoView = LayoutInflater.from(context).inflate(R.layout.layout_pick_list_info, viewFlipper, false)
        val dataBind = DataBindingUtil.bind<LayoutPickListInfoBinding>(pickListInfoView)
        dataBind.info = pickListInfo
        val pickListRecyclerView = pickListInfoView.findViewById<RecyclerView>(R.id.pickListRecyclerView)
        pickListRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = PickListMaterialAdapter()
        pickListRecyclerView.adapter = adapter
        adapter.setNewData(pickListInfo.materialInfoList)
        viewFlipper.addView(pickListInfoView)
        viewFlipper.showNext()
        toolbar_base.titleResource = R.string.title_pick_list_info
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

//    override fun canBackPressed(): Boolean {
//        return if (viewSwitcher.displayedChild != 0) {
//            viewSwitcher.removeViewAt(1)
//            toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_storage_unit_info)
//            false
//        } else
//            true
//    }

    class PickListMaterialAdapter : BaseQuickAdapter<PickListMaterialInfo, BaseViewHolder>(R.layout.item_pick_list_material_info) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: PickListMaterialInfo) {
            val dataBind = DataBindingUtil.bind<ItemPickListMaterialInfoBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind.info = item
        }
    }
}