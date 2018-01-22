package com.zotye.wms.data.api.model

/**
 * Created by hechuangju on 2018/01/18
 */
class PalletInfo(var code: String = "") {
    /**
     * 库位代码
     */
    var storagePositionCode: String? = null
    /**
     * 库位名称
     */
    var storagePositionName: String? = null
    /**
     * 库存区域代码
     */
    var storageAreaInfoCode: String? = null
    /**
     * 库存区域名称
     */
    var storageAreaInfoName: String? = null
}