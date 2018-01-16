package com.zotye.wms.data.api

import com.zotye.wms.data.api.service.ApiService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hechuangju on 2017/09/18
 */
@Singleton
class AppApiHelper @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override fun doLoginCall(email: String, pwd: String) = apiService.doLoginCall(email, pwd)

}