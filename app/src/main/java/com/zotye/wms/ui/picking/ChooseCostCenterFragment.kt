package com.zotye.wms.ui.picking

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.CostCenter
import com.zotye.wms.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_choose_cost_center.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/02/08
 */
class ChooseCostCenterFragment : BaseFragment(), ChooseCostCenterContract.ChooseCostCenterView {

    interface ChooseCostCenterDelegate {
        fun selected(costCenter: CostCenter)
    }

    @Inject
    lateinit var presenter: ChooseCostCenterContract.ChooseCostCenterPresenter
    var delegate: ChooseCostCenterDelegate? = null

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_cost_center, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.titleResource = R.string.title_choose_cost_center
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        presenter.getCostCenterByUser()
        layout_error.onClick {
            presenter.getCostCenterByUser()
        }
    }

    override fun getCostCenter(storagePackageMaterialInfoList: List<CostCenter>) {
        constCenterRecyclerView.layoutManager = LinearLayoutManager(context)
        constCenterRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        val adapter = CostCenterAdapter()
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.error_no_available_cost_center)
        adapter.emptyView = emptyView
        constCenterRecyclerView.adapter = adapter
        adapter.setNewData(storagePackageMaterialInfoList)
        adapter.setOnItemChildClickListener { _, _, position ->
            delegate?.selected(adapter.getItem(position)!!)
            activity?.onBackPressed()
        }
    }

    override fun showError(message: String?) {
        val errorMessage = message + "\n" + getString(R.string.tap_screen_retry)
        super.showError(errorMessage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    class CostCenterAdapter : BaseQuickAdapter<CostCenter, BaseViewHolder>(R.layout.item_cost_center) {

        override fun convert(helper: BaseViewHolder, item: CostCenter) {
            helper.setText(R.id.nameTextView, item.name)
            helper.addOnClickListener(R.id.nameTextView)
        }
    }
}