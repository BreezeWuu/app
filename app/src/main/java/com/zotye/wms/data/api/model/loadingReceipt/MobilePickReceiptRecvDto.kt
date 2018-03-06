package com.zotye.wms.data.api.model.loadingReceipt

/**
 * Created by hechuangju on 2018/03/06
 */
class MobilePickReceiptRecvDto{
    var userId: String? = null

    var recvDetail: List<MobileSinglePickReceiptRecvDto>? = null
}
class MobileSinglePickReceiptRecvDto{
    var pickReceiptId: String? = null

    var pickReceiptDetail: List<PickReceiptDetailReceiveDto>? = null
}
class PickReceiptDetailReceiveDto{
    var pickReceiptDetailId: String? = null

    var recvNum: Long = 0

    var lackNum: Long = 0

    var unqualifyNum: Long = 0
}