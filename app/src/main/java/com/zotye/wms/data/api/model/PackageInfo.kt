package com.zotye.wms.data.api.model

import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/01/17
 */
class PackageInfo{
    /**
     * 包装标签号
     */
     val code: String? = null
    /**
     * 物料号
     */
     val materialId: String? = null
    /**
     * 料码
     */
     val materialWRKST: String? = null
    /**
     * 物料描述
     */
     val materialDescription: String? = null
    /**
     * 发货数量
     */
     val deliveryNum: BigDecimal? = null
    /**
     * 发货单位
     */
     val deliveryUnit: String? = null
    /**
     * 供应商简称
     */
     val supplierInfo: String? = null
    /**
     * 批次号
     */
     val batchNum: String? = null
    /**
     * 交货单号
     */
     val deliveryNoteCode: String? = null
}