package com.zotye.wms.ui.picklist

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/04/11
 */
object ShelfConfirmContract{

    interface ShelfConfirmView : MvpView {
        fun shelfConfirmSucceed()
    }

    interface ShelfConfirmPresenter : MvpPresenter<ShelfConfirmView> {
        fun shelfConfirm(pickCode: String, stationCode: String)
    }

    class ShelfConfirmPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<ShelfConfirmView>(), ShelfConfirmPresenter {
        override fun shelfConfirm(pickCode: String, stationCode: String) {
            mvpView?.showProgressDialog(R.string.loading_submit_online_confirm)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.checkStSlInfo(pickCode,stationCode).enqueue(object : Callback<ApiResponse<String>> {
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.shelfConfirmSucceed()
                                        mvpView?.showMessage(it.message)
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