package com.zotye.wms.data.api.model

import android.arch.persistence.room.util.TableInfo.Column


/**
 * Created by hechuangju on 2018/02/07
 */
class CostCenter {
    /**
     * 成本中心代码
     */
    var code: String? = null
    /**
     * 成本中心名称
     */
    var name: String? = null
    /**
     * 车间
     */
    var slCode: String? = null
    /**
     * 工厂代码
     */
    var factoryCode: String? = null

    override fun toString(): String {
        return name ?: ""
    }
}