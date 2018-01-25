package com.zotye.wms.di.module

import com.zotye.wms.ui.goods.GroupReceiveContract
import com.zotye.wms.ui.goods.ReceiveConfirmContract
import com.zotye.wms.ui.main.MainContract
import com.zotye.wms.ui.picklist.CheckBadProductContract
import com.zotye.wms.ui.picklist.LoadingCreateContract
import com.zotye.wms.ui.picklist.UnderShelfContract
import com.zotye.wms.ui.storageunit.StorageUnitInfoContract
import com.zotye.wms.ui.storageunit.StorageUnitModifyContract
import com.zotye.wms.ui.user.LoginContract
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

    @Provides
    fun provideUnderShelfPresenter(presenter: UnderShelfContract.UnderShelfPresenterImpl): UnderShelfContract.UnderShelfPresenter = presenter

    @Provides
    fun provideCheckBadProductPresenter(presenter: CheckBadProductContract.CheckBadProductPresenterImpl): CheckBadProductContract.CheckBadProductPresenter = presenter

    @Provides
    fun provideCheckLoadingCreatePresenter(presenter: LoadingCreateContract.LoadingCreatePresenterImpl): LoadingCreateContract.LoadingCreatePresenter = presenter

}