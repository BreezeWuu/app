package com.zotye.wms.data.api.model

/**
 * Created by hechuangju on 2018/01/25
 */
class PickListInfo{
    /**
     * 供应商代码
     */
     var supplierCode: String? = null
    /**
     * 供应商名
     */
    var supplierName: String? = null

    var materialInfoList: List<PickListMaterialInfo>? = null
}