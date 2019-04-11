package com.zotye.wms.ui.storageunit

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.BarcodeInfo
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
object StorageUnitOnlineContract {

    interface StorageUnitOnlineView : MvpView {
//        fun getStorageUnitInfo(barCodeInfo: BarcodeInfo)
    }

    interface StorageUnitOnlinePresenter : MvpPresenter<StorageUnitOnlineView> {
        fun onlineConfirm(pickCode: String, stationCode: String)
    }

    class StorageUnitOnlinePresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<StorageUnitOnlineView>(), StorageUnitOnlinePresenter {
        override fun onlineConfirm(barCode: String, stationCode: String) {
            mvpView?.showProgressDialog(R.string.loading_submit_online_confirm)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getStorageUnitDetailInfoByCode(it.userId, barCode).enqueue(object : Callback<ApiResponse<BarcodeInfo>> {
                            override fun onFailure(call: Call<ApiResponse<BarcodeInfo>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<BarcodeInfo>>?, response: Response<ApiResponse<BarcodeInfo>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed() && it.data != null) {
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