package com.zotye.wms.di.module

import com.zotye.wms.ui.goods.receive.GroupReceiveContract
import com.zotye.wms.ui.main.MainContract
import com.zotye.wms.ui.user.login.LoginContract
import dagger.Module
import dagger.Provides

/**
 * Created by hechuangju on 2017/9/1 下午6:14.
 */
@Module
class PresenterModule {

    @Provides
    fun provideLoginPresenter(presenter: LoginContract.LoginPresenterImpl): LoginContract.LoginMvpPresenter = presenter

    @Provides
    fun provideMainPresenter(presenter: MainContract.MainPresenterImpl): MainContract.MainMvpPresenter = presenter

    @Provides
    fun provideGroupReceivePresenter(presenter: GroupReceiveContract.GroupReceivePresenterImpl): GroupReceiveContract.GroupReceivePresenter = presenter
//
//    @Provides
//    fun provideSortPresenter(presenter: SortContract.SortPresenterImpl): SortContract.SortPresenter = presenter
//
//    @Provides
//    fun provideUserInfoPresenter(presenter: UserProfileContract.UserProfilePresenterImpl): UserProfileContract.UserProfilePresenter = presenter
}