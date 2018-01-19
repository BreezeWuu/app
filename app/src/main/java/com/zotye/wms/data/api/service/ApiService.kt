package com.zotye.wms.data.api.service

import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by hechuangju on 2017/09/21
 */
interface ApiService {

    @FormUrlEncoded
    @POST("user/login")
    fun doLoginCall(@Field("userName") userName: String, @Field("password") pwd: String): Call<ApiResponse<User>>

    @FormUrlEncoded
    @POST("user/info")
    fun getUserInfo(@Field("userId") userId: String): Call<ApiResponse<User>>

    @FormUrlEncoded
    @POST("bar_code/info")
    fun getPackageInfo(@Field("userId") userId: String, @Field("isGroupReceive") isGroupReceive: Boolean, @Field("packageId") packageId: String): Call<ApiResponse<BarcodeInfo>>

    @FormUrlEncoded
    @POST("bar_code/logisticsReceive")
    fun logisticsReceive(@Field("jsonString") logisticsReceiveJsonString: String): Call<ApiResponse<String>>
}