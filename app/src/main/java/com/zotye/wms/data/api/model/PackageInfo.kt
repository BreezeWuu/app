package com.zotye.wms.data.api.model

import android.text.TextUtils
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/01/17
 */
class PackageInfo {
    constructor(code: String?) {
        this.code = code
    }

    /**
     * 包装标签号
     */
    var code: String? = null
    /**
     * 物料号
     */
    var materialId: String? = null
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
    var deliveryNum: BigDecimal? = BigDecimal.valueOf(0)
        set(value) {
            field = value ?: BigDecimal.valueOf(0)
        }
        get() {
            return if (field == null) BigDecimal.valueOf(0) else field
        }
    /**
     * 发货数量
     */
    var receiveNum: BigDecimal? = BigDecimal.valueOf(0)
        set(value) {
            field = value ?: BigDecimal.valueOf(0)
        }
        get() {
            return if (field == null) BigDecimal.valueOf(0) else field
        }
    /**
     * 发货单位
     */
    var deliveryUnit: String? = null
    /**
     * 供应商简称
     */
    var supplierInfo: String? = null
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
}