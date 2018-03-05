package com.zotye.wms.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.model.Resource
import com.zotye.wms.data.api.model.ResourceType
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemHomeButtonBinding
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.MainActivity
import com.zotye.wms.ui.goods.GroupReceiveFragment
import com.zotye.wms.ui.goods.ReceiveConfirmFragment
import com.zotye.wms.ui.goods.StrictReceiveFragment
import com.zotye.wms.ui.picking.PickingFragment
import com.zotye.wms.ui.picklist.CheckBadProductFragment
import com.zotye.wms.ui.picklist.LoadingCreateFragment
import com.zotye.wms.ui.picklist.UnderShelfFragment
import com.zotye.wms.ui.storageunit.StorageUnitInfoFragment
import com.zotye.wms.ui.storageunit.StorageUnitModifyFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.appcompat.v7.coroutines.onMenuItemClick
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.support.v4.onRefresh
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/16
 */
class MainFragment : BaseFragment(), MainContract.MainMvpView {

    @Inject
    lateinit var appDataManager: DataManager
    @Inject
    lateinit var appExecutors: AppExecutors
    @Inject
    lateinit var presenter: MainContract.MainMvpPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.titleResource = R.string.app_name
        toolbar_base.inflateMenu(R.menu.main_menu)
        toolbar_base.onMenuItemClick { item ->
            when (item?.itemId) {
                R.id.action_logout -> {
                    AlertDialog.Builder(getContext()).setMessage(R.string.info_logout).setNegativeButton(R.string.ok) { _, _ ->
                        appDataManager.setCurrentUserId(null)
                        appDataManager.setDefaultFactoryCode(null)
                        if (activity is MainActivity) {
                            (activity as MainActivity).handlerIntent(Intent(), null)
                        }
                    }.setPositiveButton(R.string.cancel, null).show()
                }
            }
        }
        buttonRecyclerView.layoutManager = GridLayoutManager(context, 3)
        buttonRecyclerView.adapter = HomeButtonAdapter()
        (buttonRecyclerView.adapter as HomeButtonAdapter).onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            if (adapter is HomeButtonAdapter) {
                val resource = adapter.getItem(position)
                resource?.let {
                    var fragment: Fragment? = null
                    when (ResourceType.fromCode(resource.code)) {
                        ResourceType.PALLETRECV -> {
                            fragment = GroupReceiveFragment.newInstance(true, it.name)
                        }
                        ResourceType.PACKAGERECV -> {
                            fragment = GroupReceiveFragment.newInstance(false, it.name)
                        }
                        ResourceType.STQUERY -> {
                            fragment = StorageUnitInfoFragment.newInstance(it.name)
                        }
                        ResourceType.ThreePLDELIVERY -> {
                            fragment = LoadingCreateFragment.newInstance(it.name)
                        }
                        ResourceType.ThreePLPRCREATE -> {
                            fragment = CheckBadProductFragment.newInstance(it.name)
                        }
                        ResourceType.ThreePLSOLDOUT -> {
                            fragment = UnderShelfFragment.newInstance(it.name)
                        }
                        ResourceType.PRODUCE_ANDROID -> {
                            fragment = PickingFragment.newInstance(it.name)
                        }
                        ResourceType.ThreePLRECVCONFIRM -> {
                            fragment = ReceiveConfirmFragment.newInstance(it.name)
                        }
                        ResourceType.ThreePLADJUST -> {
                            fragment = StorageUnitModifyFragment()
                        }
                        ResourceType.STRICT_RECV -> {
                            fragment = StrictReceiveFragment.newInstance(it.name)
                        }
                        else -> {
                            return@OnItemChildClickListener
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
                appExecutors.mainThread().execute {
                    getUserResources(it.resources)
                }
            }
        }
        swipeRefreshLayout.onRefresh {
            presenter.updateUserResources()
        }
    }

    override fun refreshFinished() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun getUserResources(resources: List<Resource>) {
        (buttonRecyclerView?.adapter as HomeButtonAdapter).setNewData(resources)
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    class HomeButtonAdapter : BaseQuickAdapter<Resource, BaseViewHolder>(R.layout.item_home_button) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()

        override fun convert(helper: BaseViewHolder, item: Resource) {
            val itemMicroCourseBinding = DataBindingUtil.bind<ItemHomeButtonBinding>(helper.itemView, fragmentDataBindingComponent)
            itemMicroCourseBinding?.buttonItem = item
            helper.addOnClickListener(R.id.buttonLayout)
        }
    }
}