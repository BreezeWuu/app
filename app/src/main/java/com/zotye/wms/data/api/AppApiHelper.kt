package com.zotye.wms.data.api

import com.zotye.wms.data.api.model.*
import com.zotye.wms.data.api.model.checkbad.ExternalCheckPickReceiptConfirmDto
import com.zotye.wms.data.api.model.checkbad.GetPickReceiptShelfDetailRequestDto
import com.zotye.wms.data.api.model.picking.ProduceAcquireConfirmRequest
import com.zotye.wms.data.api.model.receipt.MobilePickReceiptRecvDto
import com.zotye.wms.data.api.model.under.shelf.PrMobileConfirmRequest
import com.zotye.wms.data.api.request.MobileNoteRecvRequest
import com.zotye.wms.data.api.service.ApiService
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hechuangju on 2017/09/18
 */
@Singleton
class AppApiHelper @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override fun doLoginCall(userName: String, pwd: String, factoryCode: String) = apiService.doLoginCall(userName, pwd, factoryCode)

    override fun getUserInfo(userId: String) = apiService.getUserInfo(userId)

    override fun getPackageInfo(userId: String, isGroupReceive: Boolean, packageId: String) = apiService.getPackageInfo(userId, isGroupReceive, packageId)

    override fun joinPackageInfo(userId: String, packageId: String) = apiService.joinPackageInfo(userId, packageId)

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

    override fun getPickReceiptShelfDetail(requestDtos: List<GetPickReceiptShelfDetailRequestDto>) = apiService.getPickReceiptShelfDetail(requestDtos)

    override fun getAllFactory() = apiService.getAllFactory()

    override fun externalCheckPickReceiptConfirm(request: ExternalCheckPickReceiptConfirmDto) = apiService.externalCheckPickReceiptConfirm(request)

    override fun getPickingBarCodeInfo(userId: String, barCode: String) = apiService.getPickingBarCodeInfo(userId, barCode)

    override fun getCostCenterByUser(userId: String) = apiService.getCostCenterByUser(userId)

    override fun createPDAProduceAcquire(request: ProduceAcquireConfirmRequest) = apiService.createPDAProduceAcquire(request)

    override fun getPickReceiptInfoByCode(userId: String, barCode: String) = apiService.getPickReceiptInfoByCode(userId, barCode)

    override fun truckReceive(recvInfo: MobilePickReceiptRecvDto) = apiService.truckReceive(recvInfo)

    override fun getDeliveryNoteInfoByCode(userId: String, barCode: String) = apiService.getDeliveryNoteInfoByCode(userId, barCode)

    override fun getSlInfoForDeliveryNote(userId: String, noteCode: String) = apiService.getSlInfoForDeliveryNote(userId, noteCode)

    override fun normalNoteReceive(recvInfo: MobileNoteRecvRequest) = apiService.normalNoteReceive(recvInfo)

    //新增获取捡配单信息
    override fun getPickInfo(userId: String, barCode: String) = apiService.getPickInfo(userId, barCode)

    //新增不良品信息
    override fun getBadGoodsNews(userId: String, barCode: String, materialId: String, supplierId: String, batchNum: String) = apiService.getBadGoodsNews(userId, barCode, materialId, supplierId, batchNum)

    //新增更新不良品信息
    override fun operBadGoodsNews(userId: String, barCode: String, materialId: String, supplierId: String, batchNum: String, num: String, reason: String) = apiService.operBadGoodsNews(userId, barCode, materialId, supplierId, batchNum, num, reason)

    //新增删除不良品信息
    override fun delBadGoodsNews(userId: String, barCode: String, materialId: String, supplierId: String, batchNum: String) = apiService.delBadGoodsNews(userId, barCode, materialId, supplierId, batchNum)

    override fun checkStSlInfo(spCode: String, stCode: String) = apiService.checkStSlInfo(spCode, stCode)

    override fun checkPickStationInfo(pickCode: String, stationCode: String) = apiService.checkPickStationInfo(pickCode, stationCode)

    override fun getManualBoardLis(code: String, materialNum: String) = apiService.getManualBoardLis(code, materialNum)

    override fun saveManualBoardOut(manualBoardDeliveryDto: List<ManualBoardDeliveryDto>) = apiService.saveManualBoardOut(manualBoardDeliveryDto)

    override fun getStoragePositionInfoByCode(code: String) = apiService.getStoragePositionInfoByCode(code)

    override fun viewVehicleReceiptFilterInfo(userId: String) = apiService.viewVehicleReceiptFilterInfo(userId)

    override fun getVehicleReceiptByCode(code: String) = apiService.getVehicleReceiptByCode(code)

    override fun searchVehicleReceipt(dto: VehicleReceiptParamsDto) = apiService.searchVehicleReceipt(dto)

    override fun getMesPickReceiptListById(id: String) = apiService.getMesPickReceiptListById(id)

    override fun unpacking(unpackingDto: UnpackingDto) = apiService.unpacking(unpackingDto)

    override fun joinPackage(joinPackageDto: JoinPackageDto) = apiService.joinPackage(joinPackageDto)

    override fun addPackage(prNo: String, stCode: String) = apiService.addPackage(prNo, stCode)

    override fun deletePackage(dto: LESPackageInfoUpdateDto) = apiService.deletePackage(dto)

    override fun reliveryForLesDeliveryNote(userId: String, noteId: String)=apiService.reliveryForLesDeliveryNote(userId, noteId)
}