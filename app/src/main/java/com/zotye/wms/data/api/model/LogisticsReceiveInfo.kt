package com.zotye.wms.data.api.model

import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/01/18
 */
class LogisticsReceiveInfo {
    private val code: String? = null// 组托收货的时候传入托盘编码 非组托收货的时候传入包装编码

    private val eType: String? = null// 托盘  包装

    private val receiveNum: BigDecimal? = null//收货数量  组托收货时 为托盘中所有包装的实收数合计 非组托收货时未该包装的收货数量

    private val children: List<LogisticsReceiveInfo>? = null// 组托收货时 传入包装 非组托收货时为空集合

    private val userId: String? = null//收货人Id
}