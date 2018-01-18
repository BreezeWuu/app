package com.zotye.wms.ui.common

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.zotye.wms.R
import com.zotye.wms.di.Injectable
import com.zotye.wms.util.Log
import com.zotye.wms.util.NetworkUtils
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_base.view.*
import kotlinx.android.synthetic.main.layout_error.view.*
import kotlinx.android.synthetic.main.layout_progress.view.*

/**
 * Created by hechuangju on 2017/6/30.
 */
abstract class BaseFragment : Fragment(), MvpView, Injectable {

    open fun canBackPressed(): Boolean {
        return true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d("{${this.javaClass.simpleName}:0x${Integer.toHexString(this.id)}}", "onAttach:$context")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("{${this.javaClass.simpleName}:0x${Integer.toHexString(this.id)}}", "onCreate:$savedInstanceState")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("{${this.javaClass.simpleName}:0x${Integer.toHexString(this.id)}}", "onCreateView:$savedInstanceState")
        val view = inflater.inflate(R.layout.fragment_base, container, false)
        val contentView = onCreateContentView(inflater, view.layout_content, savedInstanceState)
        contentView?.let {
            view.layout_content.addView(contentView)
        }
        return view
    }

    abstract fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("{${this.javaClass.simpleName}:0x${Integer.toHexString(this.id)}}", "onViewCreated:$savedInstanceState")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("{${this.javaClass.simpleName}:0x${Integer.toHexString(this.id)}}", "onActivityCreated:$savedInstanceState")
    }

    override fun onResume() {
        super.onResume()
        Log.d("{${this.javaClass.simpleName}:0x${Integer.toHexString(this.id)}}", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("{${this.javaClass.simpleName}:0x${Integer.toHexString(this.id)}}", "onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        Log.v("{${this.javaClass.simpleName}:0x${Integer.toHexString(this.id)}}", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("{${this.javaClass.simpleName}:0x${Integer.toHexString(this.id)}}", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.v("{${this.javaClass.simpleName}:0x${Integer.toHexString(this.id)}}", "onDetach")
    }

    open fun getLoadView(): View = layout_progress
    open fun getLoadTextView(): TextView = layout_progress.text_loading

    override fun showLoading() {
        showLoading(null)
    }

    override fun showLoading(resId: Int) {
        showLoading(getString(resId))
    }

    override fun showLoading(message: String?) {
        getLoadView().visibility = View.VISIBLE
        getLoadTextView().visibility = if (message != null) View.VISIBLE else View.GONE
        message?.let { getLoadTextView().text = it }
        getLoadView().bringToFront()
    }

    open fun getErrorView(): View = layout_error

    open fun getErrorTextView(): TextView = layout_error.text_error

    override fun showError() {
        showError(null)
    }

    override fun showError(resId: Int) {
        showError(getString(resId))
    }

    override fun showError(message: String?) {
        getErrorView().visibility = View.VISIBLE
        getErrorTextView().visibility = if (message != null) View.VISIBLE else View.GONE
        message?.let { getErrorTextView().text = it }
        getErrorView().bringToFront()
    }

    open fun getContentView(): View = layout_content

    override fun showContent() {
        getContentView().visibility = View.VISIBLE
        getContentView().bringToFront()
    }

    override fun openActivityOnTokenExpire() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(resId: Int) {
        showMessage(getString(resId))
    }

    override fun showMessage(message: String) {
        showSnackBar(message)
    }

    override fun isNetworkConnected(): Boolean {
        activity?.applicationContext?.let {
            return NetworkUtils.isNetworkConnected(it)
        }
        return false
    }

    override fun showKeyboard() {
        activity?.let {
            val imm = it.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            it.currentFocus?.let {
                imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        }
    }

    override fun hideKeyboard() {
        activity?.let {
            val imm = it.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            it.currentFocus?.let {
                imm?.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    private fun showSnackBar(message: String) {
        val rootView = activity?.window?.decorView?.findViewById<View>(android.R.id.content)
        rootView?.let {
            val snackBar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            val sbView = snackBar.view
            val textView = sbView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
            sbView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.google))
            textView.setTextColor(ContextCompat.getColor(it.context, android.R.color.white))
            snackBar.show()
        }
    }
}