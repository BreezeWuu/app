package com.zotye.wms.ui.common

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.AppDataManager
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.api.model.Resource
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemHomeButtonBinding
import com.zotye.wms.ui.goods.receive.GroupReceiveFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.appcompat.v7.titleResource
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/16
 */
class MainFragment : BaseFragment() {
    @Inject lateinit var appDataManager: AppDataManager
    @Inject lateinit var appExecutors: AppExecutors

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.titleResource = R.string.app_name
        toolbar_base.inflateMenu(R.menu.main_menu)
        appExecutors.diskIO().execute {
            appDataManager.getCurrentUser()?.let {
                buttonRecyclerView.layoutManager = GridLayoutManager(context, 4)
                buttonRecyclerView.adapter = HomeButtonAdapter()
                (buttonRecyclerView.adapter as HomeButtonAdapter).setNewData(it.resources)
                (buttonRecyclerView.adapter as HomeButtonAdapter).onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
                    if (adapter is HomeButtonAdapter) {
                        val resource = adapter.getItem(position)
                        fragmentManager!!.beginTransaction().add(R.id.main_content, GroupReceiveFragment()).addToBackStack(null).commit()
                    }
                }
            }
        }
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