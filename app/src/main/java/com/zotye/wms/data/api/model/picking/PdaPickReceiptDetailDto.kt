package com.zotye.wms.data.api.model.picking

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by hechuangju on 2018/03/05
 */
class PdaPickReceiptDetailDto : MultiItemEntity {
    var pickReceiptDetailId: String? = null

    var materialNum: String? = null

    var materialName: String? = null

    var wrkst: String? = null

    var deliveryCount: Long = 0

    var sequence: Int = 0

    var lackNum: Long = 0

    var unqualifyNum: Long = 0

    override fun getItemType(): Int {
        return 1
    }
}