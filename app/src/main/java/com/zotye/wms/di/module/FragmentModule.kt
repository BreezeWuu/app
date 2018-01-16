package com.zotye.wms.di.module

import com.zotye.wms.ui.common.MainFragment
import com.zotye.wms.ui.user.login.LoginFragment
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

}