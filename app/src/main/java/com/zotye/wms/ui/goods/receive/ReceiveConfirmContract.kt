package com.zotye.wms.ui.goods.receive

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.PackageInfo
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/24
 */
object ReceiveConfirmContract {
    interface ReceiveConfirmView : MvpView {
        fun getUnReceivePackageList(packageInfoList: List<PackageInfo>)
        fun packageReceiveSucceed(message: String)
    }

    interface ReceiveConfirmPresenter : MvpPresenter<ReceiveConfirmView> {
        fun logisticsReceiveConfirmInfoByCode(barCode: String)
        fun logisticsReceiveConfirm(barCode: String)
    }

    class ReceiveConfirmPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<ReceiveConfirmView>(), ReceiveConfirmPresenter {
        override fun logisticsReceiveConfirmInfoByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.logisticsReceiveConfirmInfoByCode(it.userId, barCode).enqueue(object : Callback<ApiResponse<List<PackageInfo>>> {
                            override fun onFailure(call: Call<ApiResponse<List<PackageInfo>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<PackageInfo>>>?, response: Response<ApiResponse<List<PackageInfo>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed() && it.data != null && it.data!!.isNotEmpty()) {
                                        mvpView?.getUnReceivePackageList(it.data!!)
                                    } else {
                                        mvpView?.showMessage(it.message)
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }

        override fun logisticsReceiveConfirm(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_submit_receive_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.logisticsReceiveConfirm(it.userId, barCode).enqueue(object : Callback<ApiResponse<String>> {
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.packageReceiveSucceed(it.message)
                                    } else {
                                        mvpView?.showMessage(it.message)
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}