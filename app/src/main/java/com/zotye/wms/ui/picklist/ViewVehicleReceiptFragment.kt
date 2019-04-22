package com.zotye.wms.ui.picklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.zotye.wms.R
import com.zotye.wms.data.api.model.LineBean
import com.zotye.wms.data.api.model.StorageLocationDto
import com.zotye.wms.data.api.model.TransportGroup
import com.zotye.wms.data.api.model.VehicleReceiptFilterInfo
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
}