package com.zotye.wms.ui.common

import android.arch.lifecycle.LifecycleRegistry
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity : AppCompatActivity() {

    private val lifecycleRegistry: LifecycleRegistry
        get() = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    fun showMessage(resId: Int) {
        showMessage(getString(resId))
    }

    fun showMessage(message: String) {
        showSnackBar(message)
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(main_content, message, Snackbar.LENGTH_SHORT)
        val sbView = snackBar.view
        val textView = sbView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        snackBar.show()
    }
}