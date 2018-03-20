package com.zotye.wms.data.api.model.under.shelf

import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/03/06
 */
class MaterialReplenishment {
    var materialId: String? = null

    var materialName: String? = null

    var packageNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var requireNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var materialNum: String? = null//物料号

    var slId: String? = null//库存地点ID

    var sltId: String? = null//抬头库存地点ID
}