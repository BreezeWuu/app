package com.zotye.wms.ui.goods

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
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
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.JoinPackageDto
import com.zotye.wms.data.api.model.PackageInfo
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemGoodsPackageBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_join_package.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textResource
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/05/16
 */
class PackageUploadShelfFragment :BaseFragment(), ScannerDelegate, GroupReceiveContract.GroupReceiveView {

    private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
    @Inject
    lateinit var presenter: GroupReceiveContract.GroupReceivePresenter
    @Inject
    lateinit var dataManager: DataManager

    companion object {
        fun newInstance(title: String): PackageUploadShelfFragment {
            val fragment = PackageUploadShelfFragment()
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
        packageScanner.setOnClickListener {
            fragmentManager!!.beginTransaction().add(R.id.main_content, BarCodeScannerFragment().apply {
                setScannerDelegate(this@PackageUploadShelfFragment)
            }).addToBackStack(null).commit()
        }
        packageInput.setOnClickListener {
            val codeInputView = LayoutInflater.from(it.context).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            AlertDialog.Builder(it.context).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        spScanner.setOnClickListener {
            fragmentManager!!.beginTransaction().add(R.id.main_content, BarCodeScannerFragment().apply {
                setScannerDelegate(object : ScannerDelegate {
                    override fun succeed(result: String) {
                        joinPackage(result)
                    }
                })
            }).addToBackStack(null).commit()
        }
        spInput.setOnClickListener {
            val codeInputView = LayoutInflater.from(it.context).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            AlertDialog.Builder(it.context).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                joinPackage(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        packageRecyclerView.layoutManager = LinearLayoutManager(context)
        val goodsPackageAdapter = GoodsPackageAdapter()
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
                            item.batchNum = batchNumberEditText.text.toString()
//                            val numberText = editText.text.toString()
//                            item.receiveNum = BigDecimal(if (TextUtils.isEmpty(numberText)) "1" else numberText)
                        } else {
                            batchNumberEditText.requestFocus()
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

    fun joinPackage(result: String) {
        val adapter = (packageRecyclerView.adapter as GoodsPackageAdapter)
        val codeList = arrayListOf<String>()
        var newBatchNumber = ""
        adapter.data.forEachIndexed { index, packageInfo ->
            codeList.add(packageInfo.code)
            if (index == 0) {
                newBatchNumber = packageInfo.batchNum ?: ""
            } else {
                if (newBatchNumber != packageInfo.batchNum) {
                    showSnackBar(getString(R.string.not_match_join_package_batch_num))
                    return
                }
            }
        }
        if (codeList.size <= 1) {
            showMessage(R.string.error_join_package)
            return
        }
        val joinPackageDto = JoinPackageDto().apply {
            spId = result
            sourceCodes = codeList
            batchNum = newBatchNumber
        }
        presenter.joinPackage(joinPackageDto)
    }

    override fun getBarCodeInfo(barcodeInfo: BarcodeInfo?) {
        barcodeInfo?.let { info ->
            getPackageInfo(Gson().fromJson<PackageInfo>(info.barCodeInfo, PackageInfo::class.java))
        }
    }

    private fun getPackageInfo(packageInfo: PackageInfo) {
        packageInfo.isEditEnable = true
        packageInfo.materialBatchFlag = "X"
        val infoView = LayoutInflater.from(context).inflate(R.layout.item_goods_package, null)
        val dataBind = DataBindingUtil.bind<ItemGoodsPackageBinding>(infoView, fragmentDataBindingComponent)
        val receiveNumberEditText = infoView.findViewById<TextInputEditText>(R.id.receiveNumberText)
        val batchNumberEditText = infoView.findViewById<TextInputEditText>(R.id.batchNumber)
        infoView.findViewById<View>(R.id.dialogActionLayout).visibility = View.VISIBLE
        infoView.findViewById<View>(R.id.noReceiveNumberLayout).visibility = View.GONE
        infoView.findViewById<View>(R.id.receiveNumberLayout).visibility = View.GONE
        infoView.findViewById<View>(R.id.badNumberLayout).visibility = View.GONE
        packageInfo.receiveNum = packageInfo.deliveryNum
        dataBind?.info = packageInfo
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.package_info).setView(infoView).create()
        infoView.findViewById<AppCompatButton>(R.id.okButton).textResource = R.string.add
        infoView.findViewById<View>(R.id.okButton).setOnClickListener {
            packageInfo.batchNum = batchNumberEditText.text.toString()
            val adapter = (packageRecyclerView.adapter as GoodsPackageAdapter)
            if (adapter.data.isNotEmpty()) {
                val lastPackage = adapter.getItem(adapter.itemCount - 1)!!
                if (lastPackage.materialNum != packageInfo.materialNum) {
                    showSnackBar(infoView, getString(R.string.not_match_join_package_material_id))
                    return@setOnClickListener
                }
                if (lastPackage.supplierCode != packageInfo.supplierCode) {
                    showSnackBar(infoView, getString(R.string.not_match_join_package_supplier_info))
                    return@setOnClickListener
                }
                if (lastPackage.slCode != packageInfo.slCode) {
                    showSnackBar(infoView, getString(R.string.not_match_join_package_sl_code))
                    return@setOnClickListener
                }
                if (lastPackage.deliveryNoteCode != packageInfo.deliveryNoteCode) {
                    batchNumberEditText.error = getString(R.string.not_match_join_package_delivery_note_code)
                    return@setOnClickListener
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
            presenter.getLogisticsPackageInfo(result)
    }

    override fun submitReceiveInfoSucceed(message: String) {
        AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(message).setPositiveButton(R.string.ok, null).show()
        (packageRecyclerView.adapter as GoodsPackageAdapter).setNewData(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    override fun joinPackageSucceed(message: String) {
        AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(message).setNegativeButton(R.string.ok,null).show()
    }

    class GoodsPackageAdapter : BaseQuickAdapter<PackageInfo, BaseViewHolder>(R.layout.item_goods_package) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: PackageInfo) {
            val dataBind = DataBindingUtil.bind<ItemGoodsPackageBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind?.info = item
            helper.itemView.findViewById<View>(R.id.actionLayout).visibility = View.VISIBLE
            helper.itemView.findViewById<View>(R.id.receiveNumberLayout).visibility = View.GONE
            helper.addOnClickListener(R.id.deleteButton)
            helper.addOnClickListener(R.id.editButton)
        }
    }

}