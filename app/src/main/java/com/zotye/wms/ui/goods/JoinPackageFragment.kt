package com.zotye.wms.ui.goods

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.zotye.wms.R
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.model.BarCodeType
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.PackageInfo
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemGoodsPackageBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_goods_receive_group.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textResource
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/05/03
 */
class JoinPackageFragment : BaseFragment(), ScannerDelegate, GroupReceiveContract.GroupReceiveView {

    private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
    @Inject
    lateinit var presenter: GroupReceiveContract.GroupReceivePresenter
    @Inject
    lateinit var dataManager: DataManager

    companion object {
        fun newInstance(title: String): JoinPackageFragment {
            val fragment = JoinPackageFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_join_package, container, false)
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
        packageScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@JoinPackageFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        packageInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                presenter.getPackageInfo(true, editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        packageRecyclerView.layoutManager = LinearLayoutManager(context)
        val goodsPackageAdapter = GroupReceiveFragment.GoodsPackageAdapter()
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.package_list_empty)
        goodsPackageAdapter.emptyView = emptyView
        goodsPackageAdapter.bindToRecyclerView(packageRecyclerView)
        goodsPackageAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.deleteButton -> {
                    context?.let {
                        AlertDialog.Builder(it).setMessage(getString(R.string.delete_package_item_info, goodsPackageAdapter.getItem(position)?.code)).setNegativeButton(R.string.ok) { _, _ ->
                            goodsPackageAdapter.remove(position)
                        }.setPositiveButton(R.string.cancel, null).show()
                    }
                }
                R.id.editButton -> {
                    val item = goodsPackageAdapter.getItem(position)
                    item?.let {
                        val editText = goodsPackageAdapter.getViewByPosition(position, R.id.receiveNumberText) as TextInputEditText
                        val batchNumberEditText = goodsPackageAdapter.getViewByPosition(position, R.id.batchNumber) as TextInputEditText
                        if (item.isEditEnable) {
                            if (item.isBatchMaterialEditable()) {
                                if (batchNumberEditText.text.isNullOrBlank()) {
                                    batchNumberEditText.error = getString(R.string.batch_number_empty_error)
                                    return@let
                                } else
                                    item.batchNum = batchNumberEditText.text.toString()
                            }
                            val numberText = editText.text.toString()
                            item.receiveNum = BigDecimal(if (TextUtils.isEmpty(numberText)) "1" else numberText)
                        } else {
                            editText.requestFocus()
                        }
                        item.isEditEnable = !item.isEditEnable
                        goodsPackageAdapter.notifyItemChanged(position)
                        if (item.isEditEnable)
                            showKeyboard(editText as EditText)
                        else
                            hideKeyboard(editText as EditText)
                    }
                }
            }
        }
    }

    override fun getBarCodeInfo(barcodeInfo: BarcodeInfo?) {
        barcodeInfo?.let { info ->
            val barcodeType = BarCodeType.fromCodeType(info.barCodeType)
            barcodeType?.let {
                when (it) {
                    BarCodeType.Package -> {
                        getPackageInfo(Gson().fromJson<PackageInfo>(info.barCodeInfo, PackageInfo::class.java))
                    }
                }
            }
        }
    }

    private fun getPackageInfo(packageInfo: PackageInfo) {
        packageInfo.isEditEnable = true
        val infoView = LayoutInflater.from(context).inflate(R.layout.item_goods_package, null)
        val dataBind = DataBindingUtil.bind<ItemGoodsPackageBinding>(infoView, fragmentDataBindingComponent)
        val receiveNumberEditText = infoView.findViewById<TextInputEditText>(R.id.receiveNumberText)
        val batchNumberEditText = infoView.findViewById<TextInputEditText>(R.id.batchNumber)
        infoView.findViewById<View>(R.id.dialogActionLayout).visibility = View.VISIBLE
        infoView.findViewById<View>(R.id.noReceiveNumberLayout).visibility = View.GONE
        infoView.findViewById<View>(R.id.badNumberLayout).visibility = View.GONE
        packageInfo.receiveNum = packageInfo.deliveryNum
        dataBind?.info = packageInfo
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.package_info).setView(infoView).create()
        infoView.findViewById<AppCompatButton>(R.id.okButton).textResource = R.string.add
        infoView.findViewById<View>(R.id.okButton).onClick {
            if (packageInfo.isBatchMaterialEditable()) {
                if (batchNumberEditText.text.isNullOrBlank()) {
                    batchNumberEditText.error = getString(R.string.batch_number_empty_error)
                    return@onClick
                }
            }
            val numberText = receiveNumberEditText.text.toString()
            if (TextUtils.isEmpty(numberText)) {
                receiveNumberEditText.error = getString(R.string.error_receive_number)
                return@onClick
            }
            val receiveNumber = BigDecimal(numberText)
            if (receiveNumber.compareTo(BigDecimal.ZERO) < 1) {
                receiveNumberEditText.error = getString(R.string.error_receive_number)
                return@onClick
            }
            packageInfo.receiveNum = receiveNumber
            packageInfo.batchNum = batchNumberEditText.text.toString()
            val adapter = (packageRecyclerView.adapter as GroupReceiveFragment.GoodsPackageAdapter)
            if (adapter.data.isNotEmpty()) {
                val lastPackage = adapter.getItem(adapter.itemCount - 1)!!
                if (lastPackage.materialNum != packageInfo.materialNum) {
                    showSnackBar(infoView, getString(R.string.not_match_material_id))
                    return@onClick
                }
                if (lastPackage.supplierCode != packageInfo.supplierCode) {
                    showSnackBar(infoView, getString(R.string.not_match_supplier_info))
                    return@onClick
                }
                if (lastPackage.slCode != packageInfo.slCode) {
                    showSnackBar(infoView, getString(R.string.not_match_sl_code))
                    return@onClick
                }
                if (lastPackage.batchNum != packageInfo.batchNum) {
                    batchNumberEditText.error = getString(R.string.not_match_batch_num)
                    return@onClick
                }
                if (lastPackage.deliveryNoteCode != packageInfo.deliveryNoteCode) {
                    batchNumberEditText.error = getString(R.string.not_match_delivery_note_code)
                    return@onClick
                }
            }
            packageInfo.isEditEnable = false
            adapter.addData(packageInfo)
            hideKeyboard(receiveNumberEditText)
            dialog.dismiss()
        }
        infoView.findViewById<View>(R.id.cancleButton).onClick {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    override fun succeed(result: String) {
        if ((packageRecyclerView.adapter as GoodsPackageAdapter).data.contains(PackageInfo(result))) {
            showMessage(R.string.repeat_package_code_warn)
        } else
            presenter.getPackageInfo(true, result)
    }

    override fun submitReceiveInfoSucceed(message: String) {
        AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(message).setPositiveButton(R.string.ok, null).show()
        (packageRecyclerView.adapter as GoodsPackageAdapter).setNewData(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    class GoodsPackageAdapter : BaseQuickAdapter<PackageInfo, BaseViewHolder>(R.layout.item_goods_package) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: PackageInfo) {
            val dataBind = DataBindingUtil.bind<ItemGoodsPackageBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind?.info = item
            helper.itemView.findViewById<View>(R.id.actionLayout).visibility = View.VISIBLE
            helper.addOnClickListener(R.id.deleteButton)
            helper.addOnClickListener(R.id.editButton)
        }
    }

}