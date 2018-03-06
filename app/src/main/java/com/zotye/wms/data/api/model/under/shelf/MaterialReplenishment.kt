package com.zotye.wms.data.api.model.under.shelf

/**
 * Created by hechuangju on 2018/03/06
 */
class MaterialReplenishment{
    var materialId: String? = null

    var materialName: String? = null

    var packageNum: Long = 0

    var requireNum: Long = 0

    var materialNum: String? = null//物料号

    var slId: String? = null//库存地点ID

    var sltId: String? = null//抬头库存地点ID
}