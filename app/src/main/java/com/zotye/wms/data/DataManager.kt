package com.zotye.wms.data

import com.zotye.wms.data.db.DbHelper
import com.zotye.wms.data.prefs.PreferencesHelper
import com.zotye.wms.data.api.model.User
import com.zotye.wms.data.api.ApiHelper
import java.io.File

/**
 * Created by hechuangju on 2017/8/24 上午10:50.
 */
interface DataManager : DbHelper, PreferencesHelper, ApiHelper {
    fun getCurrentUser(): User?

    fun getCacheFileWithUrl(url: String):File

    fun getRootCacheFile():File
}