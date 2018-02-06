package com.zotye.wms.data.api.model.checkbad

/**
 * Created by hechuangju on 2018/02/06
 */
class ExternalCheckPickReceiptConfirmDto {
    var pickReceiptNo: String? = null//拣配单号

    var userId: String? = null//操作用户ID

    var externalDetail: List<ExternalCheckDetailDto>? = null//下架确认明细
}

class ExternalCheckDetailDto {
    var spMaterialDetailId: String? = null//查询时返回的行项目ID

    var storageUnitCode: String? = null

    var count: Long = 0
}