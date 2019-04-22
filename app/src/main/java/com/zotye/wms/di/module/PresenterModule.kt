package com.zotye.wms.di.module

import com.zotye.wms.ui.goods.*
import com.zotye.wms.ui.main.MainContract
import com.zotye.wms.ui.manualboard.ManualBoardOutContract
import com.zotye.wms.ui.picking.ChooseCostCenterContract
import com.zotye.wms.ui.picking.PickingContract
import com.zotye.wms.ui.picklist.*
import com.zotye.wms.ui.storageunit.StorageUnitInfoContract
import com.zotye.wms.ui.storageunit.StorageUnitModifyContract
import com.zotye.wms.ui.storageunit.StorageUnitOnlineContract
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

    @Provides
    fun providePickingPresenter(presenter: PickingContract.PickingPresenterImpl): PickingContract.PickingPresenter = presenter

    @Provides
    fun provideChooseCostCenterPresenter(presenter: ChooseCostCenterContract.ChooseCostCenterPresenterImpl): ChooseCostCenterContract.ChooseCostCenterPresenter = presenter

    @Provides
    fun provideStrictReceivePresenter(presenter: StrictReceiveContract.StrictReceivePresenterImpl): StrictReceiveContract.StrictReceivePresenter = presenter

    @Provides
    fun provideDeliveryNoteReceivePresenter(presenter: DeliveryNoteReceiveContract.DeliveryNoteReceivePresenterImpl): DeliveryNoteReceiveContract.DeliveryNoteReceivePresenter = presenter

    //新增Presenter
    @Provides
    fun provideOutBoundCheckPresenter(presenter: OutBoundCheckContract.OutBoundCheckPresenterImpl): OutBoundCheckContract.OutBoundCheckPresenter = presenter

    @Provides
    fun provideStorageUnitOnlinePresenter(presenter: StorageUnitOnlineContract.StorageUnitOnlinePresenterImpl): StorageUnitOnlineContract.StorageUnitOnlinePresenter = presenter

    @Provides
    fun provideShelfConfirmPresenter(presenter: ShelfConfirmContract.ShelfConfirmPresenterImpl): ShelfConfirmContract.ShelfConfirmPresenter = presenter

    @Provides
    fun provideManualBoardOutPresenter(presenter: ManualBoardOutContract.ManualBoardOutPresenterImpl): ManualBoardOutContract.ManualBoardOutPresenter = presenter

    @Provides
    fun provideStoragePositionInfoPresenter(presenter: StoragePositionInfoContract.StoragePositionInfoPresenterImpl): StoragePositionInfoContract.StoragePositionInfoPresenter = presenter

    @Provides
    fun provideViewVehicleReceiptPresenter(presenter: ViewVehicleReceiptContract.ViewVehicleReceiptPresenterImpl): ViewVehicleReceiptContract.ViewVehicleReceiptPresenter = presenter

}