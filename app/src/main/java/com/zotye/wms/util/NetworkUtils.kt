package com.zotye.wms.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by hechuangju on 2017/8/24 下午12:26.
 */
object NetworkUtils {
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}