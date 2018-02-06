package com.zotye.wms.ui.picklist

import com.google.gson.Gson
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.BarCodeType
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.PickListInfo
import com.zotye.wms.data.api.model.checkbad.ExternalCheckDetailDto
import com.zotye.wms.data.api.model.checkbad.ExternalCheckPickReceiptConfirmDto
import com.zotye.wms.data.api.model.checkbad.GetPickReceiptShelfDetailRequestDto
import com.zotye.wms.data.api.model.checkbad.PickReceiptShelfDetail
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import org.jetbrains.anko.collections.forEachByIndex
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
        fun getPickReceiptShelfDetailList(pickReceiptShelfDetails: List<PickReceiptShelfDetail>?)
        fun externalCheckPickReceiptConfirmSucceed()
        fun externalCheckPickReceiptConfirmFailed(message:String)
    }

    interface CheckBadProductPresenter : MvpPresenter<CheckBadProductView> {
        fun getPickListInfoByCode(barCode: String)
        fun getPickReceiptShelfDetail(request: List<GetPickReceiptShelfDetailRequestDto>)
        fun externalCheckPickReceiptConfirm(request: MutableList<PickReceiptShelfDetail>)
    }

    class CheckBadProductPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<CheckBadProductView>(), CheckBadProductPresenter {

        private var pickCode = ""

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
                                        if (it.data?.barCodeType == BarCodeType.PickList.type) {
                                            pickCode = barCode
                                            mvpView?.getPickListInfo(Gson().fromJson<PickListInfo>(it.data!!.barCodeInfo, PickListInfo::class.java))
                                        } else
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

        override fun externalCheckPickReceiptConfirm(request: MutableList<PickReceiptShelfDetail>) {
            mvpView?.showProgressDialog(R.string.loading_confirm_under_shelf)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    val confirmRequest = ExternalCheckPickReceiptConfirmDto()
                    confirmRequest.userId = it.userId
                    confirmRequest.pickReceiptNo = pickCode
                    confirmRequest.externalDetail = ArrayList()
                    request.forEachByIndex { pickReceiptShelfDetail ->
                        val externalCheckDetailDto = ExternalCheckDetailDto()
                        externalCheckDetailDto.count = pickReceiptShelfDetail.num
                        externalCheckDetailDto.spMaterialDetailId = pickReceiptShelfDetail.spMaterialDetailId
                        externalCheckDetailDto.storageUnitCode = pickReceiptShelfDetail.unitCode
                        (confirmRequest.externalDetail as ArrayList).add(externalCheckDetailDto)
                    }
                    appExecutors.mainThread().execute {
                        dataManager.externalCheckPickReceiptConfirm(confirmRequest).enqueue(object : Callback<ApiResponse<String>> {
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.externalCheckPickReceiptConfirmSucceed()
                                    } else {
                                        if (it.status == -1) {
                                            mvpView?.externalCheckPickReceiptConfirmFailed(it.message)
                                        } else
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