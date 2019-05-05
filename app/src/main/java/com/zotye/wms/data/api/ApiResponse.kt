package com.zotye.wms.data.api

import android.text.TextUtils
import com.google.gson.annotations.SerializedName


/**
 * Created by hechuangju on 2017/7/18.
 */

data class ApiResponse<T>(
        @SerializedName("retCode") var status: Int, //0
        @SerializedName("payLoad") var data: T?
) {
    @SerializedName("retMsg")
    var message: String = ""
        get() {
            return if (TextUtils.isEmpty(field)) "" else field
        }

    fun isSucceed(): Boolean {
        return status == 0
    }
}