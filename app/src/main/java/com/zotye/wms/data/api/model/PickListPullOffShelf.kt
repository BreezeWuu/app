package com.zotye.wms.data.api.model

import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/01/26
 */
class PickListPullOffShelf {
    /**
     * 下架明细ID
     */
    val id: String? = null
    /**
     * 拣配单号
     */
    var pickListCode: String? = null
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
     * 数量
     */
    var totalNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var lockNumber: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)
    /**
     * 单位
     */
    var unit: String? = null
    /**
     * 供应商代码
     */
    var supplierCode: String? = null
    /**
     * 供应商名
     */
    var supplierName: String? = null
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
    /**
     * 库位code
     */
    var storageUnitInfoCode: String? = null

    /**
     * 是否需要下架盘点
     */
    var checkFlag: Boolean = false
    /**
     * 是否需要下架确认
     */
    var pullOffConfirm: Boolean = false
    /**
     * 是否已经扫描过包装
     */
    var checkCount: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var actulOffShellNumber: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)
    /**
     * 是否已经扫描过包装
     */
    var isAddedPackage: Boolean = false

    val spDetailId: String? = null

    fun isChecked(): Boolean {
        return checkFlag && isAddedPackage && (totalNum == lockNumber)
    }
}