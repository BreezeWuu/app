package com.zotye.wms.ui.picklist

import com.google.gson.Gson
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.BarCodeType
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.PickListInfo
import com.zotye.wms.data.api.model.checkbad.GetPickReceiptShelfDetailRequestDto
import com.zotye.wms.data.api.model.checkbad.PickReceiptShelfDetail
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/25
 */
object CheckBadProductContract {
    interface CheckBadProductView : MvpView {
        fun getPickListInfo(pickListInfo: PickListInfo)
        fun getBarCodeInfo(barCodeInfo: BarcodeInfo?)
        fun getPickReceiptShelfDetailList(pickReceiptShelfDetails: List<PickReceiptShelfDetail>?)
    }

    interface CheckBadProductPresenter : MvpPresenter<CheckBadProductView> {
        fun getPickListInfoByCode(barCode: String)
        fun getStorageUnitInfoByCode(barCode: String)
        fun getPickReceiptShelfDetail(request: List<GetPickReceiptShelfDetailRequestDto>)
    }

    class CheckBadProductPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<CheckBadProductView>(), CheckBadProductPresenter {
        override fun getPickListInfoByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getPickListInfoByCode(it.userId, "20", barCode).enqueue(object : Callback<ApiResponse<BarcodeInfo>> {
                            override fun onFailure(call: Call<ApiResponse<BarcodeInfo>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<BarcodeInfo>>?, response: Response<ApiResponse<BarcodeInfo>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        if (it.data?.barCodeType == BarCodeType.PickList.type)
                                            mvpView?.getPickListInfo(Gson().fromJson<PickListInfo>(it.data!!.barCodeInfo, PickListInfo::class.java))
                                        else
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

        override fun getStorageUnitInfoByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getStorageUnitInfoByBarcode(it.userId, barCode).enqueue(object : Callback<ApiResponse<BarcodeInfo>> {
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

        override fun getPickReceiptShelfDetail(request: List<GetPickReceiptShelfDetailRequestDto>) {
            mvpView?.showProgressDialog(R.string.loading_query_storage_unit_info)
            dataManager.getPickReceiptShelfDetail(request).enqueue(object : Callback<ApiResponse<List<PickReceiptShelfDetail>>> {
                override fun onFailure(call: Call<ApiResponse<List<PickReceiptShelfDetail>>>?, t: Throwable) {
                    mvpView?.hideProgressDialog()
                    t.message?.let { mvpView?.showMessage(it) }
                }

                override fun onResponse(call: Call<ApiResponse<List<PickReceiptShelfDetail>>>?, response: Response<ApiResponse<List<PickReceiptShelfDetail>>>) {
                    mvpView?.hideProgressDialog()
                    response.body()?.let {
                        if (it.isSucceed()) {
                            mvpView?.getPickReceiptShelfDetailList(it.data)
                        } else {
                            mvpView?.showMessage(it.message)
                        }
                    }
                }
            })
        }
    }
}