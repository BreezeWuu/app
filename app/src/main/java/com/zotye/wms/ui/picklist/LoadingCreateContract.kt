package com.zotye.wms.ui.picklist

import android.text.TextUtils
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.BarcodeInfo
import com.zotye.wms.data.api.model.PickListInfo
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
object LoadingCreateContract {
    interface LoadingCreateView : MvpView {
        fun getPickListInfo(pickListInfo: PickListInfo)
        fun createLoadingListSucceed(message: String)
    }

    interface LoadingCreatePresenter : MvpPresenter<LoadingCreateView> {
        fun getPickListInfoByCode(barCode: String)
        fun createLoadingList(carNumber: String, list: List<PickListInfo>)
    }

    class LoadingCreatePresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<LoadingCreateView>(), LoadingCreatePresenter {
        override fun getPickListInfoByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getPickListInfoByCode(it.userId, "40", barCode).enqueue(object : Callback<ApiResponse<PickListInfo>> {
                            override fun onFailure(call: Call<ApiResponse<PickListInfo>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<PickListInfo>>?, response: Response<ApiResponse<PickListInfo>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed() && it.data != null) {
                                        mvpView?.getPickListInfo(it.data!!)
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

        override fun createLoadingList(carNumber: String, list: List<PickListInfo>) {
            mvpView?.showProgressDialog(R.string.loading_submit_pick_list_create)
            appExecutors.diskIO().execute {
                val pickCodeList = ArrayList<String>()
                list.forEachByIndex {
                    if (!TextUtils.isEmpty(it.id))
                        pickCodeList.add(it.id)
                }
                val pickListInfoJson = Gson().toJson(pickCodeList)
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.createLoadingList(it.userId, carNumber, pickListInfoJson).enqueue(object : Callback<ApiResponse<String>> {
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.createLoadingListSucceed(it.message)
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