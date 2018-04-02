package com.zotye.wms.data.api.interceptor

import android.content.Context
import com.zotye.wms.BuildConfig
import com.zotye.wms.data.prefs.PreferencesHelper
import com.zotye.wms.di.qualifier.ApplicationContext
import com.zotye.wms.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hechuangju on 2017/7/25.
 */
@Singleton
class HeaderInterceptor @Inject constructor(@ApplicationContext private var context: Context, private val preferencesHelper: PreferencesHelper) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        Log.e(context.toString())
        request = request.newBuilder()
                .addHeader("packageName", context.packageName)
                .addHeader("versionName", BuildConfig.VERSION_NAME)
                .addHeader("versionCode", "${BuildConfig.VERSION_CODE}")
                .addHeader("Agent", BuildConfig.OS)
                .addHeader("Accept-Language", "zh-CN")
                .addHeader("Channel", BuildConfig.FLAVOR)
                .addHeader("factoryCode", preferencesHelper.getDefaultFactoryCode() ?: "")
                .build()
        return chain.proceed(request)
    }
}