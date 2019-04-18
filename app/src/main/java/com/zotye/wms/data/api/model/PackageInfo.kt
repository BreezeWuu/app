package com.zotye.wms.data.api.model

import android.text.TextUtils
import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/01/18
 */
class PackageInfo(
        /*** 包装标签号*/
        var code: String = "") {

    /**
     * 修改表示0：不可以修改 1：可以修改
     */
    var modifyNumFlag: String? = null
    /**
     * 是否是托盘
     */
    var isPallet: Boolean = false
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
     * 批次管理标识("X":批次管理；"":非批次管理)
     */
    var materialBatchFlag: String? = null
    /**
     * 发货数量
     */
    var deliveryNum: BigDecimal? = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)

        }
        get() = BigDecimalUtil.getValue(field)
    /**
     * 少收数量
     */
    var noReceiveNum: BigDecimal? = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    /**
     * 质量问题
     */
    var badNum: BigDecimal? = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    /**
     * 收货数量
     */
    var receiveNum: BigDecimal? = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)
    /**
     * 发货单位
     */
    var deliveryUnit: String? = null
    /**
     * 供应商简称
     */
    var supplierInfo: String? = null
    /**
     * 供应商代码
     */
    var supplierCode: String? = null
    /**
     * 批次号
     */
    var batchNum: String? = null
    /**
     * 交货单号
     */
    var deliveryNoteCode: String? = null
    /**
     * 库存地点
     */
    var slCode: String? = null
    /**
     * 库存地点名称
     */
    val slName: String? = null
    /**
     * 工厂代码
     */
    var factoryCode: String? = null

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

    var isEditEnable = false
    override fun equals(other: Any?): Boolean {
        return if (other !is PackageInfo)
            false
        else {
            if (TextUtils.isEmpty(this.code))
                false
            else
                this.code == other.code
        }
    }

    fun isBatchMaterialEditable(): Boolean {
        return "X" == materialBatchFlag
    }

    fun isThirdPart(): Boolean {
        return "9999" == factoryCode
    }

    override fun hashCode(): Int {
        return code.hashCode()
    }
}