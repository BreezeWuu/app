package com.zotye.wms.ui.manualboard

import com.google.gson.Gson
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.*
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/05/18
 */
object ManualMaterialRequireContract {
    interface ManualMaterialRequireView : MvpView {
        fun queryMateiralInfos(storagePackageMaterialInfoList: ManuaMaterialInfo)
        fun saveManualMaterialRequireSucceed(message: String)
        fun getPackageInfo(fromJson: PackageInfo)
    }

    interface ManualMaterialRequirePresenter : MvpPresenter<ManualMaterialRequireView> {
        fun queryMateiralInfos(materialNum: String)
        fun saveManualMaterialRequire(produceBean: ProduceBean)
        fun getPackageInfo(toString: String)
    }

    class ManualMaterialRequirePresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<ManualMaterialRequireView>(), ManualMaterialRequirePresenter {

        override fun queryMateiralInfos(materialNum: String) {
            mvpView?.showProgressDialog(R.string.loading)
            dataManager.queryMateiralInfos(materialNum).enqueue(object : Callback<ApiResponse<ManuaMaterialInfo>> {
                override fun onFailure(call: Call<ApiResponse<ManuaMaterialInfo>>?, t: Throwable) {
                    mvpView?.hideProgressDialog()
                    t.message?.let { mvpView?.showMessage(it) }
                }

                override fun onResponse(call: Call<ApiResponse<ManuaMaterialInfo>>?, response: Response<ApiResponse<ManuaMaterialInfo>>) {
                    mvpView?.hideProgressDialog()
                    response.body()?.let {
                        if (it.isSucceed()) {
                            mvpView?.queryMateiralInfos(it.data!!)
                        } else {
                            mvpView?.showMessage(it.message)
                        }
                    }
                }
            })
        }

        override fun getPackageInfo(toString: String) {
            mvpView?.showProgressDialog(R.string.loading)
            appExecutors.mainThread().execute {
                dataManager.getPackageInfo(toString).enqueue(object : Callback<ApiResponse<BarcodeInfo>> {
                    override fun onFailure(call: Call<ApiResponse<BarcodeInfo>>?, t: Throwable) {
                        mvpView?.hideProgressDialog()
                        t.message?.let { mvpView?.showMessage(it) }
                    }

                    override fun onResponse(call: Call<ApiResponse<BarcodeInfo>>?, response: Response<ApiResponse<BarcodeInfo>>) {
                        mvpView?.hideProgressDialog()
                        response.body()?.let {
                            if (it.isSucceed()) {
                                it.data?.let { info ->
                                    val barcodeType = BarCodeType.fromCodeType(info.barCodeType)
                                    barcodeType?.let {
                                        when (it) {
                                            BarCodeType.Package -> {
                                                mvpView?.getPackageInfo(Gson().fromJson<PackageInfo>(info.barCodeInfo, PackageInfo::class.java))
                                            }
                                            else ->{

                                            }
                                        }
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

        override fun saveManualMaterialRequire(produceBean: ProduceBean) {
            mvpView?.showProgressDialog(R.string.loading)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        produceBean.userId = it.userId
                        produceBean.factoryCode = it.defaultFactoryCode
                        dataManager.saveManualMaterialRequire(produceBean).enqueue(object : Callback<ApiResponse<String>> {
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.saveManualMaterialRequireSucceed(it.message)
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