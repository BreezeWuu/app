package com.zotye.wms.util

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by hechuangju on 2018/03/20
 */
object BigDecimalUtil {
    fun formatValue(value: BigDecimal?): BigDecimal = value?.setScale(3, RoundingMode.HALF_EVEN)
            ?: BigDecimal.ZERO

    fun getValue(value: BigDecimal?): BigDecimal = value?.stripTrailingZeros()?.toPlainString()?.toBigDecimal()
            ?: BigDecimal.ZERO
}