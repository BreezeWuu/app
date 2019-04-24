package com.zotye.wms.data.api.model


/**
 * Created by hechuangju on 2019/04/19
 */
class PutAwayInfo {

    val materialNum: String? = null

    val materialDescription: String? = null

    val storagePositionCode: String? = null

    val storagePositionName: String? = null

    val storageAreaName: String? = null


    fun materialNum(): String {
        return materialNum ?: "未知"
    }

    fun materialDescription(): String {
        return materialDescription ?: "未知"
    }

    fun storagePositionCode(): String {
        return storagePositionCode ?: "未知"
    }

    fun storagePositionName(): String {
        return storagePositionName ?: "未知"
    }

    fun storageAreaInfoName(): String {
        return storageAreaName ?: "未知"
    }
}