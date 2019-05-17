package com.zotye.wms.data.api.model


import com.google.gson.annotations.SerializedName

data class ManuaMaterialInfo(
        @SerializedName("detaultSupplierCode")
        val detaultSupplierCode: String,
        @SerializedName("error")
        val error: String,
        @SerializedName("materialId")
        val materialId: String,
        @SerializedName("materialName")
        val materialName: String,
        @SerializedName("materialNum")
        val materialNum: String,
        @SerializedName("packageNum")
        val packageNum: Int,
        @SerializedName("stations")
        val stations: List<Station>,
        @SerializedName("suppliers")
        val suppliers: List<Supplier>,
        @SerializedName("unit")
        val unit: String
)