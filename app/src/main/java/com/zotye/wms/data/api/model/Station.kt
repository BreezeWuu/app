package com.zotye.wms.data.api.model


import com.google.gson.annotations.SerializedName

data class Station(
        @SerializedName("code")
        val code: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String
)