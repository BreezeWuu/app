package com.zotye.wms.ui.user

import android.text.TextUtils
import com.zotye.wms.AppConstants
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.FactoryInfo
import com.zotye.wms.data.api.model.User
import com.zotye.wms.data.api.model.checkbad.PickReceiptShelfDetail
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import com.zotye.wms.util.MD5Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2017/8/23 上午11:14.
 */
object LoginContract {
    interface LoginMvpView : MvpView {
        fun openMainFragment()
        fun getAllFactory(factoryInfoList: List<FactoryInfo>)
        fun getFactoryFailed()
    }

    interface LoginMvpPresenter : MvpPresenter<LoginMvpView> {
        fun getAllFactory()

        fun onLoginClick(userName: String, password: String, factoryCode: String)

        fun getDataManager(): DataManager

        fun getAppExecutors(): AppExecutors
    }


    class LoginPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<LoginMvpView>(), LoginMvpPresenter {
        override fun onLoginClick(userName: String, password: String, factoryCode: String) {
            if (TextUtils.isEmpty(userName)) {
                mvpView?.showContent()
                mvpView?.showMessage(R.string.login_name_empty_error)
                return@onLoginClick
            }
            if (TextUtils.isEmpty(password)) {
                mvpView?.showContent()
                mvpView?.showMessage(R.string.login_name_password_error)
                return@onLoginClick
            }
            mvpView?.showLoading(R.string.login_loading)
            dataManager.doLoginCall(userName, MD5Util.encryptwithSalt(password, AppConstants.MD5Salt), factoryCode).enqueue(object : Callback<ApiResponse<User>> {
                override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                    mvpView?.showContent()
                    t.message?.let { mvpView?.showMessage(it) }
                }

                override fun onResponse(call: Call<ApiResponse<User>>, response: Response<ApiResponse<User>>) {
                    response.body()?.let {
                        if (it.isSucceed()) {
                            it.data?.let { user ->
                                appExecutors.diskIO().execute {
                                    if (dataManager.insertUser(user) > 0) {
                                        appExecutors.mainThread().execute {
                                            dataManager.setCurrentUserId(user.userId)
                                            dataManager.setDefaultFactoryCode(user.defaultFactoryCode)
                                            mvpView?.showContent()
                                            mvpView?.openMainFragment()
                                        }
                                    } else {
                                        appExecutors.mainThread().execute {
                                            mvpView?.showContent()
                                            mvpView?.showMessage("save user failed")
                                        }
                                    }
                                }
                            }
                        } else {
                            mvpView?.showContent()
                            mvpView?.showMessage(it.message)
                        }
                    }
                }
            })
        }

        override fun getAllFactory() {
            mvpView?.showProgressDialog(R.string.loading_query_factory_info)
            dataManager.getAllFactory().enqueue(object : Callback<ApiResponse<List<FactoryInfo>>> {
                override fun onFailure(call: Call<ApiResponse<List<FactoryInfo>>>?, t: Throwable) {
                    mvpView?.hideProgressDialog()
                    t.message?.let { mvpView?.showMessage(it) }
                    mvpView?.getFactoryFailed()
                }

                override fun onResponse(call: Call<ApiResponse<List<FactoryInfo>>>?, response: Response<ApiResponse<List<FactoryInfo>>>) {
                    mvpView?.hideProgressDialog()
                    response.body()?.let {
                        if (it.isSucceed()) {
                            mvpView?.getAllFactory(it.data!!)
                        } else {
                            mvpView?.showMessage(it.message)
                            mvpView?.getFactoryFailed()
                        }
                    }
                }
            })
        }

        override fun getDataManager(): DataManager {
            return dataManager
        }

        override fun getAppExecutors(): AppExecutors {
            return appExecutors
        }
    }
}