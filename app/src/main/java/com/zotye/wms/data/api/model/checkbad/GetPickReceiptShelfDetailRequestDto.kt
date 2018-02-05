package com.zotye.wms.data.api.model.checkbad

/**
 * Created by hechuangju on 2018/02/05
 */
data class GetPickReceiptShelfDetailRequestDto(
        /**
         * 物料id
         */
        val materialId: String?,
        /**
         * 下架库存地点id
         */
        val storageLocationId: String?,
        /**
         * 供应商id
         */
        val supplierId: String?,
        /**
         * 下架数量
         */
        val num: Long
)