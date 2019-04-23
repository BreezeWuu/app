package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.*
import com.zotye.wms.databinding.ItemMesPickReceiptBinding
import com.zotye.wms.databinding.ItemVehicleReceiptBinding
import com.zotye.wms.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_viewvehiclereceipt.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/04/22
 */
class ViewVehicleReceiptFragment : BaseFragment(), ViewVehicleReceiptContract.ViewVehicleReceiptView {

    @Inject
    lateinit var presenter: ViewVehicleReceiptContract.ViewVehicleReceiptPresenter

    companion object {
        fun newInstance(title: String): ViewVehicleReceiptFragment {
            val fragment = ViewVehicleReceiptFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_viewvehiclereceipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title")
                ?: getString(R.string.title_view_vehicle_receipt)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        presenter.getViewVehicleReceiptFilterInfo()
        searchButton.setOnClickListener {
            VehicleReceiptParamsDto().apply {
                start = 0
                length = Int.MAX_VALUE
                val lineBean = lineSpinner.selectedItem as LineBean
                line = if (lineSpinner.selectedItemPosition == 0) "" else lineBean.id+"("+lineBean.lineDesc+")"

                val storageLocationDto = spSpinner.selectedItem as StorageLocationDto
                slId = if (spSpinner.selectedItemPosition == 0) "" else storageLocationDto.id

                val transportGroup = peisongSpinner.selectedItem as TransportGroup
                transportGroupId = if (peisongSpinner.selectedItemPosition == 0) "" else transportGroup.id
                presenter.searchVehicleReceipt(this)
            }
        }
    }

    override fun getViewVehicleReceiptFilterInfo(vehicleReceiptFilterInfo: VehicleReceiptFilterInfo?) {
        vehicleReceiptFilterInfo?.apply {
            if (transportGroups != null && transportGroups.isNotEmpty()) {
                transportGroups.add(0, TransportGroup().apply {
                    seteDesc("全部")
                })
                peisongSpinner.adapter = ArrayAdapter<TransportGroup>(context!!, android.R.layout.simple_spinner_dropdown_item, transportGroups)
            }
            if (lines != null && lines.isNotEmpty()) {
                lines.add(0, LineBean().apply {
                    lineDesc = "全部"
                })
                lineSpinner.adapter = ArrayAdapter<LineBean>(context!!, android.R.layout.simple_spinner_dropdown_item, lines)
            }
            if (storageLocations != null && storageLocations.isNotEmpty()) {
                storageLocations.add(0, StorageLocationDto().apply {
                    name = "全部"
                })
                spSpinner.adapter = ArrayAdapter<StorageLocationDto>(context!!, android.R.layout.simple_spinner_dropdown_item, storageLocations)
            }
        }
    }

    override fun getVehicleReceipt(data: List<VehicleReceiptDto>?) {
        if (data?.isNotEmpty() == true) {
            data.apply {
                viewFlipper.displayedChild = 1
                PCRecyclerView.layoutManager = LinearLayoutManager(context)
                PCRecyclerView.adapter = PCadapter().apply {
                    setNewData(data)
                    setOnItemChildClickListener { _, _, position ->
                        presenter.getMesPickReceiptListById(get(position).id)
                    }
                }
            }
        }else{
            showMessage("未找到相应的结果！")
        }
    }

    override fun getMesPickReceiptList(data: List<MESPickReceiptDto>?) {
        if (data?.isNotEmpty() == true) {
            data.apply {
                viewFlipper.displayedChild = 2
                mesPickReceiptRecyclerView.layoutManager = LinearLayoutManager(context)
                mesPickReceiptRecyclerView.adapter = JPadapter().apply {
                    setNewData(data)
                    setOnItemChildClickListener { _, _, position ->
                        get(position).apply {
                            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.main_content, UnderShelfFragment.newInstance(getString(R.string.title_under_shelf),this.no))?.addToBackStack(null)?.commit()
                        }
                    }
                }
            }
        }else{
            showMessage("未找到相应的结果！")
        }
    }

    override fun canBackPressed(): Boolean {
        return if (viewFlipper.displayedChild != 0) {
            viewFlipper.displayedChild = viewFlipper.displayedChild - 1
            false
        } else
            super.canBackPressed()
    }

    class PCadapter : BaseQuickAdapter<VehicleReceiptDto, BaseViewHolder>(R.layout.item_vehicle_receipt) {

        override fun convert(helper: BaseViewHolder, item: VehicleReceiptDto) {
            val dataBind = DataBindingUtil.bind<ItemVehicleReceiptBinding>(helper.itemView)
            dataBind?.info = item
            helper.addOnClickListener(R.id.layout)
        }
    }


    class JPadapter : BaseQuickAdapter<MESPickReceiptDto, BaseViewHolder>(R.layout.item_mes_pick_receipt) {

        override fun convert(helper: BaseViewHolder, item: MESPickReceiptDto) {
            val dataBind = DataBindingUtil.bind<ItemMesPickReceiptBinding>(helper.itemView)
            dataBind?.info = item
            helper.addOnClickListener(R.id.underShelfButton)
        }
    }
}