package com.zotye.wms.data.api.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/01/25
 */
class PickListMaterialInfo : MultiItemEntity {
    companion object {
        val TYPE_PICK_LIST_MATERIAL_INFO = 1
    }

    var materialId: String? = null
    var materialNum: String? = null
    var materialWRKST: String? = null
    var desc: String? = null
    var unit: String? = null
    var batchNum: String? = null
    var offShelfNumber: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)
    /**
     * 供应商Id
     */
    var supplierId: String? = null
    /**
     * 供应商代码
     */
    var supplierCode: String? = null
    /**
     * 供应商名
     */
    var supplierName: String? = null

    override fun getItemType(): Int {
        return 1
    }
}