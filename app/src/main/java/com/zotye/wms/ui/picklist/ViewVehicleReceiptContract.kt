package com.zotye.wms.ui.picklist

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.PutAwayInfo
import com.zotye.wms.data.api.model.VehicleReceiptFilterInfo
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/04/22
 */
object ViewVehicleReceiptContract {

    interface ViewVehicleReceiptView : MvpView {
        fun getViewVehicleReceiptFilterInfo(vehicleReceiptFilterInfo: VehicleReceiptFilterInfo?)
    }

    interface ViewVehicleReceiptPresenter : MvpPresenter<ViewVehicleReceiptView> {
        fun getViewVehicleReceiptFilterInfo()
    }

    class ViewVehicleReceiptPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<ViewVehicleReceiptView>(), ViewVehicleReceiptPresenter {
        override fun getViewVehicleReceiptFilterInfo() {
            mvpView?.showLoading(R.string.loading)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.viewVehicleReceiptFilterInfo(it.userId).enqueue(object : Callback<ApiResponse<VehicleReceiptFilterInfo>> {
                            override fun onFailure(call: Call<ApiResponse<VehicleReceiptFilterInfo>>?, t: Throwable) {
                                mvpView?.showContent()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<VehicleReceiptFilterInfo>>?, response: Response<ApiResponse<VehicleReceiptFilterInfo>>) {
                                mvpView?.showContent()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getViewVehicleReceiptFilterInfo(it.data)
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