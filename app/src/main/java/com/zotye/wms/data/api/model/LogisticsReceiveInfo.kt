package com.zotye.wms.data.api.model

import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/01/18
 */
class LogisticsReceiveInfo {
    var userId: String? = null

    var code: String? = null// 组托收货的时候传入托盘编码 非组托收货的时候传入包装编码

    var eType: String? = null// 托盘  包装

    var receiveNum: BigDecimal? = null//收货数量  组托收货时 为托盘中所有包装的实收数合计 非组托收货时未该包装的收货数量

    var children: ArrayList<LogisticsReceiveInfo>? = null// 组托收货时 传入包装 非组托收货时为空集合

}