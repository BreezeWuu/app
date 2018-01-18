package com.zotye.wms.data.api.model

/**
 * Created by hechuangju on 2018/01/17
 */
class BarcodeInfo {
    /**
     * 是否是托盘
     */
    val barCodeType: Int = 0
    /**
     * 包含信息
     */
    val barCodeInfo: String? = ""
}

enum class BarCodeType {
    Package,
    Pallet;

    companion object {
        fun fromCodeType(type: Int): BarCodeType? {
            BarCodeType.values().forEach { barCodeType ->
                if (barCodeType.ordinal == type) return barCodeType
            }
            return null
        }
    }
}