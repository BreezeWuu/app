package com.zotye.wms.data.api.model.picking

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by hechuangju on 2018/03/05
 */
class PickReceiptDto: AbstractExpandableItem<PdaPickReceiptDetailDto>(), MultiItemEntity {
    var pickReceiptId: String? = null

    var pickReceiptCode: String? = null

    var pickReceiptSource: String? = null

    var pickReceiptDetail: List<PdaPickReceiptDetailDto>? = null

    override fun getItemType(): Int {
        return 0
    }

    override fun getLevel(): Int {
        return 0
    }
}