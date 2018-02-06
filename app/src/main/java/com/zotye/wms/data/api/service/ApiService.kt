package com.zotye.wms.data.api.service

import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.*
import com.zotye.wms.data.api.model.checkbad.ExternalCheckPickReceiptConfirmDto
import com.zotye.wms.data.api.model.checkbad.GetPickReceiptShelfDetailRequestDto
import com.zotye.wms.data.api.model.checkbad.PickReceiptShelfDetail
import com.zotye.wms.data.api.model.goods.receive.GoodsReceiveResponse
import com.zotye.wms.data.api.model.under.shelf.PrMobileConfirmRequest
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by hechuangju on 2017/09/21
 */
interface ApiService {

    @GET("user/getAllFactory")
    fun getAllFactory(): Call<ApiResponse<List<FactoryInfo>>>

    @FormUrlEncoded
    @POST("user/login")
    fun doLoginCall(@Field("userName") userName: String, @Field("password") pwd: String,@Field("factoryCode") factoryCode:String): Call<ApiResponse<User>>

    @FormUrlEncoded
    @POST("user/info")
    fun getUserInfo(@Field("userId") userId: String): Call<ApiResponse<User>>

    @FormUrlEncoded
    @POST("bar_code/info")
    fun getPackageInfo(@Field("userId") userId: String, @Field("isGroupReceive") isGroupReceive: Boolean, @Field("packageId") packageId: String): Call<ApiResponse<BarcodeInfo>>

    @FormUrlEncoded
    @POST("bar_code/storageUnitInfoByCode")
    fun getStorageUnitInfoByBarcode(@Field("userId") userId: String, @Field("barCode") barCode: String): Call<ApiResponse<BarcodeInfo>>

    @FormUrlEncoded
    @POST("bar_code/storageUnitDetailInfoByCode")
    fun getStorageUnitDetailInfoByCode(@Field("userId") userId: String, @Field("barCode") barCode: String): Call<ApiResponse<BarcodeInfo>>

    @FormUrlEncoded
    @POST("bar_code/logisticsReceive")
    fun logisticsReceive(@Field("jsonString") logisticsReceiveJsonString: String): Call<ApiResponse<GoodsReceiveResponse>>

    @FormUrlEncoded
    @POST("bar_code/authStorageUnitNewPositionByQRCode")
    fun authStorageUnitNewPositionByQRCode(@Field("userId") userId: String, @Field("qrCode") qrCode: String): Call<ApiResponse<String>>

    @FormUrlEncoded
    @POST("bar_code/storageUnitModify")
    fun storageUnitModify(@Field("userId") userId: String, @Field("code") code: String, @Field("newStoragePositionCode") newStoragePositionCode: String): Call<ApiResponse<String>>

    @FormUrlEncoded
    @POST("bar_code/logisticsReceiveConfirmInfoByCode")
    fun logisticsReceiveConfirmInfoByCode(@Field("userId") userId: String, @Field("barCode") barCode: String): Call<ApiResponse<List<PackageInfo>>>

    @FormUrlEncoded
    @POST("bar_code/logisticsReceiveConfirm")
    fun logisticsReceiveConfirm(@Field("userId") userId: String, @Field("noteId") noteId: String): Call<ApiResponse<String>>

    @FormUrlEncoded
    @POST("bar_code/pickListInfoByCode")
    fun getPickListInfoByCode(@Field("userId") userId: String, @Field("OpterationType") OpterationType: String, @Field("barCode") barCode: String): Call<ApiResponse<BarcodeInfo>>

    @FormUrlEncoded
    @POST("bar_code/getStorageUnitMaterialTotalNumber")
    fun getStorageUnitMaterialTotalNumber(@Field("userId") userId: String, @Field("storageUnitInfoCode") storageUnitInfoCode: String, @Field("spDetailId") spDetailId: String): Call<ApiResponse<String>>

    @FormUrlEncoded
    @POST("bar_code/createLoadingList")
    fun createLoadingList(@Field("userId") userId: String, @Field("carNumber") carNumber: String, @Field("pickListJson") pickListJson: String): Call<ApiResponse<String>>

    @POST("bar_code/underShelfConfirm")
    fun underShelfConfirm(@Body request: PrMobileConfirmRequest): Call<ApiResponse<String>>

    @POST("bar_code/getPickReceiptShelfDetail")
    fun getPickReceiptShelfDetail(@Body requestDtos: List<GetPickReceiptShelfDetailRequestDto>): Call<ApiResponse<List<PickReceiptShelfDetail>>>

    @POST("bar_code/externalCheckPickReceiptConfirm")
    fun externalCheckPickReceiptConfirm(@Body request: ExternalCheckPickReceiptConfirmDto): Call<ApiResponse<String>>
}