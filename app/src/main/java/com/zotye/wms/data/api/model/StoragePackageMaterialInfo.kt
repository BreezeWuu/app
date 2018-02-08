package com.zotye.wms.data.api.model

import android.text.TextUtils
import com.google.gson.annotations.Expose

/**
 * Created by hechuangju on 2018/01/25
 */
class StoragePackageMaterialInfo {
    /**
     * 编号
     */
    var code: String? = null
    var spMaterialDetailId: String? = null
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
    var deliveryNum: Long = 0
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
     * 供应商批次号
     */
    var batchNum: String? = null
    /**
     * 交货单号
     */
    var deliveryNoteCode: String? = null

    /**
     * 收货库存地点
     */
    var slCode: String? = null
    /**
     * 可用数量
     */
    var availableNum: Long = 0
    /**
     * 领用数量
     */
    @Expose
    var useNum: Long = 0
    /**
     * 是否正在编辑
     */
    @Expose
    var isEditMode: Boolean = false
    /**
     * 工厂代码
     */
    var factoryCode: String? = null

    override fun equals(other: Any?): Boolean {
        return if (other !is StoragePackageMaterialInfo)
            false
        else {
            if (TextUtils.isEmpty(this.code))
                false
            else
                this.code == other.code
        }
    }

    override fun hashCode(): Int {
        return code?.hashCode() ?: 0
    }

    constructor()
    constructor(code: String) {
        this.code = code
    }
}