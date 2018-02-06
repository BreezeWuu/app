package com.zotye.wms.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.zotye.wms.di.qualifier.ApplicationContext
import com.zotye.wms.di.qualifier.PreferenceInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hechuangju on 2017/8/29 下午12:55.
 */
@Singleton
class AppPreferencesHelper @Inject constructor(@ApplicationContext context: Context, @PreferenceInfo prefsName: String) : PreferencesHelper {

    private var mPrefs: SharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    companion object {
        val PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID"
        val PREF_KEY_DEFAULT_FACTORY_CODE = "PREF_KEY_DEFAULT_FACTORY_CODE"
        val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
    }

    override fun setCurrentUserId(userId: String?) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_ID, userId).apply()
    }

    override fun getCurrentUserId(): String? {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_ID, null)
    }

    override fun setDefaultFactoryCode(factoryCode: String?) {
        mPrefs.edit().putString(PREF_KEY_DEFAULT_FACTORY_CODE, factoryCode).apply()
    }

    override fun getDefaultFactoryCode(): String? {
        return mPrefs.getString(PREF_KEY_DEFAULT_FACTORY_CODE, "")
    }

    override fun setAccessToken(token: String) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, token).apply()
    }

    override fun getAccessToken(): String? = mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null)
}