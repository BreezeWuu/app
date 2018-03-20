package com.zotye.wms.data.api.model.receipt

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zotye.wms.util.BigDecimalUtil
import java.io.Serializable
import java.math.BigDecimal

/**
 * Created by hechuangju on 2018/03/07
 */
class DeliveryNoteInfoResponse {
    var deliveryNoteInfo: DeliveryNoteInfoDto? = null
    var receiveDetailList: List<ReceiveDetailDto>? = null
}

class DeliveryNoteInfoDto : AbstractExpandableItem<ReceiveDetailDto>(), MultiItemEntity {

    companion object {
        const val TYPE_NOTE_INFO = 0
    }

    var noteCode: String? = null

    var noteType: String? = null

    var supplierCode: String? = null

    var supplierName: String? = null

    var postTime: String? = null//发货时间

    var storageLocationCode: String? = null

    var storageLocationName: String? = null

    var pickReceiptFlag: String? = null

    var pickReceiptCount: Int = 0

    var receivedPickReceiptCount: Int = 0

    override fun getItemType(): Int {
        return 0
    }

    override fun getLevel(): Int {
        return 0
    }
}

class ReceiveDetailDto : MultiItemEntity, Serializable {
    companion object {
        const val TYPE_RECEIVE_DETAIL = 1
    }

    /**
     * 记录Id
     */
    var id: String? = null

    /**
     * 物料号
     */
    var materialNo: String? = null

    /**
     * 物料名称
     */
    var materialName: String? = null

    /**
     * 需求数量
     */
    var requireNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    /**
     * 单包装数量
     */
    var packageNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    /**
     * 实收数量
     */
    var receiveNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    /**
     * 基本计量单位
     */
    var unit: String? = null

    /**
     * 是否是随机附件总成
     */
    var isBom: Boolean? = null

    /**
     * 子件定额
     */
    var componentCount: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    /**
     * 父Id
     */
    var parent: String? = null

    /**
     * 是否需要批次管理
     */
    var isBatch: Boolean? = null

    /**
     * 批次号
     */
    var batchNum: String? = null

    /**
     * 料码
     */
    var wrkst: String? = null

    var lackNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var unqualifyNum: BigDecimal = BigDecimal.ZERO
        set(value) {
            field = BigDecimalUtil.formatValue(value)
        }
        get() = BigDecimalUtil.getValue(field)

    var isEditEnable: Boolean = false

    var child: ArrayList<ReceiveDetailDto> = ArrayList()

    override fun getItemType(): Int {
        return 1
    }
}