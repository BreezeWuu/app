package com.zotye.wms.data.api.interceptor

import android.content.Context
import com.zotye.wms.BuildConfig
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
class HeaderInterceptor @Inject constructor(@ApplicationContext private var context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        Log.e(context.toString())
        request = request.newBuilder()
                .addHeader("Version-Name", BuildConfig.VERSION_NAME)
                .addHeader("Version-Code", "${BuildConfig.VERSION_CODE}")
                .addHeader("Agent", "Android")
                .addHeader("Accept-Language", "zh-CN")
                .addHeader("Channel", BuildConfig.FLAVOR)
                .build()
        return chain.proceed(request)
    }
}