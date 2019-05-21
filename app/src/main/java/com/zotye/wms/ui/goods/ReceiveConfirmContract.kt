package com.zotye.wms.ui.goods

import android.text.TextUtils
import com.google.gson.Gson
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
import com.google.gson.reflect.TypeToken
import com.zotye.wms.data.api.RecLesDto
import com.zotye.wms.data.api.model.*


/**
 * Created by hechuangju on 2018/01/24
 */
object ReceiveConfirmContract {
    interface ReceiveConfirmView : MvpView {
        fun getUnReceivePickInfoList(pickInfoList: List<PickListInfo>)
        fun getUnReceivePackageList(packageInfoList: List<PackageInfo>)
        fun packageReceiveSucceed(message: String?)
    }

    interface ReceiveConfirmPresenter : MvpPresenter<ReceiveConfirmView> {
        fun logisticsReceiveConfirmInfoByCode(barCode: String)
        fun reliveryForLesDeliveryNoteByCode(barCode: String)
        fun logisticsReceiveConfirm(barCode: String)
        fun reliveryForLesDeliveryNote(barCode: RecLesDto)
    }

    class ReceiveConfirmPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<ReceiveConfirmView>(), ReceiveConfirmPresenter {
        override fun logisticsReceiveConfirmInfoByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.logisticsReceiveConfirmInfoByCode(it.userId, barCode).enqueue(object : Callback<ApiResponse<BarcodeInfo>> {
                            override fun onFailure(call: Call<ApiResponse<BarcodeInfo>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<BarcodeInfo>>?, response: Response<ApiResponse<BarcodeInfo>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed() && it.data != null) {
                                        when (it.data!!.barCodeType) {
                                            BarCodeType.Package.type -> {
                                                mvpView?.getUnReceivePackageList(Gson().fromJson<List<PackageInfo>>(it.data!!.barCodeInfo, object : TypeToken<List<PackageInfo>>() {

                                                }.type))
                                            }
                                            BarCodeType.PickList.type -> {
                                                mvpView?.getUnReceivePickInfoList(Gson().fromJson<List<PickListInfo>>(it.data!!.barCodeInfo, object : TypeToken<List<PickListInfo>>() {

                                                }.type))
                                            }
                                            else -> {

                                            }
                                        }
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

        override fun reliveryForLesDeliveryNoteByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.reliveryForLesDeliveryNoteByCode(it.userId, barCode).enqueue(object : Callback<ApiResponse<BarcodeInfo>> {
                            override fun onFailure(call: Call<ApiResponse<BarcodeInfo>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<BarcodeInfo>>?, response: Response<ApiResponse<BarcodeInfo>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed() && it.data != null) {
                                        when (it.data!!.barCodeType) {
                                            BarCodeType.Package.type -> {
                                                mvpView?.getUnReceivePackageList(Gson().fromJson<List<PackageInfo>>(it.data!!.barCodeInfo, object : TypeToken<List<PackageInfo>>() {

                                                }.type))
                                            }
                                            BarCodeType.PickList.type -> {
                                                mvpView?.getUnReceivePickInfoList(Gson().fromJson<List<PickListInfo>>(it.data!!.barCodeInfo, object : TypeToken<List<PickListInfo>>() {

                                                }.type))
                                            }
                                            else -> {

                                            }
                                        }
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

        override fun logisticsReceiveConfirm(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_submit_receive_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.logisticsReceiveConfirm(it.userId, barCode).enqueue(object : Callback<ApiResponse<String>> {
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.packageReceiveSucceed(if(TextUtils.isEmpty(it.message))"收货成功" else it.message )
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

        override fun reliveryForLesDeliveryNote(barCode: RecLesDto) {
            mvpView?.showProgressDialog(R.string.loading_submit_receive_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.reliveryForLesDeliveryNote(barCode).enqueue(object : Callback<ApiResponse<String>> {
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.packageReceiveSucceed(if(TextUtils.isEmpty(it.message))"收货成功" else it.message)
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