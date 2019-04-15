package com.zotye.wms.data.api.model

import java.math.BigDecimal

/**
 * Created by hechuangju on 2019/04/12
 */
class ManualBoardDeliveryDto {

     val id: String? = null

     val materialId: String? = null

    /**
     * 物料名称
     */
     val materialName: String? = null

    /**
     * 工位
     */
     val station: String? = null

    /**
     * 需求数量
     */
     val requiredQuantity: Int? = null

    /**
     * 拣配时间
     */
     val pickTime: Int? = null

    /**
     * 缓冲时间
     */
     val bufferTime: Int? = null

    /**
     * 出库库存地点code
     */
     val outLocationCode: String? = null
    /**
     * 出库地点
     */
     val outLocation: String? = null

    /**
     * 出库库存区域code
     */
     val outAreaCode: String? = null
    /**
     * 出库区域
     */
     val outArea: String? = null

    /**
     * 供应商code
     */
     val supplierCode: String? = null
    /**
     * 供应商
     */
     val supplierName: String? = null

    /**
     * 接收部门code
     */
     val costCenterCode: String? = null
    /**
     * 接收部门
     */
     val costCenter: String? = null

    /**
     * 手工看板Code
     */
     val manualBoardCode: String? = null


    /**
     * 当前库存数
     */
     val currentNum: BigDecimal? = null

    /**
     * 道口
     */
     val crossing: String? = null

    /**
     * 线路
     */
     val lineCode: String? = null

     val materialNum: String? = null

    /**
     * 新增需求时间
     */
    var demandTime: String? = null
}
