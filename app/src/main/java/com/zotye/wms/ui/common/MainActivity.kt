package com.zotye.wms.ui.common

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.view.KeyEvent
import com.zotye.wms.AppConstants
import com.zotye.wms.BuildConfig
import com.zotye.wms.R
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.model.AppVersion
import com.zotye.wms.ui.main.MainFragment
import com.zotye.wms.ui.user.LoginFragment
import com.zotye.wms.util.Log
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import org.jetbrains.anko.doAsync
import java.io.File
import javax.inject.Inject


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

    fun handlerIntent(newIntent: Intent, savedInstanceState: Bundle?) {
        Log.i("handlerIntent-->newIntent:$newIntent savedInstanceState:$savedInstanceState")
        if (dataManager.getCurrentUserId() == null) {
            supportFragmentManager.beginTransaction().replace(R.id.main_content, LoginFragment()).commit()
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
                            val apkDownloadUrl = appVersion.address
                            mDownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "${BuildConfig.APPLICATION_ID}.apk")
                            doAsync {
                                if (file.exists()) {
                                    file.delete()
                                }
                                runOnUiThread {
                                    val request = DownloadManager.Request(Uri.parse(apkDownloadUrl))
                                    request.setDestinationUri(Uri.fromFile(file))
                                    // 获取下载队列 id
                                    enqueueId = mDownloadManager?.enqueue(request) ?: -1
                                }
                            }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val apkUri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.fileProvider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        val fragment  = supportFragmentManager.findFragmentById(R.id.main_content)
        if(fragment is BarCodeScannerFragment){
            fragment.onKeyUp(keyCode, event)
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val fragment  = supportFragmentManager.findFragmentById(R.id.main_content)
        if(fragment is BarCodeScannerFragment){
            fragment.onKeyDown(keyCode, event)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}
