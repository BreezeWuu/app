package com.zotye.wms.ui.picking

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.CostCenter
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/02/08
 */
object ChooseCostCenterContract {
    interface ChooseCostCenterView : MvpView {
        fun getCostCenter(storagePackageMaterialInfoList: List<CostCenter>)
    }

    interface ChooseCostCenterPresenter : MvpPresenter<ChooseCostCenterView> {
        fun getCostCenterByUser()
    }

    class ChooseCostCenterPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<ChooseCostCenterView>(), ChooseCostCenterPresenter {

        override fun getCostCenterByUser() {
            mvpView?.showLoading(R.string.loading_cost_center_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getCostCenterByUser(it.userId).enqueue(object : Callback<ApiResponse<List<CostCenter>>> {
                            override fun onFailure(call: Call<ApiResponse<List<CostCenter>>>?, t: Throwable) {
                                t.message?.let { mvpView?.showError(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<CostCenter>>>?, response: Response<ApiResponse<List<CostCenter>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.showContent()
                                        mvpView?.getCostCenter(it.data!!)
                                    } else {
                                        mvpView?.showError(it.message)
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