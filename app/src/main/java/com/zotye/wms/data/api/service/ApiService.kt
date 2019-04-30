package com.zotye.wms.data.api.service

import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.*
import com.zotye.wms.data.api.model.checkbad.ExternalCheckPickReceiptConfirmDto
import com.zotye.wms.data.api.model.checkbad.GetPickReceiptShelfDetailRequestDto
import com.zotye.wms.data.api.model.checkbad.PickReceiptShelfDetail
import com.zotye.wms.data.api.model.outcheck.OutBoundBadNewsDto
import com.zotye.wms.data.api.model.outcheck.OutBoundCheckDto
import com.zotye.wms.data.api.model.receipt.GoodsReceiveResponse
import com.zotye.wms.data.api.model.receipt.MobilePickReceiptRecvDto
import com.zotye.wms.data.api.model.picking.PickReceiptDto
import com.zotye.wms.data.api.model.picking.ProduceAcquireConfirmRequest
import com.zotye.wms.data.api.model.receipt.DeliveryNoteInfoResponse
import com.zotye.wms.data.api.model.under.shelf.MaterialReplenishment
import com.zotye.wms.data.api.model.under.shelf.PrMobileConfirmRequest
import com.zotye.wms.data.api.model.under.shelf.SUMaterialInfo
import com.zotye.wms.data.api.request.MobileNoteRecvRequest
import com.zotye.wms.data.api.response.ReceiveConfirmResponse
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
    fun doLoginCall(@Field("userName") userName: String, @Field("password") pwd: String, @Field("factoryCode") factoryCode: String): Call<ApiResponse<User>>

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
    fun logisticsReceiveConfirmInfoByCode(@Field("userId") userId: String, @Field("barCode") barCode: String): Call<ApiResponse<BarcodeInfo>>

    @FormUrlEncoded
    @POST("bar_code/logisticsReceiveConfirm")
    fun logisticsReceiveConfirm(@Field("userId") userId: String, @Field("noteId") noteId: String): Call<ApiResponse<String>>

    @FormUrlEncoded
    @POST("bar_code/pickListInfoByCode")
    fun getPickListInfoByCode(@Field("userId") userId: String, @Field("OpterationType") OpterationType: String, @Field("barCode") barCode: String): Call<ApiResponse<BarcodeInfo>>

    @FormUrlEncoded
    @POST("bar_code/getStorageUnitMaterialTotalNumber")
    fun getStorageUnitMaterialTotalNumber(@Field("userId") userId: String, @Field("storageUnitInfoCode") storageUnitInfoCode: String, @Field("spDetailId") spDetailId: String): Call<ApiResponse<SUMaterialInfo>>

    @FormUrlEncoded
    @POST("bar_code/createLoadingList")
    fun createLoadingList(@Field("userId") userId: String, @Field("carNumber") carNumber: String, @Field("pickListJson") pickListJson: String): Call<ApiResponse<String>>

    @POST("bar_code/underShelfConfirm")
    fun underShelfConfirm(@Body request: PrMobileConfirmRequest): Call<ApiResponse<List<MaterialReplenishment>>>

    @POST("bar_code/getPickReceiptShelfDetail")
    fun getPickReceiptShelfDetail(@Body requestDtos: List<GetPickReceiptShelfDetailRequestDto>): Call<ApiResponse<List<PickReceiptShelfDetail>>>

    @POST("bar_code/externalCheckPickReceiptConfirm")
    fun externalCheckPickReceiptConfirm(@Body request: ExternalCheckPickReceiptConfirmDto): Call<ApiResponse<String>>

    @FormUrlEncoded
    @POST("bar_code/getPickingBarCodeInfo")
    fun getPickingBarCodeInfo(@Field("userId") userId: String, @Field("barCode") barCode: String): Call<ApiResponse<List<StoragePackageMaterialInfo>>>

    @FormUrlEncoded
    @POST("bar_code/getCostCenterByUser")
    fun getCostCenterByUser(@Field("userId") userId: String): Call<ApiResponse<List<CostCenter>>>

    @POST("bar_code/createPDAProduceAcquire")
    fun createPDAProduceAcquire(@Body request: ProduceAcquireConfirmRequest): Call<ApiResponse<String>>

    @FormUrlEncoded
    @POST("bar_code/loadingReceipt/getPickReceiptInfoByCode")
    fun getPickReceiptInfoByCode(@Field("userId") userId: String, @Field("barCode") barCode: String): Call<ApiResponse<PickReceiptDto>>

    @POST("bar_code/loadingReceipt/truckReceive")
    fun truckReceive(@Body recvInfo: MobilePickReceiptRecvDto): Call<ApiResponse<ReceiveConfirmResponse>>

    @FormUrlEncoded
    @POST("bar_code/DeliveryNoteReceipt/getDeliveryNoteInfoByCode")
    fun getDeliveryNoteInfoByCode(@Field("userId") userId: String, @Field("barCode") barCode: String): Call<ApiResponse<DeliveryNoteInfoResponse>>

    @FormUrlEncoded
    @POST("bar_code/DeliveryNoteReceipt/getSlInfoForDeliveryNote")
    fun getSlInfoForDeliveryNote(@Field("userId") userId: String, @Field("noteCode") noteCode: String): Call<ApiResponse<List<ValidSlInfoDto>>>

    @POST("bar_code/DeliveryNoteReceipt/normalNoteReceive")
    fun normalNoteReceive(@Body recvInfo: MobileNoteRecvRequest): Call<ApiResponse<ReceiveConfirmResponse>>

    //新增获取捡配单信息
    @FormUrlEncoded
    @POST("bar_code/outboundInspection/getDataByCode")
    fun getPickInfo(@Field("userId") userId: String, @Field("code") barCode: String): Call<ApiResponse<List<OutBoundCheckDto>>>

    //获取物料不良品信息
    @FormUrlEncoded
    @POST("bar_code/outboundInspection/getFailedInfo")
    fun getBadGoodsNews(@Field("userId") userId: String, @Field("code") barCode: String, @Field("materialId") materialId: String, @Field("supplierId") supplierId: String, @Field("batchNum") batchNum: String): Call<ApiResponse<OutBoundBadNewsDto>>

    //更新不良品信息
    @FormUrlEncoded
    @POST("bar_code/outboundInspection/operFailedInfo")
    fun operBadGoodsNews(@Field("userId") userId: String, @Field("code") barCode: String, @Field("materialId") materialId: String, @Field("supplierId") supplierId: String, @Field("batchNum") batchNum: String, @Field("num") num: String, @Field("reason") reason: String): Call<ApiResponse<String>>

    //删除不良品信息
    @FormUrlEncoded
    @POST("bar_code/outboundInspection/delFailedInfo")
    fun delBadGoodsNews(@Field("userId") userId: String, @Field("code") barCode: String, @Field("materialId") materialId: String, @Field("supplierId") supplierId: String, @Field("batchNum") batchNum: String): Call<ApiResponse<String>>

    /**上架确认*/
    @FormUrlEncoded
    @POST("bar_code/checkStSlInfo")
    fun checkStSlInfo(@Field("stCode") stCode: String, @Field("spCode") spCode: String): Call<ApiResponse<String>>

    /**上线确认*/
    @FormUrlEncoded
    @POST("bar_code/checkPickStationInfo")
    fun checkPickStationInfo(@Field("pickCode") pickCode: String, @Field("stationCode") stationCode: String): Call<ApiResponse<String>>

    /**获取库位信息*/
    @FormUrlEncoded
    @POST("bar_code/getStoragePositionInfoByCode")
    fun getStoragePositionInfoByCode(@Field("code") code: String): Call<ApiResponse<List<PutAwayInfo>>>

    /**获取库位信息*/
    @FormUrlEncoded
    @POST("bar_code/viewVehicleReceiptFilterInfo")
    fun viewVehicleReceiptFilterInfo(@Field("userId") userId: String): Call<ApiResponse<VehicleReceiptFilterInfo>>

    /**获取库位信息*/
    @POST("bar_code/searchVehicleReceipt")
    fun searchVehicleReceipt(@Body dto: VehicleReceiptParamsDto): Call<ApiResponse<List<VehicleReceiptDto>>>

    @FormUrlEncoded
    @POST("bar_code/getVehicleReceiptByCode")
    fun getVehicleReceiptByCode(@Field("code") code: String): Call<ApiResponse<List<VehicleReceiptDto>>>

    /**获取库位信息*/
    @FormUrlEncoded
    @POST("bar_code/getMesPickReceiptListById")
    fun getMesPickReceiptListById(@Field("id") id: String): Call<ApiResponse<List<MESPickReceiptDto>>>

    /**获取手工看板信息*/
    @FormUrlEncoded
    @POST("bar_code/ManualBoard/getManualBoardList")
    fun getManualBoardLis(@Field("code") code: String, @Field("materialNum") materialNum: String): Call<ApiResponse<List<ManualBoardDeliveryDto>>>

    /**手工看板出库*/
    @POST("bar_code/ManualBoard/saveManualBoardOut")
    fun saveManualBoardOut(@Body manualBoardDeliveryDto: List<ManualBoardDeliveryDto>): Call<ApiResponse<List<MaterialPullResult>>>

}