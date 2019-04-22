package com.zotye.wms.ui.picklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zotye.wms.R
import com.zotye.wms.ui.common.BaseFragment

/**
 * Created by hechuangju on 2019/04/22
 */
class ViewVehicleReceiptFragment :BaseFragment(){
    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_viewvehiclereceipt,container,false)
    }
}