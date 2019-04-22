package com.zotye.wms.di.module

import com.zotye.wms.ui.goods.*
import com.zotye.wms.ui.main.MainFragment
import com.zotye.wms.ui.manualboard.ManualBoardOutFragment
import com.zotye.wms.ui.picking.ChooseCostCenterFragment
import com.zotye.wms.ui.picking.PickingFragment
import com.zotye.wms.ui.picklist.*
import com.zotye.wms.ui.storageunit.StorageUnitInfoFragment
import com.zotye.wms.ui.storageunit.StorageUnitModifyFragment
import com.zotye.wms.ui.storageunit.StorageUnitOnlineFragment
import com.zotye.wms.ui.user.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by hechuangju on 2017/7/17.
 */
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributeGroupReceiveFragment(): GroupReceiveFragment

    @ContributesAndroidInjector
    abstract fun contributeReceiveConfirmFragment(): ReceiveConfirmFragment

    @ContributesAndroidInjector
    abstract fun contributeStorageUnitModifyFragment(): StorageUnitModifyFragment

    @ContributesAndroidInjector
    abstract fun contributeStorageUnitInfoFragment(): StorageUnitInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeUnderShelfFragment(): UnderShelfFragment

    @ContributesAndroidInjector
    abstract fun contributeCheckBadProductFragment(): CheckBadProductFragment

    @ContributesAndroidInjector
    abstract fun contributeLoadingCreatFragment(): LoadingCreateFragment

    @ContributesAndroidInjector
    abstract fun contributePickingFragment(): PickingFragment

    @ContributesAndroidInjector
    abstract fun contributeChooseCostCenterFragment(): ChooseCostCenterFragment

    @ContributesAndroidInjector
    abstract fun contributeStrictReceiveFragment(): StrictReceiveFragment

    @ContributesAndroidInjector
    abstract fun contributeDeliveryNoteReceiveFragment(): DeliveryNoteReceiveFragment

    //新增
    @ContributesAndroidInjector
    abstract fun contributeOutBoundCheckFragment(): OutBoundCheckFragment

    @ContributesAndroidInjector
    abstract fun contributeStorageUnitOnlineFragment(): StorageUnitOnlineFragment

    @ContributesAndroidInjector
    abstract fun contributeShelfConfirmFragment(): ShelfConfirmFragment

    @ContributesAndroidInjector
    abstract fun contributeManualBoardOutFragment(): ManualBoardOutFragment

    @ContributesAndroidInjector
    abstract fun contributeStoragePositionInfoFragment(): StoragePositionInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeViewVehicleReceiptFragment(): ViewVehicleReceiptFragment
}