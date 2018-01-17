package com.zotye.wms.ui.goods.receive

import android.support.annotation.StringRes
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.PackageInfo
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
        fun getPackageInfo(packageInfo: PackageInfo?)
    }

    interface GroupReceivePresenter : MvpPresenter<GroupReceiveView> {
        fun getPackageInfo(packageId: String)
        fun cancelQueryPackageInfo()
    }

    class GroupReceivePresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<GroupReceiveView>(), GroupReceivePresenter {
        private var packageInfoCall: Call<ApiResponse<PackageInfo>>? = null
        override fun cancelQueryPackageInfo() {
            packageInfoCall?.cancel()
        }

        override fun getPackageInfo(packageId: String) {
            mvpView?.showProgressDialog(R.string.loading_query_package_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        packageInfoCall = dataManager.getPackageInfo(it.userId, packageId)
                        packageInfoCall!!.enqueue(object : Callback<ApiResponse<PackageInfo>> {
                            override fun onFailure(call: Call<ApiResponse<PackageInfo>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                if (!packageInfoCall!!.isCanceled)
                                    t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<PackageInfo>>?, response: Response<ApiResponse<PackageInfo>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getPackageInfo(it.data)
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