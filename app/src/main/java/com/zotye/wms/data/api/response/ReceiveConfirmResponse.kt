package com.zotye.wms.data.api.response

/**
 * Created by hechuangju on 2018/03/08
 */
class ReceiveConfirmResponse {
    var srmFlag: Boolean? = null//是否向SRM发送拣配单

    var putAwayFlag: Boolean? = null//是否生成上架单

    var sapFlag: Boolean? = null//是否上传SAP

    var noteId: String? = null//交货单Id

    var strictFlag: Boolean? = null//装车单标识

    var exchangeId: String? = null//物料退返单Id

    var exchangeNo: String? = null//物料退返单单号用于前台展示

    var spsContainsFlag: Boolean? = null//是否向SPS补货标识

    var lackPickReceiptIdList: HashSet<String> = HashSet()//少收的拣配单Id集合
}