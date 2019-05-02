package com.zotye.wms.ui.goods

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
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
import com.zotye.wms.data.api.model.UnpackingDto
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemInfoUppackageBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import com.zotye.wms.ui.storageunit.StorageUnitModifyContract
import kotlinx.android.synthetic.main.dialog_under_shelf_package.*
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.layout_code_scanner.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
import java.lang.Exception
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/05/02
 */
class UnPackageFragment : BaseFragment(), ScannerDelegate, StorageUnitModifyContract.StorageUnitModifyView {

    private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
    @Inject
    lateinit var presenter: StorageUnitModifyContract.StorageUnitModifyPresenter

    companion object {
        fun newInstance(title: String): UnPackageFragment {
            val fragment = UnPackageFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_storage_unit_modify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_un_package)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        codeScanner.setOnClickListener {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        codeInput.setOnClickListener {
            val codeInputView = LayoutInflater.from(context!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            AlertDialog.Builder(context!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                presenter.getStorageUnitInfoByCode(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
    }

    override fun succeed(result: String) {
        presenter.getStorageUnitInfoByCode(result)
    }

    override fun getBarCodeInfo(barcodeInfo: BarcodeInfo?) {
        barcodeInfo?.let { info ->
            BarCodeType.fromCodeType(info.barCodeType)?.apply {
                getStorageUnitPackage(info)
            }
        }
    }

    private fun getStorageUnitPackage(info: BarcodeInfo) {
        val packageInfo: PackageInfo = Gson().fromJson<PackageInfo>(info.barCodeInfo, PackageInfo::class.java)
        val infoView = LayoutInflater.from(context).inflate(R.layout.item_info_uppackage, null)
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.package_info).setView(infoView).create()
        infoView.find<Button>(R.id.unPackageButton).setOnClickListener {
            dialog.dismiss()
            val codeInputView = LayoutInflater.from(it.context).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.un_package_number)
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            AlertDialog.Builder(it.context).setTitle(R.string.action_input_un_package_number).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                val unpackingDto = UnpackingDto().apply {
                    sourceCode = packageInfo.code
                    num = try {
                        editText.text.toString().toBigDecimal()
                    } catch (e: Exception) {
                        BigDecimal.ZERO
                    }
                }
                if (unpackingDto.num == BigDecimal.ZERO || unpackingDto.num!! > packageInfo.deliveryNum) {
                    showMessage(R.string.error_un_packing_number)
                } else {
                    presenter.unPacking(unpackingDto)
                }
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        infoView.find<Button>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }
        val dataBind = DataBindingUtil.bind<ItemInfoUppackageBinding>(infoView, fragmentDataBindingComponent)
        dataBind?.info = packageInfo
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun getNewStorageUnitPosition(info: BarcodeInfo, qrCode: String) {
    }

    override fun storageUnitModifySucceed(message: String) {
    }

    override fun unPackingSucceed(message: String) {
        AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(message).setNegativeButton(R.string.ok,null).show()
    }
}
