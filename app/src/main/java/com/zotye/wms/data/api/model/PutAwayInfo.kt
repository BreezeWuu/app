package com.zotye.wms.data.api.model

import java.math.BigDecimal


/**
 * Created by hechuangju on 2019/04/19
 */
class PutAwayInfo {

    val piNo: String? = null

    val material: Material? = null

    val storagePosition: StoragePosition? = null


    val uCount: BigDecimal? = null

    val batchNum: String? = null

    fun materialNum(): String {
        return material?.materialNum ?: ""
    }

    fun materialDescription(): String {
        return material?.eDesc ?: ""
    }

    fun storagePositionName(): String {
        return storagePosition?.spName ?: ""
    }

    fun storageAreaInfoName(): String {
        return storagePosition?.storageAreaInfo?.areaName ?: ""
    }
}

class Material {
    /**
     * 物料号
     */
    val materialNum: String? = null
    /**
     * 三方物流拣配时间
     */
    val pickTime: Int? = null
    /**
     * 三方物流缓冲时间
     */
    val bufferTime: Int? = null
    /**
     * 三方物流运输时间
     */
    val transportTime: Int? = null
    /**
     * 物料描述
     */
    val eDesc: String? = null
    /**
     * 基本计量单位
     */
    val unit: String? = null
    /**
     * 料码
     */
    val WRKST: String? = null
    /**
     * 物料类型
     */
    val eType: String? = null
    /**
     * 物料组
     */
    val groupCode: String? = null
    /**
     * 物料组描述
     */
    val groupDesc: String? = null
    /**
     * 散装物料("":非散装；"1":散装)
     */
    val bulkFlag: String? = null
    /**
     * 库存单位
     */
    val storageUnit: String? = null

    /**
     * 基本单位数量
     */
    val basicUnitCount: BigDecimal? = null

    /**
     * 库存单位数量
     */
    val storageUnitCount: BigDecimal? = null
}

class StoragePosition {
    /**
     * 库位信息
     */
    val spName: String? = null

    val storageLocation: StorageLocation? = null
    /**
     * 库存区域
     */
    val storageAreaInfo: StorageAreaInfo? = null
}

class StorageLocation {
    /**
     * 库存地点编号
     */
    val slCode: String? = null
    /**
     * 描述
     */
    val slName: String? = null
}

class StorageAreaInfo {
    /**
     * 区域代码
     */
    val areaCode: String? = null
    /**
     * 库存区域描述
     */
    val areaName: String? = null
}