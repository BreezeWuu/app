package com.zotye.wms.data.prefs

/**
 * Created by hechuangju on 2017/8/29 下午12:54.
 */
interface PreferencesHelper {

    fun setCurrentUserId(userId: String?)

    fun getCurrentUserId(): String?

    fun setAccessToken(token: String)

    fun getAccessToken(): String?

}