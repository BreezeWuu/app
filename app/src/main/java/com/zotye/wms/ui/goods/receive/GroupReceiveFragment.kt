package com.zotye.wms.ui.goods.receive

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.PackageInfo
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.CodeScannerFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_goods_receive_group.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/17
 */
class GroupReceiveFragment : BaseFragment(), ScannerDelegate, GroupReceiveContract.GroupReceiveView {

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
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_pda_code).setView(R.layout.dialog_pda_code_input).setNegativeButton(R.string.ok) { _, _ ->
            }.setPositiveButton(R.string.cancel, null).show()
        }
        packageRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = GoodsPackageAdapter()
        packageRecyclerView.adapter = adapter
        adapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, _, position ->
            context?.let {
                AlertDialog.Builder(it).setMessage(getString(R.string.delete_jpd_item_info, adapter.getItem(position))).setNegativeButton(R.string.ok) { _, _ ->
                    adapter.remove(position)
                    if (adapter.itemCount == 0)
                        packageEmptyTextView.bringToFront()
                }.setPositiveButton(R.string.cancel, null).show()
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
            val adapter = packageRecyclerView.adapter as GoodsPackageAdapter
            adapter.addData(packageInfo)
            if (adapter.itemCount != 0)
                packageRecyclerView.bringToFront()
        }
    }

    override fun succeed(result: String) {
        presenter.getPackageInfo(result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    class GoodsPackageAdapter : BaseQuickAdapter<PackageInfo, BaseViewHolder>(R.layout.item_goods_package) {

        override fun convert(helper: BaseViewHolder, item: PackageInfo) {
            helper.setText(R.id.jpdCodeTextView, helper.itemView.resources.getString(R.string.jpd_code_format, item.code))
            helper.addOnClickListener(R.id.jdpDeleteButton)
        }
    }
}