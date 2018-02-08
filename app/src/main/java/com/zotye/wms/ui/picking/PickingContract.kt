package com.zotye.wms.ui.picking

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.CostCenter
import com.zotye.wms.data.api.model.StoragePackageMaterialInfo
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/02/07
 */
object PickingContract {
    interface PickingView : MvpView {
        fun getPickingBarCodeInfo(storagePackageMaterialInfoList: List<StoragePackageMaterialInfo>)
    }

    interface PickingPresenter : MvpPresenter<PickingView> {
        fun getPickingBarCodeInfo(barCode: String)
    }

    class PickingPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<PickingView>(), PickingPresenter {
        override fun getPickingBarCodeInfo(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getPickingBarCodeInfo(it.userId, barCode).enqueue(object : Callback<ApiResponse<List<StoragePackageMaterialInfo>>> {
                            override fun onFailure(call: Call<ApiResponse<List<StoragePackageMaterialInfo>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<StoragePackageMaterialInfo>>>?, response: Response<ApiResponse<List<StoragePackageMaterialInfo>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getPickingBarCodeInfo(it.data!!)
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