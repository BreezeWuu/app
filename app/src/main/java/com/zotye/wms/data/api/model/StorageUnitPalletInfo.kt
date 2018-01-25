package com.zotye.wms.data.api.model

/**
 * Created by hechuangju on 2018/01/25
 */
class StorageUnitPalletInfo {
    /**
     * 托盘号
     */
    val code: String? = null
    /**
     * 交货单号
     */
    val deliveryNoteCode: String? = null
    /**
     * 库位代码
     */
    val storagePositionCode: String? = null
    /**
     * 库位名称
     */
    val storagePositionName: String? = null
    /**
     * 库存区域代码
     */
    val storageAreaInfoCode: String? = null
    /**
     * 库位名称
     */
    val storageAreaInfoName: String? = null

    /**
     * 托盘物料信息
     */
    val packageMaterialInfoList: List<StoragePackageMaterialInfo>? = null
}