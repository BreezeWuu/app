package com.zotye.wms.ui.user.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.zotye.wms.R
import com.zotye.wms.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.layout_progress.view.*
import javax.inject.Inject


/**
 * Created by hechuangju on 2017/12/19
 */
class LoginFragment : BaseFragment(), LoginContract.LoginMvpView {

    companion object {
        val TAG = "LoginFragment"
    }

    @Inject lateinit var loginMvpPresenter: LoginContract.LoginMvpPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = getString(R.string.login)
//        toolbar_base.navigationIconResource = R.drawable.ic_action_back
        toolbar_base?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        loginMvpPresenter.onAttach(this)
        login_password.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    login()
                    return@setOnEditorActionListener true
                }
                else ->
                    return@setOnEditorActionListener false
            }
        }
        login_submit.setOnClickListener { login() }
//        registerAccount.onClick {
//            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.main_content, WebFragment.newInstance("http://www.upenglish.cn/Center/Register", getString(R.string.login_register_account), true))?.addToBackStack(null)?.commit()
//        }
//        forgotPassword.onClick {
//            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.main_content, WebFragment.newInstance("http://www.upenglish.cn/Center/ForgetPassword", getString(R.string.login_forget_password), true))?.addToBackStack(null)?.commit()
//        }
    }

    override fun getLoadView(): View = layout_login_loading

    override fun getLoadTextView(): TextView = layout_login_loading.text_loading

    override fun getContentView(): View = layout_login_content

    private fun login() {
        loginMvpPresenter.onLoginClick(login_username.text.toString(), login_password.text.toString())
    }

    override fun onDestroyView() {
        loginMvpPresenter.onDetach()
        super.onDestroyView()
    }

    override fun openMainFragment() {
        
    }

}