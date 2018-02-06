package com.zotye.wms.data.api

import com.zotye.wms.data.api.model.checkbad.ExternalCheckPickReceiptConfirmDto
import com.zotye.wms.data.api.model.checkbad.GetPickReceiptShelfDetailRequestDto
import com.zotye.wms.data.api.model.checkbad.PickReceiptShelfDetail
import com.zotye.wms.data.api.model.under.shelf.PrMobileConfirmRequest
import com.zotye.wms.data.api.service.ApiService
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hechuangju on 2017/09/18
 */
@Singleton
class AppApiHelper @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override fun doLoginCall(userName: String, pwd: String) = apiService.doLoginCall(userName, pwd)

    override fun getUserInfo(userId: String) = apiService.getUserInfo(userId)

    override fun getPackageInfo(userId: String, isGroupReceive: Boolean, packageId: String) = apiService.getPackageInfo(userId, isGroupReceive, packageId)

    override fun getStorageUnitInfoByBarcode(userId: String, barCode: String) = apiService.getStorageUnitInfoByBarcode(userId, barCode)

    override fun getStorageUnitDetailInfoByCode(userId: String, barCode: String) = apiService.getStorageUnitDetailInfoByCode(userId, barCode)

    override fun logisticsReceive(logisticsReceiveJsonString: String) = apiService.logisticsReceive(logisticsReceiveJsonString)

    override fun authStorageUnitNewPositionByQRCode(userId: String, qrCode: String) = apiService.authStorageUnitNewPositionByQRCode(userId, qrCode)

    override fun storageUnitModify(userId: String, oldStoragePositionCode: String, newStoragePositionCode: String) = apiService.storageUnitModify(userId, oldStoragePositionCode, newStoragePositionCode)

    override fun logisticsReceiveConfirmInfoByCode(userId: String, barCode: String) = apiService.logisticsReceiveConfirmInfoByCode(userId, barCode)

    override fun logisticsReceiveConfirm(userId: String, noteId: String) = apiService.logisticsReceiveConfirm(userId, noteId)

    override fun getPickListInfoByCode(userId: String, OpterationType: String, barCode: String) = apiService.getPickListInfoByCode(userId, OpterationType, barCode)

    override fun getStorageUnitMaterialTotalNumber(userId: String, storageUnitInfoCode: String, spDetailId: String) = apiService.getStorageUnitMaterialTotalNumber(userId, storageUnitInfoCode, spDetailId)

    override fun underShelfConfirm(request: PrMobileConfirmRequest) = apiService.underShelfConfirm(request)

    override fun createLoadingList(userId: String, carNumber: String, pickListJson: String) = apiService.createLoadingList(userId, carNumber, pickListJson)

    override fun getPickReceiptShelfDetail(requestDtos: List<GetPickReceiptShelfDetailRequestDto>)=apiService.getPickReceiptShelfDetail(requestDtos)

    override fun externalCheckPickReceiptConfirm(request: ExternalCheckPickReceiptConfirmDto) =apiService.externalCheckPickReceiptConfirm(request)
}