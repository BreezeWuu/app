package com.zotye.wms.ui.goods.receive

import android.app.ProgressDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.PackageInfo
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemGoodsPackageBinding
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.CodeScannerFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_goods_receive_group.*
import kotlinx.android.synthetic.main.layout_item_goods_package.view.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/17
 */
class GroupReceiveFragment : BaseFragment(), ScannerDelegate, GroupReceiveContract.GroupReceiveView {

    private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
    @Inject lateinit var presenter: GroupReceiveContract.GroupReceivePresenter
    private var progressDialog: ProgressDialog? = null

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_goods_receive_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.titleResource = R.string.goods_receive_group
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        packageScanner.onClick {
            val fragment = CodeScannerFragment()
            fragment.setScannerDelegate(this@GroupReceiveFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        packageInput.onClick {
            val view = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = view.findViewById<EditText>(R.id.packageCode)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_package_code).setView(view).setNegativeButton(R.string.ok) { _, _ ->
                presenter.getPackageInfo(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        packageRecyclerView.layoutManager = LinearLayoutManager(context)
        val goodsPackageAdapter = GoodsPackageAdapter()
        goodsPackageAdapter.bindToRecyclerView(packageRecyclerView)
        goodsPackageAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.deleteButton -> {
                    context?.let {
                        AlertDialog.Builder(it).setMessage(getString(R.string.delete_jpd_item_info, goodsPackageAdapter.getItem(position))).setNegativeButton(R.string.ok) { _, _ ->
                            goodsPackageAdapter.remove(position)
                            if (goodsPackageAdapter.itemCount == 0)
                                packageEmptyTextView.bringToFront()
                        }.setPositiveButton(R.string.cancel, null).show()
                    }
                }
                R.id.editButton -> {
                    val item = goodsPackageAdapter.getItem(position)
                    item?.let {
                        val editText = goodsPackageAdapter.getViewByPosition(position, R.id.receiveNumberText)
                        if (editText is TextInputEditText) {
                            if (item.isEditEnable) {
                                val numberText = editText.text.toString()
                                item.receiveNum = BigDecimal(if (TextUtils.isEmpty(numberText)) "0" else numberText)
                            } else {
                                editText.requestFocus()
                            }
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

    override fun showProgressDialog(resId: Int) {
        progressDialog = ProgressDialog.show(context!!, null, getString(resId), true, true) {
            presenter.cancelQueryPackageInfo()
        }
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    override fun getPackageInfo(packageInfo: PackageInfo?) {
        packageInfo?.let {
            val infoView = LayoutInflater.from(context).inflate(R.layout.item_goods_package, null)
            infoView.actionLayout.visibility = View.GONE
            val dataBind = DataBindingUtil.bind<ItemGoodsPackageBinding>(infoView, fragmentDataBindingComponent)
            val editText = infoView.findViewById<TextInputEditText>(R.id.receiveNumberText)
            it.isEditEnable = true
            dataBind.info = packageInfo
            AlertDialog.Builder(context!!).setTitle(R.string.package_info).setView(infoView).setNegativeButton(R.string.ok) { _, _ ->
                val numberText = editText.text.toString()
                packageInfo.receiveNum = BigDecimal(if (TextUtils.isEmpty(numberText)) "0" else numberText)
                packageInfo.isEditEnable = false
                val adapter = packageRecyclerView.adapter as GoodsPackageAdapter
                adapter.addData(packageInfo)
                if (adapter.itemCount != 0)
                    packageRecyclerView.bringToFront()
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel) { _, _ ->
                hideKeyboard(editText)
            }.show()
        }
    }

    override fun succeed(result: String) {
        if ((packageRecyclerView.adapter as GoodsPackageAdapter).data.contains(PackageInfo(result))) {
            showMessage(R.string.repeat_package_code_warn)
        } else
            presenter.getPackageInfo(result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    class GoodsPackageAdapter : BaseQuickAdapter<PackageInfo, BaseViewHolder>(R.layout.item_goods_package) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: PackageInfo) {
            val dataBind = DataBindingUtil.bind<ItemGoodsPackageBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind.info = item
            helper.addOnClickListener(R.id.deleteButton)
            helper.addOnClickListener(R.id.editButton)
        }
    }
}