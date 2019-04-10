package com.zotye.wms.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.util.Log
import com.zotye.wms.WmsApp
import com.zotye.wms.di.component.DaggerAppComponent
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

/**
 * Created by hechuangju on 2017/7/17.
 */
class AppInjector {
    companion object {
        fun init(app: WmsApp) {
            DaggerAppComponent.builder().application(app).build().inject(app)
            app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityPaused(p0: Activity?) {
                }

                override fun onActivityResumed(p0: Activity?) {
                }

                override fun onActivityStarted(p0: Activity?) {
                }

                override fun onActivityDestroyed(p0: Activity?) {
                }

                override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
                }

                override fun onActivityStopped(activity: Activity?) {
                }

                override fun onActivityCreated(activity: Activity?, p1: Bundle?) {
                    if (activity != null) handleActivity(activity)
                }
            })
        }

        fun handleActivity(activity: Activity) {
            if (activity is HasSupportFragmentInjector) {
                AndroidInjection.inject(activity)
            }
            (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                            if (f is Injectable) {
                                try {
                                    AndroidSupportInjection.inject(f)
                                } catch (e: Exception) {
                                    Log.e("AppInjector", e.message)
                                }
                            }
                        }
                    }, true)
        }
    }
}