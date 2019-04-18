package com.zotye.wms.data.api.model

import java.math.BigDecimal

/**
 * Created by hechuangju on 2019/04/12
 */
class MaterialReplenishmentRequest {
    var materialId: String? = null

    var materialName: String? = null

    var packageNum: BigDecimal? = null

    var requireNum: BigDecimal? = null

    var materialNum: String? = null//物料号

    var slId: String? = null//库存地点ID

    var sltId: String? = null//抬头库存地点ID

    var supplierId: String? = null//供应商id
}
