package com.zotye.wms.data.api.model.under.shelf

/**
 * Created by hechuangju on 2018/02/01
 */
class PrMobileConfirmRequest {
    var prNo: String? = null
    var userId: String? = null
    var confirmDetail: ArrayList<PrCheckInfoDto>? = null

    class PrCheckInfoDto {
        var id: String? = null//下架明细id
        var checkNum: Long = 0//下架后实盘数量
    }
}