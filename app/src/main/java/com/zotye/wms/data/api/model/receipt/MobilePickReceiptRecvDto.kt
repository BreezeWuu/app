package com.zotye.wms.data.api.model.receipt

import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/03/06
 */
class MobilePickReceiptRecvDto {
    var userId: String? = null

    var recvDetail: List<MobileSinglePickReceiptRecvDto>? = null
}

class MobileSinglePickReceiptRecvDto {
    var pickReceiptId: String? = null

    var pickReceiptDetail: List<PickReceiptDetailReceiveDto>? = null
}

class PickReceiptDetailReceiveDto {
    var pickReceiptDetailId: String? = null

    var recvNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

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
}