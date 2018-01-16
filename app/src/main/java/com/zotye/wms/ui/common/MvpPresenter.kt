package com.zotye.wms.ui.common

/**
 * Created by hechuangju on 2017/8/23 上午11:10.
 */
interface MvpPresenter<in V : MvpView> {

    fun onAttach(mvpView: V)

    fun onDetach()

    fun setUserAsLoggedOut()
}