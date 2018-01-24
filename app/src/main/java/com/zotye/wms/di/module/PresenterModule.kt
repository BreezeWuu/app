package com.zotye.wms.di.module

import com.zotye.wms.ui.goods.receive.GroupReceiveContract
import com.zotye.wms.ui.goods.receive.ReceiveConfirmContract
import com.zotye.wms.ui.main.MainContract
import com.zotye.wms.ui.storageunit.modify.StorageUnitInfoContract
import com.zotye.wms.ui.storageunit.modify.StorageUnitModifyContract
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

    @Provides
    fun provideStorageUnitModifyPresenter(presenter: StorageUnitModifyContract.StorageUnitModifyPresenterImpl): StorageUnitModifyContract.StorageUnitModifyPresenter = presenter

    @Provides
    fun provideStorageUnitInfoPresenter(presenter: StorageUnitInfoContract.StorageUnitInfoPresenterImpl): StorageUnitInfoContract.StorageUnitInfoPresenter = presenter

    @Provides
    fun provideReceiveConfirmPresenter(presenter: ReceiveConfirmContract.ReceiveConfirmPresenterImpl): ReceiveConfirmContract.ReceiveConfirmPresenter = presenter

}