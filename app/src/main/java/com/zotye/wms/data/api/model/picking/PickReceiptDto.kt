package com.zotye.wms.data.api.model.picking

/**
 * Created by hechuangju on 2018/03/05
 */
class PickReceiptDto{
    var pickReceiptId: String? = null

    var pickReceiptCode: String? = null

    var pickReceiptSource: String? = null

    var pickReceiptDetail: List<PdaPickReceiptDetailDto>? = null
}