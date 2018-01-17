package com.zotye.wms.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by hechuangju on 2017/7/25.
 */
class ResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.isSuccessful) {
            if (response.body() != null) {
                return response
            } else
                throw IOException("Empty Response Body")
        } else
            throw IOException("Response Failed Code:${response.code()} ${response.message()}")
    }
}
