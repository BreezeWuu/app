package com.zotye.wms.data.api.model


import com.google.gson.annotations.SerializedName

data class Supplier(
        @SerializedName("code")
        val code: String,
        @SerializedName("name")
        val name: String
) {
    override fun toString(): String {
        return name
    }
}