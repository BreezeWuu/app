package com.zotye.wms.data.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by hechuangju on 2017/7/18.
 */

data class ApiResponse<T>(
        @SerializedName("retCode") var status: Int, //0
        @SerializedName("retMsg") var message: String = "", //null
        @SerializedName("payLoad") var data: T?
){
    fun isSucceed(): Boolean {
        return status == 0
    }
}