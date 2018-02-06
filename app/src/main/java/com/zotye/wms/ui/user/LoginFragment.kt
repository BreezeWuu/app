package com.zotye.wms.ui.user

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.zotye.wms.R
import com.zotye.wms.data.api.model.FactoryInfo
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.main.MainFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.layout_progress.view.*
import javax.inject.Inject
import android.widget.ArrayAdapter


/**
 * Created by hechuangju on 2017/12/19
 */
class LoginFragment : BaseFragment(), LoginContract.LoginMvpView {

    companion object {
        val TAG = "LoginFragment"
    }

    @Inject
    lateinit var loginMvpPresenter: LoginContract.LoginMvpPresenter
    var factoryInfoList: List<FactoryInfo>? = null


    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = getString(R.string.login)
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
        loginMvpPresenter.getAllFactory()
    }

    override fun getLoadView(): View = layout_login_loading

    override fun getLoadTextView(): TextView = layout_login_loading.text_loading

    override fun getContentView(): View = layout_login_content

    private fun login() {
        factoryInfoList?.let {
            hideKeyboard(login_password)
            loginMvpPresenter.onLoginClick(login_username.text.toString(), login_password.text.toString(), it[factorySpinner.selectedItemPosition].factoryCode!!)
        }
    }

    override fun onDestroyView() {
        loginMvpPresenter.onDetach()
        super.onDestroyView()
    }

    override fun openMainFragment() {
        fragmentManager?.beginTransaction()?.remove(this)?.replace(R.id.main_content, MainFragment())?.commit()
    }

    override fun getFactoryFailed() {
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(R.string.error_get_factory).setNegativeButton(R.string.ok) { _, _ ->
            loginMvpPresenter.getAllFactory()
        }.setPositiveButton(R.string.cancel) { _, _ ->
                    activity?.finish()
                }.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getAllFactory(factoryInfoList: List<FactoryInfo>) {
        this.factoryInfoList = factoryInfoList
        val items = ArrayList<String>()
        factoryInfoList.forEach {
            items.add(it.factoryName!!)
        }
        val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items)
        factorySpinner.adapter = arrayAdapter
    }
}