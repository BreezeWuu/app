package com.zotye.wms.di.module

import com.zotye.wms.ui.goods.GroupReceiveFragment
import com.zotye.wms.ui.goods.ReceiveConfirmFragment
import com.zotye.wms.ui.main.MainFragment
import com.zotye.wms.ui.picklist.CheckBadProductFragment
import com.zotye.wms.ui.picklist.UnderShelfFragment
import com.zotye.wms.ui.storageunit.StorageUnitInfoFragment
import com.zotye.wms.ui.storageunit.StorageUnitModifyFragment
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
}