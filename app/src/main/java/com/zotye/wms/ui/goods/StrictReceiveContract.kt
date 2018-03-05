package com.zotye.wms.ui.goods

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.picking.PickReceiptDto
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/03/05
 */
object StrictReceiveContract {

    interface StrictReceiveView : MvpView {
        fun getPickReceiptInfoByCode(pickReceiptDto: PickReceiptDto?)
    }

    interface StrictReceivePresenter : MvpPresenter<StrictReceiveView> {
        fun getPickReceiptInfoByCode(barCode: String)
    }

    class StrictReceivePresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<StrictReceiveView>(), StrictReceivePresenter {

        override fun getPickReceiptInfoByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getPickReceiptInfoByCode(it.userId, barCode).enqueue(object : Callback<ApiResponse<PickReceiptDto>> {
                            override fun onFailure(call: Call<ApiResponse<PickReceiptDto>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<PickReceiptDto>>?, response: Response<ApiResponse<PickReceiptDto>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getPickReceiptInfoByCode(it.data)
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