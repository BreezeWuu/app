package com.zotye.wms.data.api.model

/**
 * Created by hechuangju on 2018/01/17
 */
class BarcodeInfo {
    /**
     * 是否是托盘
     */
    val barCodeType: Int = 1
    /**
     * 包含信息
     */
    val barCodeInfo: String? = ""
}

enum class BarCodeType(val type: Int) {
    Package(1),
    Pallet(2),
    PickList(3),
    PickListPullOffShelfList(4),
    Unknown(0);

    companion object {
        fun fromCodeType(type: Int): BarCodeType? {
            BarCodeType.values().forEach { barCodeType ->
                if (barCodeType.type == type) return barCodeType
            }
            return Unknown
        }
    }
}