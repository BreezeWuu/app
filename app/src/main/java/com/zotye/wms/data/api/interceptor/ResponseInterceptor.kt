package com.zotye.wms.data.api.interceptor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zotye.wms.AppConstants.ACTION_APP_VERSION
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.AppVersion
import com.zotye.wms.di.qualifier.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by hechuangju on 2017/7/25.
 */
@Singleton
class ResponseInterceptor @Inject constructor(@ApplicationContext private var context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.isSuccessful) {
            if (response.body() != null) {
                val builder: Response.Builder = response.newBuilder()
                val responseBody: ResponseBody = response.body() as ResponseBody
                var responseString = responseBody.string()
                try{
                    val retCode = JSONObject(responseString).getInt("retCode")
                    if (retCode == -777) {
                        val type = object : TypeToken<ApiResponse<AppVersion>>() {}.type
                        val appVersion = Gson().fromJson<ApiResponse<AppVersion>>(responseString, type).data!!
                        val intent = Intent(ACTION_APP_VERSION)
                        val bundle = Bundle()
                        bundle.putSerializable("appVersion", appVersion)
                        intent.putExtras(bundle)
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                    }
                    builder.body(ResponseBody.create(responseBody.contentType(), responseString))
                    return builder.build()
                }catch (e:Exception){
                    throw IOException("Error Response Body")
                }
            } else
                throw IOException("Empty Response Body")
        } else
            throw IOException("Response Failed Code:${response.code()} ${response.message()}")
    }
}
