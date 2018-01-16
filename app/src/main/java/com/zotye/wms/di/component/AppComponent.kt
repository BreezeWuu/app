package com.zotye.wms.di.component

import android.app.Application
import com.zotye.wms.WmsApp
import com.zotye.wms.di.module.ActivityModule
import com.zotye.wms.di.module.AppModule
import com.zotye.wms.di.module.PresenterModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by hechuangju on 2017/7/7.
 */

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, AppModule::class, ActivityModule::class, PresenterModule::class))
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: WmsApp)
}