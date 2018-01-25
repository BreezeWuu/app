package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.zotye.wms.R
import com.zotye.wms.data.api.model.*
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemPickListMaterialInfoBinding
import com.zotye.wms.databinding.LayoutPickListInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_pick_list.*
import kotlinx.android.synthetic.main.layout_code_scanner.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.find
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
        pickListInfoView.find<Button>(R.id.addPackageButton).onClick {
            viewFlipper.showNext()
            updateTitle()
        }
        viewFlipper.addView(pickListInfoView)
        val pickListInfoAddPackageView = LayoutInflater.from(context).inflate(R.layout.layout_pick_list_add_package, viewFlipper, false)
        viewFlipper.addView(pickListInfoAddPackageView)
        pickListInfoAddPackageView.find<Button>(R.id.packageCodeInput).onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                presenter.getStorageUnitInfoByCode(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        pickListInfoAddPackageView.find<Button>(R.id.packageCodeScanner).onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(object : ScannerDelegate {
                override fun succeed(result: String) {
                    presenter.getStorageUnitInfoByCode(result)
                }
            })
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        val pickListInfoPackageListView = LayoutInflater.from(context).inflate(R.layout.layout_pick_list_package_list, viewFlipper, false)
        viewFlipper.addView(pickListInfoPackageListView)
        viewFlipper.showNext()
        updateTitle()
    }

    override fun getBarCodeInfo(barCodeInfo: BarcodeInfo?) {
        barCodeInfo?.let { info ->
            val barcodeType = BarCodeType.fromCodeType(info.barCodeType)
            barcodeType?.let {
                when (it) {
                    BarCodeType.Package -> {
                        getStorageUnitPackage(Gson().fromJson<PackageInfo>(info.barCodeInfo, PackageInfo::class.java))
                    }
                    BarCodeType.Pallet -> {
                        getStorageUnitPallet(Gson().fromJson<PalletInfo>(info.barCodeInfo, PalletInfo::class.java))
                    }
                }
            }
        }
    }

    private fun getStorageUnitPackage(packageInfo: PackageInfo) {
        viewFlipper.showNext()
        updateTitle()
    }

    private fun getStorageUnitPallet(palletInfo: PalletInfo) {
        viewFlipper.showNext()
        updateTitle()
    }


    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    private fun updateTitle() {
        when (viewFlipper.displayedChild) {
            0 -> {
                toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_storage_unit_info)
            }
            1 -> {
                toolbar_base.titleResource = R.string.title_pick_list_info
            }
            2 -> {
                toolbar_base.titleResource = R.string.title_pick_list_add_package
            }
            3 -> {
                toolbar_base.titleResource = R.string.package_list
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

    class PickListMaterialAdapter : BaseQuickAdapter<PickListMaterialInfo, BaseViewHolder>(R.layout.item_pick_list_material_info) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: PickListMaterialInfo) {
            val dataBind = DataBindingUtil.bind<ItemPickListMaterialInfoBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind.info = item
        }
    }
}