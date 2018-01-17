package com.zotye.wms.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.AppDataManager
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.api.model.Resource
import com.zotye.wms.data.api.model.ResourceType
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemHomeButtonBinding
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.goods.receive.GroupReceiveFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.support.v4.onRefresh
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/16
 */
class MainFragment : BaseFragment(), MainContract.MainMvpView {

    @Inject lateinit var appDataManager: AppDataManager
    @Inject lateinit var appExecutors: AppExecutors
    @Inject lateinit var presenter: MainContract.MainMvpPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.titleResource = R.string.app_name
        toolbar_base.inflateMenu(R.menu.main_menu)
        buttonRecyclerView.layoutManager = GridLayoutManager(context, 4)
        buttonRecyclerView.adapter = HomeButtonAdapter()
        (buttonRecyclerView.adapter as HomeButtonAdapter).onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            if (adapter is HomeButtonAdapter) {
                val resource = adapter.getItem(position)
                resource?.let {
                    var fragment: Fragment? = null
                    when (ResourceType.fromCode(resource.code)) {
                        ResourceType.ORGMAINTAIN -> {

                        }
                        ResourceType.KCWLSH -> {
                            fragment = GroupReceiveFragment()
                        }
                        ResourceType.ROLEMAINTAIN -> {

                        }
                    }
                    fragment?.let {
                        fragmentManager?.beginTransaction()?.add(R.id.main_content, it)?.addToBackStack(null)?.commit()
                    }
                }
            }
        }
        appExecutors.diskIO().execute {
            appDataManager.getCurrentUser()?.let {
                getUserResources(it.resources)
            }
        }
        swipeRefreshLayout.onRefresh {
            appExecutors.diskIO().execute {
                appDataManager.getCurrentUser()?.let {
                    presenter.updateUserResources(it.userId)
                }
            }
        }
    }

    override fun refreshFinished() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun getUserResources(resources: List<Resource>) {
        (buttonRecyclerView.adapter as HomeButtonAdapter).setNewData(resources)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    class HomeButtonAdapter : BaseQuickAdapter<Resource, BaseViewHolder>(R.layout.item_home_button) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()

        override fun convert(helper: BaseViewHolder, item: Resource) {
            val itemMicroCourseBinding = DataBindingUtil.bind<ItemHomeButtonBinding>(helper.itemView, fragmentDataBindingComponent)
            itemMicroCourseBinding.buttonItem = item
            helper.addOnClickListener(R.id.action_icon)
        }
    }
}