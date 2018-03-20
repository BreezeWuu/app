package com.zotye.wms.data.api.model.checkbad

import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/02/05
 */
class GetPickReceiptShelfDetailRequestDto {

    /**
     * 物料id
     */
    var materialId: String? = null
    /**
     * 下架库存地点id
     */
    var storageLocationId: String? = null
    /**
     * 供应商id
     */
    var supplierId: String? = null
    /**
     * 下架数量
     */
    var num: Float = 0f
        set(value) {
            field = BigDecimal(value.toDouble()).setScale(3, 4).toFloat()
        }
    constructor(materialId: String?, storageLocationId: String?, supplierId: String?, num: Float) {
        this.materialId = materialId
        this.storageLocationId = storageLocationId
        this.supplierId = supplierId
        this.num = num
    }
}
