package com.zotye.wms.data.api.model.picking

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/03/05
 */
class PdaPickReceiptDetailDto : MultiItemEntity {
    var pickReceiptDetailId: String? = null

    var materialNum: String? = null

    var materialName: String? = null

    var wrkst: String? = null

    var deliveryCount: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var reciprocalNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var sequence: Int = 0

    var lackNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var unqualifyNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var isEditEnable: Boolean = false

    var parent: PickReceiptDto? = null

    override fun getItemType(): Int {
        return 1
    }
}