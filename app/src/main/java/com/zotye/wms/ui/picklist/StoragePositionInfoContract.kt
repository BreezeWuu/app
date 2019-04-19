package com.zotye.wms.ui.picklist

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.PutAwayInfo
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/04/19
 */
object StoragePositionInfoContract{

    interface StoragePositionInfoView : MvpView {
        fun getStoragePositionInfo(list: List<PutAwayInfo>?)
    }

    interface StoragePositionInfoPresenter : MvpPresenter<StoragePositionInfoView> {
        fun getStoragePositionInfo(code: String)
    }

    class StoragePositionInfoPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<StoragePositionInfoView>(), StoragePositionInfoPresenter {
        override fun getStoragePositionInfo(code: String) {
            mvpView?.showProgressDialog(R.string.loading_submit_online_confirm)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getStoragePositionInfoByCode(code).enqueue(object : Callback<ApiResponse<List<PutAwayInfo>>> {
                            override fun onFailure(call: Call<ApiResponse<List<PutAwayInfo>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<PutAwayInfo>>>?, response: Response<ApiResponse<List<PutAwayInfo>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getStoragePositionInfo(it.data)
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