package com.zotye.wms.ui.picklist

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.MESPickReceiptDto
import com.zotye.wms.data.api.model.VehicleReceiptDto
import com.zotye.wms.data.api.model.VehicleReceiptFilterInfo
import com.zotye.wms.data.api.model.VehicleReceiptParamsDto
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
        fun getVehicleReceipt(data: List<VehicleReceiptDto>?)
        fun getMesPickReceiptList(vId:String,data: List<MESPickReceiptDto>?)
    }

    interface ViewVehicleReceiptPresenter : MvpPresenter<ViewVehicleReceiptView> {
        fun getViewVehicleReceiptFilterInfo()
        fun searchVehicleReceipt(dto: VehicleReceiptParamsDto)
        fun getVehicleReceiptByCode(code:String)
        fun getMesPickReceiptListById(id: String)
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

        override fun getVehicleReceiptByCode(code: String) {
            mvpView?.showProgressDialog(R.string.loading)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getVehicleReceiptByCode(code).enqueue(object : Callback<ApiResponse<List<VehicleReceiptDto>>> {
                            override fun onFailure(call: Call<ApiResponse<List<VehicleReceiptDto>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<VehicleReceiptDto>>>?, response: Response<ApiResponse<List<VehicleReceiptDto>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getVehicleReceipt(it.data)
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

        override fun searchVehicleReceipt(dto: VehicleReceiptParamsDto) {
            mvpView?.showProgressDialog(R.string.loading)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.searchVehicleReceipt(dto).enqueue(object : Callback<ApiResponse<List<VehicleReceiptDto>>> {
                            override fun onFailure(call: Call<ApiResponse<List<VehicleReceiptDto>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<VehicleReceiptDto>>>?, response: Response<ApiResponse<List<VehicleReceiptDto>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getVehicleReceipt(it.data)
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


        override fun getMesPickReceiptListById(id: String) {
            mvpView?.showProgressDialog(R.string.loading)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getMesPickReceiptListById(id).enqueue(object : Callback<ApiResponse<List<MESPickReceiptDto>>> {
                            override fun onFailure(call: Call<ApiResponse<List<MESPickReceiptDto>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<MESPickReceiptDto>>>?, response: Response<ApiResponse<List<MESPickReceiptDto>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getMesPickReceiptList(id,it.data)
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