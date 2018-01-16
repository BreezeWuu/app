package com.zotye.wms.ui.common

/**
 * Created by hechuangju on 2017/8/23 上午11:25.
 */
open class BasePresenter<V : MvpView> : MvpPresenter<V> {

    var mvpView: V? = null

    override fun onAttach(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun onDetach() {
        this.mvpView = null
    }

    fun isViewAttached(): Boolean {
        return this.mvpView != null
    }

    override fun setUserAsLoggedOut() {
    }
}