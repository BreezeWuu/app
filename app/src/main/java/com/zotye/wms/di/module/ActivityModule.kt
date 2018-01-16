package com.zotye.wms.di.module

import com.zotye.wms.ui.common.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by hechuangju on 2017/7/17.
 */
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = arrayOf(FragmentModule::class))
    abstract fun contributeMainActivity(): MainActivity
}