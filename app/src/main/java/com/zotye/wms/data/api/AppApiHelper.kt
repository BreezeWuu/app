package com.zotye.wms.data.api

import com.zotye.wms.data.api.model.StorageUnitInfo
import com.zotye.wms.data.api.service.ApiService
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hechuangju on 2017/09/18
 */
@Singleton
class AppApiHelper @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override fun doLoginCall(email: String, pwd: String) = apiService.doLoginCall(email, pwd)

    override fun getUserInfo(userId: String) = apiService.getUserInfo(userId)

    override fun getPackageInfo(userId: String, isGroupReceive: Boolean, packageId: String) = apiService.getPackageInfo(userId, isGroupReceive, packageId)

    override fun getStorageUnitInfoByBarcode(userId: String, barCode: String) = apiService.getStorageUnitInfoByBarcode(userId, barCode)

    override fun getStorageUnitDetailInfoByCode(userId: String, barCode: String)=apiService.getStorageUnitDetailInfoByCode(userId, barCode)

    override fun logisticsReceive(logisticsReceiveJsonString: String) = apiService.logisticsReceive(logisticsReceiveJsonString)

    override fun authStorageUnitNewPositionByQRCode(userId: String, qrCode: String) = apiService.authStorageUnitNewPositionByQRCode(userId, qrCode)

    override fun storageUnitModify(userId: String, oldStoragePositionCode: String, newStoragePositionCode: String) = apiService.storageUnitModify(userId, oldStoragePositionCode, newStoragePositionCode)

    override fun logisticsReceiveConfirmInfoByCode(userId: String, barCode: String) = apiService.logisticsReceiveConfirmInfoByCode(userId, barCode)

    override fun logisticsReceiveConfirm(userId: String, noteId: String)=apiService.logisticsReceiveConfirm(userId, noteId)
}