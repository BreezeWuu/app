package com.zotye.wms.data.api.model

import android.text.TextUtils
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by hechuangju on 2018/01/25
 */
class PickListInfo(var code: String? = null) : AbstractExpandableItem<PickListMaterialInfo>(), MultiItemEntity {

    var id: String = ""

    var quantity: Long = 0
    /**
     * 供应商代码
     */
    var supplierCode: String? = null
    /**
     * 供应商名
     */
    var supplierName: String? = null

    var materialInfoList: List<PickListMaterialInfo>? = null

    override fun getItemType(): Int {
        return 0
    }

    override fun getLevel(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is PickListInfo)
            false
        else {
            if (TextUtils.isEmpty(this.code))
                false
            else
                this.code == other.code
        }
    }

    override fun hashCode(): Int {
        return code?.hashCode() ?: 0
    }
}