package com.zotye.wms.data.api.model

import java.util.HashMap
import java.util.HashSet

/**
 * Created by hechuangju on 2019/04/12
 */
class MaterialPullResult {
    var needsUpLoadSrm: Boolean? = null//是否需要上传Srm

    var needsAutoPrint: Boolean? = null//是否需要自动打印

    var mesPullId: String? = null//拉料明细ID

    var mesPullDetailIds: Set<String> = HashSet()

    var completeFlag: Boolean? = null

    var securityRpInfo: Map<String, List<MaterialReplenishmentRequest>> = HashMap()
}
