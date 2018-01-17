package com.zotye.wms.ui.common

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.zotye.wms.R
import com.zotye.wms.data.DataManager
import com.zotye.wms.ui.main.MainFragment
import com.zotye.wms.ui.user.login.LoginFragment
import com.zotye.wms.util.Log
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity : BaseActivity(), HasSupportFragmentInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var dataManager: DataManager
    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handlerIntent(intent, savedInstanceState)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handlerIntent(intent, null)
    }

    private fun handlerIntent(newIntent: Intent, savedInstanceState: Bundle?) {
        Log.i("handlerIntent-->newIntent:$newIntent savedInstanceState:$savedInstanceState")
        if (dataManager.getCurrentUserId() == null) {
            val loginFragment: Fragment = supportFragmentManager.findFragmentByTag(LoginFragment.TAG) ?: LoginFragment()
            supportFragmentManager.beginTransaction().replace(R.id.main_content, loginFragment, LoginFragment.TAG).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.main_content, MainFragment()).commit()
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val peekFragment = supportFragmentManager.findFragmentById(R.id.main_content)
        var canBack = true
        if (peekFragment is BaseFragment)
            canBack = peekFragment.canBackPressed()
        if (canBack && !fragmentManager.popBackStackImmediate()) {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.main_content)
            if (currentFragment is BaseFragment) {
                if (!currentFragment.canBackPressed()) return
            }
            if (backPressed + 2000 <= System.currentTimeMillis()) {
                showMessage(R.string.app_exit)
                backPressed = System.currentTimeMillis()
                return
            }
            super.onBackPressed()
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
