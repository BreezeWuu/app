package com.zotye.wms.data.api.model

import java.io.Serializable
import java.math.BigDecimal

/**
 * Created by hechuangju on 2019/05/02
 */
class UnpackingDto : Serializable {

    /**
     * 源包装号
     */
    var sourceCode: String? = null

    /**
     * 拆包数量
     */
    var num: BigDecimal? = null

    override fun toString(): String {
        return StringBuffer("源包装号：").append(sourceCode).append("； 拆包数量：").append(if (num == null) "" else num).toString()
    }

    companion object {

        private const val serialVersionUID = 1073001152052819610L
    }
}