package com.zotye.wms.data.api.model

import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/01/24
 */
class StorageUnitPackageInfo {
    /**
     * 包装标签号
     */
    var code: String? = null
    /**
     * 物料号
     */
    var materialNum: String? = null
    /**
     * 料码
     */
    var materialWRKST: String? = null
    /**
     * 物料描述
     */
    var materialDescription: String? = null
    /**
     * 发货数量
     */
    var deliveryNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)
    /**
     * 发货单位
     */
    var deliveryUnit: String? = null
    /**
     * 供应商code
     */
    var supplierCode: String? = null
    /**
     * 供应商简称
     */
    var supplierInfo: String? = null
    /**
     * 交货单号
     */
    var deliveryNoteCode: String? = null

    /**
     * 收货库存地点
     */
    var slCode: String? = null

    /**
     * 工厂代码
     */
    var factoryCode: String? = null
    /**
     * 包装状态
     */
    var state: String? = null
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
     * 库位名称
     */
    var storageAreaInfoName: String? = null
}