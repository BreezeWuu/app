package com.zotye.wms.data.api.model


import com.google.gson.annotations.SerializedName

data class MaterialVatague(
        @SerializedName("id")
        val id: String,
        @SerializedName("materialId")
        val materialId: String,
        @SerializedName("materialName")
        val materialName: String,
        @SerializedName("materialNum")
        val materialNum: String,
        @SerializedName("materialType")
        val materialType: Any,
        @SerializedName("packageNum")
        val packageNum: Int,
        @SerializedName("text")
        val text: String,
        @SerializedName("unit")
        val unit: String


) {
    override fun toString(): String {
        return "$materialId ($materialName)"
    }
}