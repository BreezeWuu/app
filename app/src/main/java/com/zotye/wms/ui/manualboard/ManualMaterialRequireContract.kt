package com.zotye.wms.ui.manualboard

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.CostCenter
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import com.zotye.wms.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/05/18
 */
object ManualMaterialRequireContract {
    interface ManualMaterialRequireView : MvpView {
        fun queryMateiralInfos(storagePackageMaterialInfoList: List<CostCenter>)
    }

    interface ManualMaterialRequirePresenter : MvpPresenter<ManualMaterialRequireView> {
        fun queryMateiralInfos(materialNum: String)
    }

    class ManualMaterialRequirePresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<ManualMaterialRequireView>(), ManualMaterialRequirePresenter {

        override fun queryMateiralInfos(materialNum: String) {
            mvpView?.showProgressDialog(R.string.loading_cost_center_info)
            dataManager.queryMateiralInfos(materialNum).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable) {
                    t.message?.let { mvpView?.showError(it) }
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
                    mvpView?.hideProgressDialog()
                    response.body()?.let {
                        Log.e(it.string())
//                        if (it.isSucceed()) {
//                            mvpView?.showContent()
//                            mvpView?.getCostCenter(it.data!!)
//                        } else {
//                            mvpView?.showError(it.message)
//                        }
                    }
                }
            })
        }
    }
}