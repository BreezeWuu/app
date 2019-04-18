package com.zotye.wms.data.api.model

import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/01/18
 */
class LogisticsReceiveInfo {
    var userId: String? = null

    var code: String? = null// 组托收货的时候传入托盘编码 非组托收货的时候传入包装编码

    var eType: String? = null// 托盘  包装

    var receiveNum: BigDecimal? = BigDecimal.ZERO//收货数量  组托收货时 为托盘中所有包装的实收数合计 非组托收货时未该包装的收货数量
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)
    /**
     * 少收数量
     */
    var noReceiveNum: BigDecimal? = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    /**
     * 质量问题
     */
    var badNum: BigDecimal? = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var batchNum: String? = null

    var children: ArrayList<LogisticsReceiveInfo>? = null// 组托收货时 传入包装 非组托收货时为空集合

}