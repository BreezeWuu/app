package com.zotye.wms.util

import android.util.Log
import com.zotye.wms.BuildConfig
import java.util.*

/**
 * Created by hechuangju on 2017/7/5 下午1:09.
 */

object Log {
    private val TAG = "HeChuangJu->"
    private val MATCH = "methodName:[%s]->lineNumber:[%d]->"

    private val SWITCH = BuildConfig.DEBUG

    private fun buildHeader(): String {
        val stack = Thread.currentThread().stackTrace[4]
        return if (stack == null)
            "UNKNOWN"
        else
            String.format(Locale.getDefault(), MATCH, stack.methodName, stack.lineNumber)
    }

    fun v(msg: Any) {
        if (SWITCH) Log.v(TAG, buildHeader() + msg.toString())
    }

    fun v(tag: String, msg: Any) {
        if (SWITCH) Log.v(TAG + tag, buildHeader() + msg.toString())
    }

    fun d(msg: Any) {
        if (SWITCH) Log.d(TAG, buildHeader() + msg.toString())
    }

    fun d(tag: String, msg: Any) {
        if (SWITCH) Log.d(TAG + tag, buildHeader() + msg.toString())
    }

    fun i(msg: Any) {
        if (SWITCH) Log.i(TAG, buildHeader() + msg.toString())
    }

    fun i(tag: String, msg: Any) {
        if (SWITCH) Log.i(TAG + tag, buildHeader() + msg.toString())
    }

    fun w(msg: Any) {
        if (SWITCH) Log.w(TAG, buildHeader() + msg.toString())
    }

    fun w(tag: String, msg: Any) {
        if (SWITCH) Log.w(TAG + tag, buildHeader() + msg.toString())
    }

    fun e(msg: Any) {
        if (SWITCH) Log.e(TAG, buildHeader() + msg.toString())
    }

    fun e(tag: String, msg: Any) {
        if (SWITCH) Log.e(TAG + tag, buildHeader() + msg.toString())
    }
}
