package com.zotye.wms.ui.storageunit.modify

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
 * Created by hechuangju on 2018/01/24
 */
object StorageUnitInfoContract {

    interface StorageUnitInfoView : MvpView {
        fun getStorageUnitInfo(barCodeInfo: BarcodeInfo)
    }

    interface StorageUnitInfoPresenter : MvpPresenter<StorageUnitInfoView> {
        fun getStorageUnitDetailInfoByCode(barCode: String)
    }

    class StorageUnitInfoPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<StorageUnitInfoView>(), StorageUnitInfoPresenter {
        override fun getStorageUnitDetailInfoByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
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
                                        mvpView?.getStorageUnitInfo(it.data!!)
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