package com.zotye.wms.data

import android.content.Context
import com.zotye.wms.data.api.model.User
import com.zotye.wms.data.db.DbHelper
import com.zotye.wms.data.prefs.PreferencesHelper
import com.zotye.wms.data.api.ApiHelper
import com.zotye.wms.data.api.ApiResponse
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

    override fun doLoginCall(userName: String, pwd: String) = apiHelper.doLoginCall(userName, pwd)

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

    override fun createLoadingList(userId: String, carNumber: String, pickListJson: String) = apiHelper.createLoadingList(userId, carNumber, pickListJson)

    override fun setCurrentUserId(userId: String?) = preferencesHelper.setCurrentUserId(userId)

    override fun getCurrentUserId() = preferencesHelper.getCurrentUserId()

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