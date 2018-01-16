package com.zotye.wms

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.zotye.wms.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject
import android.support.multidex.MultiDex

/**
 * Created by hechuangju on 2017/8/20 下午10:11.
 */
class WmsApp : MultiDexApplication(), HasActivityInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    companion object {
        init {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        Stetho.initializeWithDefaults(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}