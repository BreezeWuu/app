package com.zotye.wms.ui.storageunit

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
import com.google.gson.Gson
import com.zotye.wms.R
import com.zotye.wms.data.api.model.*
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemStorageUnitPackageInfoBinding
import com.zotye.wms.databinding.ItemStorageUnitPackageMaterialInfoBinding
import com.zotye.wms.databinding.ItemStorageUnitPalletInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_storage_unit_info.*
import kotlinx.android.synthetic.main.layout_code_scanner.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/24
 */
class StorageUnitInfoFragment : BaseFragment(), StorageUnitInfoContract.StorageUnitInfoView, ScannerDelegate {

    companion object {
        fun newInstance(title: String): StorageUnitInfoFragment {
            val fragment = StorageUnitInfoFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
    @Inject
    lateinit var presenter: StorageUnitInfoContract.StorageUnitInfoPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_storage_unit_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_storage_unit_info)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        codeScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@StorageUnitInfoFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        codeInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
    }

    override fun succeed(result: String) {
        presenter.getStorageUnitDetailInfoByCode(result)
    }

    override fun getStorageUnitInfo(barcodeInfo: BarcodeInfo) {
        val barcodeType = BarCodeType.fromCodeType(barcodeInfo.barCodeType)
        barcodeType?.let {
            when (it) {
                BarCodeType.Package -> {
                    getStorageUnitPackageInfo(Gson().fromJson<StorageUnitPackageInfo>(barcodeInfo.barCodeInfo, StorageUnitPackageInfo::class.java))
                }
                BarCodeType.Pallet -> {
                    getStorageUnitPalletInfo(Gson().fromJson<StorageUnitPalletInfo>(barcodeInfo.barCodeInfo, StorageUnitPalletInfo::class.java))
                }
            }
        }
    }

    private fun getStorageUnitPalletInfo(storageUnitPalletInfo: StorageUnitPalletInfo?) {
        storageUnitPalletInfo?.let {
            val infoView = LayoutInflater.from(context).inflate(R.layout.item_storage_unit_pallet_info, viewSwitcher, false)
            val dataBind = DataBindingUtil.bind<ItemStorageUnitPalletInfoBinding>(infoView, fragmentDataBindingComponent)
            dataBind.info = it
            val adapter = PackageMaterialAdapter()
            val palletRecyclerView = infoView.findViewById<RecyclerView>(R.id.palletRecyclerView)
            palletRecyclerView.layoutManager = LinearLayoutManager(context)
            palletRecyclerView.adapter = adapter
            adapter.setNewData(it.packageMaterialInfoList)
            viewSwitcher.addView(infoView)
            viewSwitcher.showNext()
            toolbar_base.titleResource = R.string.pallet_info
        }
    }

    private fun getStorageUnitPackageInfo(storageUnitPackageInfo: StorageUnitPackageInfo?) {
        storageUnitPackageInfo.let {
            val infoView = LayoutInflater.from(context).inflate(R.layout.item_storage_unit_package_info, viewSwitcher, false)
            val dataBind = DataBindingUtil.bind<ItemStorageUnitPackageInfoBinding>(infoView, fragmentDataBindingComponent)
            dataBind.info = it
            viewSwitcher.addView(infoView)
            viewSwitcher.showNext()
            toolbar_base.titleResource = R.string.package_info
        }
    }

    override fun canBackPressed(): Boolean {
        return if (viewSwitcher.displayedChild != 0) {
            viewSwitcher.removeViewAt(1)
            toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_storage_unit_info)
            false
        } else
            true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    class PackageMaterialAdapter : BaseQuickAdapter<StoragePackageMaterialInfo, BaseViewHolder>(R.layout.item_storage_unit_package_material_info) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: StoragePackageMaterialInfo) {
            val dataBind = DataBindingUtil.bind<ItemStorageUnitPackageMaterialInfoBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind.info = item
        }
    }
}