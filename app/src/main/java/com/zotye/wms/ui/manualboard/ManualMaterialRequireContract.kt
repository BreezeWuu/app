package com.zotye.wms.ui.manualboard

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.ManuaMaterialInfo
import com.zotye.wms.data.api.model.ProduceBean
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
        fun saveManualMaterialRequireSucceed(message:String)
    }

    interface ManualMaterialRequirePresenter : MvpPresenter<ManualMaterialRequireView> {
        fun queryMateiralInfos(materialNum: String)
        fun saveManualMaterialRequire(userId: String, produceBean: ProduceBean)
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

        override fun saveManualMaterialRequire(userId: String, produceBean: ProduceBean) {
            mvpView?.showProgressDialog(R.string.loading)
            dataManager.saveManualMaterialRequire(userId, produceBean).enqueue(object : Callback<ApiResponse<String>> {
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