package com.zotye.wms.data.api.model.picking

import com.zotye.wms.data.api.model.checkbad.ExternalCheckDetailDto


/**
 * Created by hechuangju on 2018/02/08
 */
class ProduceAcquireConfirmRequest {
    var userId: String? = null//操作用户ID

    var costCenterCode: String? = null//成本中心/领用部门编码

    var externalDetail: List<ExternalCheckDetailDto>? = null//领料明细DTO
}