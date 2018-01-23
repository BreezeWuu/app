package com.zotye.wms.ui.storageunit.modify

import android.app.ProgressDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import com.zotye.wms.R
import com.zotye.wms.data.api.model.BarCodeType
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.PackageInfo
import com.zotye.wms.data.api.model.PalletInfo
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemStorageUnitInfoPackageBinding
import com.zotye.wms.databinding.ItemStorageUnitInfoPalletBinding
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.QRCodeScannerFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_storage_unit_modify.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/22
 */
class StorageUnitModifyFragment : BaseFragment(), StorageUnitModifyContract.StorageUnitModifyView, ScannerDelegate {
    private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
    @Inject
    lateinit var presenter: StorageUnitModifyContract.StorageUnitModifyPresenter
    private var progressDialog: ProgressDialog? = null

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_storage_unit_modify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.titleResource = R.string.title_storage_unit_modify
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        storageUnitScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@StorageUnitModifyFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        storageUnitInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                presenter.getStorageUnitInfoByCode(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
    }

    override fun succeed(result: String) {
        presenter.getStorageUnitInfoByCode(result)
    }

    override fun showProgressDialog(resId: Int) {
        progressDialog = ProgressDialog.show(context!!, null, getString(resId), true, false)
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    override fun getBarCodeInfo(barcodeInfo: BarcodeInfo?) {
        barcodeInfo?.let { info ->
            val barcodeType = BarCodeType.fromCodeType(info.barCodeType)
            barcodeType?.let {
                when (it) {
                    BarCodeType.Package -> {
                        getStorageUnitPackage(info)
                    }
                    BarCodeType.Pallet -> {
                        getStorageUnitPallet(info)
                    }
                }
            }
        }
    }

    private fun getStorageUnitPallet(info: BarcodeInfo) {
        val palletInfo = Gson().fromJson<PalletInfo>(info.barCodeInfo, PalletInfo::class.java)
        val infoView = LayoutInflater.from(context).inflate(R.layout.item_storage_unit_info_pallet, null)
        val dialog = AlertDialog.Builder(context!!).setView(infoView).create()
        infoView.find<Button>(R.id.storageUnitModifyButton).onClick {
            dialog.dismiss()
            val fragment = QRCodeScannerFragment()
            fragment.setScannerDelegate(object : ScannerDelegate {
                override fun succeed(result: String) {
                    presenter.authStorageUnitNewPositionByQRCode(info, result)
                }
            })
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        infoView.find<Button>(R.id.cancelButton).onClick {
            dialog.dismiss()
        }
        val dataBind = DataBindingUtil.bind<ItemStorageUnitInfoPalletBinding>(infoView, fragmentDataBindingComponent)
        dataBind.info = palletInfo
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun getStorageUnitPackage(info: BarcodeInfo) {
        val packageInfo: PackageInfo = Gson().fromJson<PackageInfo>(info.barCodeInfo, PackageInfo::class.java)
        val infoView = LayoutInflater.from(context).inflate(R.layout.item_storage_unit_info_package, null)
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.package_info).setView(infoView).create()
        infoView.find<Button>(R.id.storageUnitModifyButton).onClick {
            dialog.dismiss()
            val fragment = QRCodeScannerFragment()
            fragment.setScannerDelegate(object : ScannerDelegate {
                override fun succeed(result: String) {
                    presenter.authStorageUnitNewPositionByQRCode(info, result)
                }
            })
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        val dataBind = DataBindingUtil.bind<ItemStorageUnitInfoPackageBinding>(infoView, fragmentDataBindingComponent)
        dataBind.info = packageInfo
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun getNewStorageUnitPosition(info: BarcodeInfo, qrCode: String) {
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(R.string.storage_unit_position_avlid_modify).setNegativeButton(R.string.ok) { _, _ ->
            val barcodeType = BarCodeType.fromCodeType(info.barCodeType)
            barcodeType?.let {
                when (it) {
                    BarCodeType.Package -> {
                        val packageInfo: PackageInfo = Gson().fromJson<PackageInfo>(info.barCodeInfo, PackageInfo::class.java)
                        packageInfo.storagePositionCode?.let {
                            presenter.storageUnitModify(it, qrCode)
                        }
                    }
                    BarCodeType.Pallet -> {
                        val palletInfo = Gson().fromJson<PalletInfo>(info.barCodeInfo, PalletInfo::class.java)
                        palletInfo.storageAreaInfoCode?.let {
                            presenter.storageUnitModify(it, qrCode)
                        }
                    }
                }
            }
        }.setPositiveButton(R.string.cancel, null).create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun storageUnitModifySucceed(message: String) {
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(message).setNegativeButton(R.string.ok, null).create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }
}