package com.zotye.wms.ui.goods

import com.google.gson.Gson
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.LogisticsReceiveInfo
import com.zotye.wms.data.api.model.receipt.GoodsReceiveResponse
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
object GroupReceiveContract {
    interface GroupReceiveView : MvpView {
        fun getBarCodeInfo(barcodeInfo: BarcodeInfo?)
        fun submitReceiveInfoSucceed(message: String)
    }

    interface GroupReceivePresenter : MvpPresenter<GroupReceiveView> {
        fun getPackageInfo(isGroupReceive: Boolean, packageId: String)
        fun submitReceiveInfo(logisticsReceiveInfo: LogisticsReceiveInfo)
    }

    class GroupReceivePresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<GroupReceiveView>(), GroupReceivePresenter {

        override fun submitReceiveInfo(logisticsReceiveInfo: LogisticsReceiveInfo) {
            mvpView?.showProgressDialog(R.string.loading_submit_receive_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.logisticsReceive(Gson().toJson(logisticsReceiveInfo)).enqueue(object : Callback<ApiResponse<GoodsReceiveResponse>> {
                            override fun onFailure(call: Call<ApiResponse<GoodsReceiveResponse>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<GoodsReceiveResponse>>?, response: Response<ApiResponse<GoodsReceiveResponse>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.submitReceiveInfoSucceed(it.message)
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

        override fun getPackageInfo(isGroupReceive: Boolean, packageId: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getPackageInfo(it.userId, isGroupReceive, packageId).enqueue(object : Callback<ApiResponse<BarcodeInfo>> {
                            override fun onFailure(call: Call<ApiResponse<BarcodeInfo>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<BarcodeInfo>>?, response: Response<ApiResponse<BarcodeInfo>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getBarCodeInfo(it.data)
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