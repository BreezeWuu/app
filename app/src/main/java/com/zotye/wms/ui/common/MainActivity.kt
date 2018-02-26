package com.zotye.wms.ui.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.zotye.wms.AppConstants
import com.zotye.wms.R
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.model.AppVersion
import com.zotye.wms.ui.main.MainFragment
import com.zotye.wms.ui.user.LoginFragment
import com.zotye.wms.util.Log
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import android.app.DownloadManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import com.zotye.wms.BuildConfig
import java.io.File
import java.net.URI
import android.support.v4.content.ContextCompat.startActivity
import org.jetbrains.anko.custom.onUiThread
import org.jetbrains.anko.doAsync


class MainActivity : BaseActivity(), HasSupportFragmentInjector {
    private var mDownloadManager: DownloadManager? = null
    private var enqueueId: Long = 0
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var dataManager: DataManager
    private var backPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val filter = IntentFilter()
        filter.addAction(AppConstants.ACTION_APP_VERSION)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        handlerIntent(intent, savedInstanceState)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handlerIntent(intent, null)
    }

    public fun handlerIntent(newIntent: Intent, savedInstanceState: Bundle?) {
        Log.i("handlerIntent-->newIntent:$newIntent savedInstanceState:$savedInstanceState")
        if (dataManager.getCurrentUserId() == null) {
            val loginFragment: Fragment = supportFragmentManager.findFragmentByTag(LoginFragment.TAG)
                    ?: LoginFragment()
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

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (AppConstants.ACTION_APP_VERSION == intent.action) {
                val appVersion = intent.extras.getSerializable("appVersion") as AppVersion
                val dialog = AlertDialog.Builder(this@MainActivity).setTitle(R.string.new_version).setMessage(appVersion.versionDesc)
                        .setNegativeButton(R.string.ok) { _, _ ->
                            val apkDownloadUrl = "http://assets.sfcdn.org/pub/com.shafa.market/296/fe18a6c/com.shafa.market_5.0.4_webmarket.apk?_upd=%E6%B2%99%E5%8F%91%E7%AE%A1%E5%AE%B6V5.0_v5.0.4_webmarket.apk"
                            mDownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "${BuildConfig.APPLICATION_ID}.apk")
                            if (file.exists()) {
                                file.delete()
                            }
                            val request = DownloadManager.Request(Uri.parse(apkDownloadUrl))
                            request.setDestinationUri(Uri.fromFile(file))
                            // 获取下载队列 id
                            enqueueId = mDownloadManager?.enqueue(request) ?: -1
                        }.setPositiveButton(R.string.cancel) { _, _ ->
                            finish()
                        }.create()
                dialog.setCanceledOnTouchOutside(false)
                dialog.setCancelable(false)
                dialog.show()
            }
        }
    }
    private var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val downloadCompletedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
            // 检查是否是自己的下载队列 id, 有可能是其他应用的
            if (enqueueId != downloadCompletedId) {
                return
            }
            val query = DownloadManager.Query()
            query.setFilterById(enqueueId)
            val c = mDownloadManager?.query(query)
            c?.let {
                if (c.moveToFirst()) {
                    val columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    // 下载失败也会返回这个广播，所以要判断下是否真的下载成功
                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                        // 提示用户安装
                        promptInstall(applicationContext)
                    }
                }
            }
        }
    }

    private fun promptInstall(context: Context) {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "${BuildConfig.APPLICATION_ID}.apk")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            val apkUri = FileProvider.getUriForFile(context, "com.zotye.wms.fileProvider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}
