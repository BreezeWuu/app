package com.zotye.wms.ui.common

import com.zotye.wms.data.api.model.BarCodeType

/**
 * Created by hechuangju on 2018/01/17
 */
interface ScannerDelegate{
    fun succeed(barCodeType: BarCodeType,result:String)
}