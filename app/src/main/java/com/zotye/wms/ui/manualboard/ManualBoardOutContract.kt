package com.zotye.wms.ui.manualboard

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.ManualBoardDeliveryDto
import com.zotye.wms.data.api.model.MaterialPullResult
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/04/12
 */
object ManualBoardOutContract {


    interface ManualBoardOutView : MvpView {
        fun getManualBoardList(manualBoardList: List<ManualBoardDeliveryDto>)
        fun saveManualBoardOutSucceed(message:String,result: List<MaterialPullResult>)
    }

    interface ManualBoardOutPresenter : MvpPresenter<ManualBoardOutView> {
        fun getManualBoardList(code: String, materialNum: String)
        fun saveManualBoardOut(manualBoardList: List<ManualBoardDeliveryDto>)
    }

    class ManualBoardOutPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<ManualBoardOutView>(), ManualBoardOutPresenter {
        override fun getManualBoardList(code: String, materialNum: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getManualBoardLis(code, materialNum).enqueue(object : Callback<ApiResponse<List<ManualBoardDeliveryDto>>> {
                            override fun onFailure(call: Call<ApiResponse<List<ManualBoardDeliveryDto>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<ManualBoardDeliveryDto>>>?, response: Response<ApiResponse<List<ManualBoardDeliveryDto>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed() && it.data != null) {
                                        mvpView?.getManualBoardList(it.data!!)
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

        override fun saveManualBoardOut(manualBoardList: List<ManualBoardDeliveryDto>) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.saveManualBoardOut(manualBoardList).enqueue(object : Callback<ApiResponse<List<MaterialPullResult>>> {
                            override fun onFailure(call: Call<ApiResponse<List<MaterialPullResult>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<MaterialPullResult>>>?, response: Response<ApiResponse<List<MaterialPullResult>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.saveManualBoardOutSucceed(it.message,it.data!!)
                                    } else {
                                        mvpView?.showMessage("手工看板出库失败！")
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