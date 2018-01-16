package com.zotye.wms.ui.common

import android.support.annotation.StringRes

/**
 * Created by hechuangju on 2017/8/23 上午11:09.
 */
interface MvpView {

    fun showLoading()

    fun showLoading(@StringRes resId: Int)

    fun showLoading(message: String?)

    fun showContent()

    fun showError()

    fun showError(@StringRes resId: Int)

    fun showError(message: String?)

    fun openActivityOnTokenExpire()

    fun showMessage(message: String)

    fun showMessage(@StringRes resId: Int)

    fun isNetworkConnected(): Boolean

    fun hideKeyboard()
}