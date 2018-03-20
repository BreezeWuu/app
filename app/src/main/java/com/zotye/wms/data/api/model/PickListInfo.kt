package com.zotye.wms.data.api.model

import android.text.TextUtils
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zotye.wms.util.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/01/25
 */
class PickListInfo(var code: String? = null) : AbstractExpandableItem<PickListMaterialInfo>(), MultiItemEntity {
    companion object {
        const val TYPE_PICK_LIST = 0
    }

    var id: String = ""

    var quantity: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var supplierId: String? = null
    /**
     * 供应商代码
     */
    var supplierCode: String? = null
    /**
     * 供应商名
     */
    var supplierName: String? = null

    /**
     * 库位代码
     */
    var storagePositionCode: String? = null
    /**
     * 库位名称
     */
    var storagePositionName: String? = null
    /**
     * 库存区域代码
     */
    var storageAreaInfoCode: String? = null
    /**
     * 库位名称
     */
    var storageAreaInfoName: String? = null
    /**
     * 出库地点
     */
    var outSlId: String? = null

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