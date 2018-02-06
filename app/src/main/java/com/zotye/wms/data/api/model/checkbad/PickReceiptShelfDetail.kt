package com.zotye.wms.data.api.model.checkbad

/**
 * Created by hechuangju on 2018/02/05
 */
class PickReceiptShelfDetail {
    var spMaterialDetailId: String? = null
    /**
     * 区域描述
     */
    var areaDesc: String? = null
    /**
     * 库位描述
     */
    var positionDesc: String? = null
    /**
     * 物料code
     */
    var materialCode: String? = null
    /**
     * 存储单元编码
     */
    var unitCode: String? = null
    /**
     * 下架数量
     */
    var num: Long = 0
    /**
     * 是否已经确认
     */
    var confirmed: Boolean = false
}