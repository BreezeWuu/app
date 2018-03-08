package com.zotye.wms.data.api.request

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
    var id: String? = null

    var isBom: Boolean? = null

    var receiveNum: Long = 0

    var batchNum: String? = null

    var lackNum: Long = 0

    var unqualifyNum: Long = 0
}