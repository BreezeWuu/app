package com.zotye.wms.data.api.model

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by hechuangju on 2018/01/25
 */
class PickListMaterialInfo : MultiItemEntity {
    companion object {
        val TYPE_PICK_LIST_MATERIAL_INFO = 1
    }

    var materialNum: String? = null
    var materialWRKST: String? = null
    var desc: String? = null
    var unit: String? = null

    override fun getItemType(): Int {
        return 1
    }
}