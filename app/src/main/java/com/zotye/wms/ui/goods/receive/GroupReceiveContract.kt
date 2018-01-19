package com.zotye.wms.ui.goods.receive

import android.support.annotation.StringRes
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.BarCodeType
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.LogisticsReceiveInfo
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/17
 */
object GroupReceiveContract {
    interface GroupReceiveView : MvpView {
        fun showProgressDialog(@StringRes resId: Int)
        fun hideProgressDialog()
        fun getBarCodeInfo(barcodeInfo: BarcodeInfo?)
    }

    interface GroupReceivePresenter : MvpPresenter<GroupReceiveView> {
        fun getPackageInfo(packageId: String)
        fun submitReceiveInfo(logisticsReceiveInfo: LogisticsReceiveInfo)
        fun cancelQueryPackageInfo()
    }

    class GroupReceivePresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<GroupReceiveView>(), GroupReceivePresenter {
        private var barcodeInfoCall: Call<ApiResponse<BarcodeInfo>>? = null
        override fun cancelQueryPackageInfo() {
            barcodeInfoCall?.cancel()
        }

        override fun submitReceiveInfo(logisticsReceiveInfo: LogisticsReceiveInfo) {
            mvpView?.showProgressDialog(R.string.loading_submit_receive_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {

                    }
                }
            }
        }

        override fun getPackageInfo(packageId: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        barcodeInfoCall = dataManager.getPackageInfo(it.userId, packageId)
                        barcodeInfoCall!!.enqueue(object : Callback<ApiResponse<BarcodeInfo>> {
                            override fun onFailure(call: Call<ApiResponse<BarcodeInfo>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                if (!barcodeInfoCall!!.isCanceled)
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

    }
}