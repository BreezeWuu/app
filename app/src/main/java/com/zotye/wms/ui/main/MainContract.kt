package com.zotye.wms.ui.main

import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.Resource
import com.zotye.wms.data.api.model.User
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/17
 */
object MainContract {
    interface MainMvpView : MvpView {
        fun getUserResources(resources: List<Resource>)
        fun refreshFinished()
    }

    interface MainMvpPresenter : MvpPresenter<MainMvpView> {
        fun updateUserResources(userId: String)
    }

    class MainPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<MainMvpView>(), MainMvpPresenter {
        override fun updateUserResources(userId: String) {
            dataManager.getUserInfo(userId).enqueue(object : Callback<ApiResponse<User>> {
                override fun onResponse(call: Call<ApiResponse<User>>?, response: Response<ApiResponse<User>>) {
                    response.body()?.let {
                        if (it.isSucceed()) {
                            it.data?.let { user ->
                                appExecutors.diskIO().execute {
                                    dataManager.updateUser(user)
                                    appExecutors.mainThread().execute {
                                        mvpView?.showContent()
                                        mvpView?.getUserResources(user.resources)
                                    }
                                }
                            }
                        } else {
                            mvpView?.showContent()
                            mvpView?.showMessage(it.message)
                        }
                    }
                    mvpView?.refreshFinished()
                }

                override fun onFailure(call: Call<ApiResponse<User>>?, t: Throwable) {
                    t.message?.let { mvpView?.showMessage(it) }
                    mvpView?.refreshFinished()
                }
            })
        }
    }
}