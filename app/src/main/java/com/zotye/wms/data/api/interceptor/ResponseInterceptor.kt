//package com.zotye.wms.data.api.interceptor
//
//import com.zotye.wms.data.api.ApiResponse
//import com.zotye.wms.util.GZipUtil
//import com.zotye.wms.util.Log
//import okhttp3.Interceptor
//import okhttp3.Response
//import okhttp3.ResponseBody
//import org.simpleframework.xml.core.Persister
//import org.simpleframework.xml.core.ValueRequiredException
//import java.io.IOException
//import java.nio.charset.Charset
//
//
///**
// * Created by hechuangju on 2017/7/25.
// */
//class ResponseInterceptor : Interceptor {
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        val response = chain.proceed(request)
//        if (response.isSuccessful) {
//            if (response.body() != null) {
//                val builder: Response.Builder = response.newBuilder()
//                val responseBody: ResponseBody = response.body() as ResponseBody
//                var responseString = if (responseBody.contentType().toString().contains("charset")) responseBody.string() else responseBody.source().readString(Charset.forName("gb2312"))
//                try {
//                    val apiResponse = Persister().read(ApiResponse.ApiResponse::class.java, responseString)
//                    responseString = if (responseString.contains("base64Binary", false)) {
//                        GZipUtil.decompress(apiResponse.body!!)
//                    } else {
//                        apiResponse.body
//                    }
//                    Log.i(responseString)
//                    builder.body(ResponseBody.create(responseBody.contentType(), responseString))
//                    return builder.build()
//                } catch (e: Exception) {
//                    Log.i(responseString)
//                    builder.body(ResponseBody.create(responseBody.contentType(), responseString))
//                    return builder.build()
//                } catch (ex: ValueRequiredException) {
//                    ex.message?.let { Log.e(it) }
//                    throw IOException(ex.message)
//                }
//            } else
//                throw IOException("Empty Response Body")
//        } else
//            throw IOException("Response Failed Code:${response.code()} ${response.message()}")
//    }
//}
