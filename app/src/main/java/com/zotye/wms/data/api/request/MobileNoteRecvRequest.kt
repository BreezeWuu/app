package com.zotye.wms.data.api.request

import com.zotye.wms.data.api.model.receipt.ReceiveDetailDto
import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/03/08
 */
class MobileNoteRecvRequest {
    var noteCode: String? = null//交货单号

    var userId: String? = null//收货人

    var postTime: String? = null// 过账日期

    var slCode: String? = null//最终收货库存地点编码

    var noteDetail: ArrayList<ReceiveConfirmRequest> = ArrayList()//收货明细
}

class ReceiveConfirmRequest {

    companion object {
        fun newInstance(detailDto: ReceiveDetailDto): ReceiveConfirmRequest {
            val request = ReceiveConfirmRequest()
            request.id = detailDto.id
            request.isBom = detailDto.isBom
            request.receiveNum = detailDto.receiveNum
            request.batchNum = detailDto.batchNum
            request.lackNum = detailDto.lackNum
            request.unqualifyNum = detailDto.unqualifyNum
            return request
        }
    }

    var id: String? = null

    var isBom: Boolean? = null

    var batchNum: String? = null

    var receiveNum: BigDecimal = BigDecimal.ZERO
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