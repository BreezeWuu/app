package com.zotye.wms.ui.picklist

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.*
import com.zotye.wms.data.api.model.under.shelf.MaterialReplenishment
import com.zotye.wms.data.api.model.under.shelf.PrMobileConfirmRequest
import com.zotye.wms.data.api.model.under.shelf.SUMaterialInfo
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
object UnderShelfContract {
    interface UnderShelfView : MvpView {
        fun getPickListPullOffShelfList(pickListPullOffShelfList: List<PickListPullOffShelf>)
        fun getPickListPullOffShelfListFailed()
        fun getStorageUnitMaterialTotalNumber(position: Int, info: SUMaterialInfo)
        fun underShelfSucceed()
        fun addPackageSucceed(prNo: String, stCode: String)
        fun deletePackageSucceed(prNo: String, stCode: String)
    }

    interface UnderShelfPresenter : MvpPresenter<UnderShelfView> {
        fun getPickListInfoByCode(barCode: String)
        fun getStorageUnitMaterialTotalNumber(position: Int, storageUnitInfoCode: String, spDetailId: String)
        fun underShelfConfirm(request: PrMobileConfirmRequest)
        fun addPackage(prNo: String, stCode: String)
        fun deletePackage(prNo: String, stCode: String)
    }

    class UnderShelfPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<UnderShelfView>(), UnderShelfPresenter {
        override fun getPickListInfoByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getPickListInfoByCode(it.userId, "10", barCode).enqueue(object : Callback<ApiResponse<BarcodeInfo>> {
                            override fun onFailure(call: Call<ApiResponse<BarcodeInfo>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                                mvpView?.getPickListPullOffShelfListFailed()
                            }

                            override fun onResponse(call: Call<ApiResponse<BarcodeInfo>>?, response: Response<ApiResponse<BarcodeInfo>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed() && it.data != null) {
                                        val list = Gson().fromJson<List<PickListPullOffShelf>>(it.data!!.barCodeInfo, object : TypeToken<List<PickListPullOffShelf>>() {

                                        }.type)
                                        mvpView?.getPickListPullOffShelfList(list)
                                    } else {

                                        mvpView?.showMessage(it.message)

                                        mvpView?.getPickListPullOffShelfListFailed()
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }

        override fun getStorageUnitMaterialTotalNumber(position: Int, storageUnitInfoCode: String, spDetailId: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getStorageUnitMaterialTotalNumber(it.userId, storageUnitInfoCode, spDetailId).enqueue(object : Callback<ApiResponse<SUMaterialInfo>> {
                            override fun onFailure(call: Call<ApiResponse<SUMaterialInfo>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<SUMaterialInfo>>?, response: Response<ApiResponse<SUMaterialInfo>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getStorageUnitMaterialTotalNumber(position, it.data!!)
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

        override fun underShelfConfirm(request: PrMobileConfirmRequest) {
            mvpView?.showProgressDialog(R.string.loading_confirm_under_shelf)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        request.userId = it.userId
                        dataManager.underShelfConfirm(request).enqueue(object : Callback<ApiResponse<List<MaterialReplenishment>>> {
                            override fun onFailure(call: Call<ApiResponse<List<MaterialReplenishment>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<MaterialReplenishment>>>?, response: Response<ApiResponse<List<MaterialReplenishment>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.underShelfSucceed()
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

        override fun addPackage(prNo: String, stCode: String) {
            mvpView?.showProgressDialog(R.string.submiting)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.addPackage(prNo, stCode).enqueue(object : Callback<ApiResponse<String>> {
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.addPackageSucceed(prNo, stCode)
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

        override fun deletePackage(prNo: String, stCode: String) {
            val lesPackageInfoUpdateDto = LESPackageInfoUpdateDto().apply {
                pickReceiptCode = prNo
                packageCodes = listOf(stCode)
            }
            mvpView?.showProgressDialog(R.string.submiting)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.deletePackage(lesPackageInfoUpdateDto).enqueue(object : Callback<ApiResponse<String>> {
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.deletePackageSucceed(prNo, stCode)
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