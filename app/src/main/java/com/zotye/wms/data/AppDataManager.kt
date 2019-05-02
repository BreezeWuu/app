package com.zotye.wms.data

import android.content.Context
import com.zotye.wms.data.db.DbHelper
import com.zotye.wms.data.prefs.PreferencesHelper
import com.zotye.wms.data.api.ApiHelper
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.*
import com.zotye.wms.data.api.model.checkbad.ExternalCheckPickReceiptConfirmDto
import com.zotye.wms.data.api.model.checkbad.GetPickReceiptShelfDetailRequestDto
import com.zotye.wms.data.api.model.receipt.MobilePickReceiptRecvDto
import com.zotye.wms.data.api.model.picking.ProduceAcquireConfirmRequest
import com.zotye.wms.data.api.model.receipt.DeliveryNoteInfoResponse
import com.zotye.wms.data.api.model.under.shelf.PrMobileConfirmRequest
import com.zotye.wms.data.api.request.MobileNoteRecvRequest
import com.zotye.wms.data.api.response.ReceiveConfirmResponse
import com.zotye.wms.di.qualifier.ApplicationContext
import com.zotye.wms.util.FileUtil
import retrofit2.Call
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hechuangju on 2017/8/24 下午6:34.
 */
@Singleton
class AppDataManager @Inject constructor(@ApplicationContext val context: Context, private val dbHelper: DbHelper, private val preferencesHelper: PreferencesHelper, private val apiHelper: ApiHelper) : DataManager {

    override fun getAllFactory() = apiHelper.getAllFactory()

    override fun doLoginCall(userName: String, pwd: String, factoryCode: String) = apiHelper.doLoginCall(userName, pwd, factoryCode)

    override fun getUserInfo(userId: String) = apiHelper.getUserInfo(userId)

    override fun getPackageInfo(userId: String, isGroupReceive: Boolean, packageId: String) = apiHelper.getPackageInfo(userId, isGroupReceive, packageId)

    override fun getStorageUnitInfoByBarcode(userId: String, barCode: String) = apiHelper.getStorageUnitInfoByBarcode(userId, barCode)

    override fun getStorageUnitDetailInfoByCode(userId: String, barCode: String) = apiHelper.getStorageUnitDetailInfoByCode(userId, barCode)

    override fun authStorageUnitNewPositionByQRCode(userId: String, qrCode: String) = apiHelper.authStorageUnitNewPositionByQRCode(userId, qrCode)

    override fun storageUnitModify(userId: String, code: String, newStoragePositionCode: String) = apiHelper.storageUnitModify(userId, code, newStoragePositionCode)

    override fun logisticsReceive(logisticsReceiveJsonString: String) = apiHelper.logisticsReceive(logisticsReceiveJsonString)

    override fun logisticsReceiveConfirmInfoByCode(userId: String, barCode: String) = apiHelper.logisticsReceiveConfirmInfoByCode(userId, barCode)

    override fun logisticsReceiveConfirm(userId: String, noteId: String) = apiHelper.logisticsReceiveConfirm(userId, noteId)

    override fun getPickListInfoByCode(userId: String, OpterationType: String, barCode: String) = apiHelper.getPickListInfoByCode(userId, OpterationType, barCode)

    override fun getStorageUnitMaterialTotalNumber(userId: String, storageUnitInfoCode: String, spDetailId: String) = apiHelper.getStorageUnitMaterialTotalNumber(userId, storageUnitInfoCode, spDetailId)

    override fun underShelfConfirm(request: PrMobileConfirmRequest) = apiHelper.underShelfConfirm(request)

    override fun createLoadingList(userId: String, carNumber: String, pickListJson: String) = apiHelper.createLoadingList(userId, carNumber, pickListJson)

    override fun getPickReceiptShelfDetail(requestDtos: List<GetPickReceiptShelfDetailRequestDto>) = apiHelper.getPickReceiptShelfDetail(requestDtos)

    override fun externalCheckPickReceiptConfirm(request: ExternalCheckPickReceiptConfirmDto) = apiHelper.externalCheckPickReceiptConfirm(request)

    override fun getPickingBarCodeInfo(userId: String, barCode: String) = apiHelper.getPickingBarCodeInfo(userId, barCode)

    override fun createPDAProduceAcquire(request: ProduceAcquireConfirmRequest) = apiHelper.createPDAProduceAcquire(request)

    override fun getPickReceiptInfoByCode(userId: String, barCode: String) = apiHelper.getPickReceiptInfoByCode(userId, barCode)

    override fun truckReceive(recvInfo: MobilePickReceiptRecvDto) = apiHelper.truckReceive(recvInfo)

    override fun getDeliveryNoteInfoByCode(userId: String, barCode: String) = apiHelper.getDeliveryNoteInfoByCode(userId, barCode)

    override fun getSlInfoForDeliveryNote(userId: String, noteCode: String) = apiHelper.getSlInfoForDeliveryNote(userId, noteCode)

    override fun normalNoteReceive(recvInfo: MobileNoteRecvRequest) = apiHelper.normalNoteReceive(recvInfo)

    //新增捡配单信息
    override fun getPickInfo(userId: String, barCode: String) = apiHelper.getPickInfo(userId, barCode)

    //新增获取不良品信息
    override fun getBadGoodsNews(userId: String, barCode: String, materialId: String, supplierId: String, batchNum: String) = apiHelper.getBadGoodsNews(userId, barCode, materialId, supplierId, batchNum)

    //新增更新不良品信息
    override fun operBadGoodsNews(userId: String, barCode: String, materialId: String, supplierId: String, batchNum: String, num: String, reason: String) = apiHelper.operBadGoodsNews(userId, barCode, materialId, supplierId, batchNum, num, reason)

    //新增删除不良品信息
    override fun delBadGoodsNews(userId: String, barCode: String, materialId: String, supplierId: String, batchNum: String) = apiHelper.delBadGoodsNews(userId, barCode, materialId, supplierId, batchNum)

    override fun checkStSlInfo(spCode: String, stCode: String) = apiHelper.checkStSlInfo(spCode, stCode)

    override fun checkPickStationInfo(pickCode: String, stationCode: String) = apiHelper.checkPickStationInfo(pickCode, stationCode)

    override fun getManualBoardLis(code: String, materialNum: String) = apiHelper.getManualBoardLis(code, materialNum)

    override fun saveManualBoardOut(manualBoardDeliveryDto: List<ManualBoardDeliveryDto>) = apiHelper.saveManualBoardOut(manualBoardDeliveryDto)

    override fun getStoragePositionInfoByCode(code: String) = apiHelper.getStoragePositionInfoByCode(code)

    override fun viewVehicleReceiptFilterInfo(userId: String) = apiHelper.viewVehicleReceiptFilterInfo(userId)

    override fun getVehicleReceiptByCode(code: String) = apiHelper.getVehicleReceiptByCode(code)

    override fun searchVehicleReceipt(dto: VehicleReceiptParamsDto) = apiHelper.searchVehicleReceipt(dto)

    override fun getMesPickReceiptListById(id: String) = apiHelper.getMesPickReceiptListById(id)

    override fun unpacking(unpackingDto: UnpackingDto) = apiHelper.unpacking(unpackingDto)

    override fun getCostCenterByUser(userId: String) = apiHelper.getCostCenterByUser(userId)

    override fun setCurrentUserId(userId: String?) = preferencesHelper.setCurrentUserId(userId)

    override fun getCurrentUserId() = preferencesHelper.getCurrentUserId()

    override fun setDefaultFactoryCode(factoryCode: String?) = preferencesHelper.setDefaultFactoryCode(factoryCode)

    override fun getDefaultFactoryCode() = preferencesHelper.getDefaultFactoryCode()

    override fun setAccessToken(token: String) = preferencesHelper.setAccessToken(token)

    override fun getAccessToken() = preferencesHelper.getAccessToken()

    override fun insertUser(user: User) = dbHelper.insertUser(user)

    override fun getAllUsers() = dbHelper.getAllUsers()

    override fun getUser(userId: String) = dbHelper.getUser(userId)

    override fun updateUser(user: User) = dbHelper.updateUser(user)

    override fun getCurrentUser(): User? {
        getCurrentUserId()?.let {
            return getUser(getCurrentUserId()!!)
        }
        return null
    }

    override fun getCacheFileWithUrl(url: String): File {
        val fileName = FileUtil.getFileNameWithUrl(url)
        return File(context.getExternalFilesDir(null), fileName)
    }

    override fun getRootCacheFile(): File {
        return context.getExternalFilesDir(null)
    }

}