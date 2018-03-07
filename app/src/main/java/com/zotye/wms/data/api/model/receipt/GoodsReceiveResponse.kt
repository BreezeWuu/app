package com.zotye.wms.data.api.model.receipt
import com.google.gson.annotations.SerializedName


/**
 * Created by hechuangju on 2018/02/01
 */
data class GoodsReceiveResponse(
		@SerializedName("putAwayInfoId") var putAwayInfoId: String = "", //72933352-5979-46ad-9e88-bf5c3b26e026
		@SerializedName("deliveryNoteCode") var deliveryNoteCode: String = "" //ASN2018020100543
)