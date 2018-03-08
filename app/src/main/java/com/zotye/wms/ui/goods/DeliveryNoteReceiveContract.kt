package com.zotye.wms.ui.goods

import android.support.v4.util.ArrayMap
import android.util.SparseArray
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.ValidSlInfoDto
import com.zotye.wms.data.api.model.receipt.DeliveryNoteInfoDto
import com.zotye.wms.data.api.model.receipt.DeliveryNoteInfoResponse
import com.zotye.wms.data.api.model.receipt.ReceiveDetailDto
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/03/07
 */
object DeliveryNoteReceiveContract {

    interface DeliveryNoteReceiveView : MvpView {
        fun getDeliveryNoteInfoByCode(data: DeliveryNoteInfoResponse?)
        fun getSlInfoForDeliveryNote(note: DeliveryNoteInfoDto, data: List<ValidSlInfoDto>?)
    }

    interface DeliveryNoteReceivePresenter : MvpPresenter<DeliveryNoteReceiveView> {
        fun getDeliveryNoteInfoByCode(barCode: String)
        fun getSlInfoForDeliveryNote(note: DeliveryNoteInfoDto)
    }

    class DeliveryNoteReceivePresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<DeliveryNoteReceiveView>(), DeliveryNoteReceivePresenter {

        override fun getDeliveryNoteInfoByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getDeliveryNoteInfoByCode(it.userId, barCode).enqueue(object : Callback<ApiResponse<DeliveryNoteInfoResponse>> {
                            override fun onFailure(call: Call<ApiResponse<DeliveryNoteInfoResponse>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<DeliveryNoteInfoResponse>>?, response: Response<ApiResponse<DeliveryNoteInfoResponse>>) {
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        appExecutors.diskIO().execute {
                                            it.data?.receiveDetailList?.let {
                                                val keyList = ArrayMap<String, ReceiveDetailDto>()
                                                it.filter { it.isBom == null || !it.isBom!! }.map { receiveDetailDto ->
                                                    keyList.put(receiveDetailDto.id, receiveDetailDto)
                                                }.toList()
                                                it.filter { it.isBom != null && it.isBom!! }.map { receiveDetailDto ->
                                                    keyList[receiveDetailDto.parent]?.child?.add(receiveDetailDto)
                                                }.toList()
                                            }
                                            appExecutors.mainThread().execute {
                                                mvpView?.hideProgressDialog()
                                                mvpView?.getDeliveryNoteInfoByCode(it.data)
                                            }
                                        }
                                    } else {
                                        mvpView?.hideProgressDialog()
                                        mvpView?.showMessage(it.message)
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }

        override fun getSlInfoForDeliveryNote(note: DeliveryNoteInfoDto) {
            mvpView?.showProgressDialog(R.string.loading_query_bar_code_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    appExecutors.mainThread().execute {
                        dataManager.getSlInfoForDeliveryNote(it.userId, note.noteCode!!).enqueue(object : Callback<ApiResponse<List<ValidSlInfoDto>>> {
                            override fun onFailure(call: Call<ApiResponse<List<ValidSlInfoDto>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<List<ValidSlInfoDto>>>?, response: Response<ApiResponse<List<ValidSlInfoDto>>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.getSlInfoForDeliveryNote(note, it.data)
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