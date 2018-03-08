package com.zotye.wms.ui.goods

import android.support.v4.util.ArrayMap
import android.text.TextUtils
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.ValidSlInfoDto
import com.zotye.wms.data.api.model.receipt.DeliveryNoteInfoDto
import com.zotye.wms.data.api.model.receipt.DeliveryNoteInfoResponse
import com.zotye.wms.data.api.model.receipt.ReceiveDetailDto
import com.zotye.wms.data.api.request.MobileNoteRecvRequest
import com.zotye.wms.data.api.request.ReceiveConfirmRequest
import com.zotye.wms.data.api.response.ReceiveConfirmResponse
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
        fun normalNoteReceiveSucceed()
    }

    interface DeliveryNoteReceivePresenter : MvpPresenter<DeliveryNoteReceiveView> {
        fun getDeliveryNoteInfoByCode(barCode: String)
        fun getSlInfoForDeliveryNote(note: DeliveryNoteInfoDto)
        fun normalNoteReceive(response: DeliveryNoteInfoResponse)
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
                                response.body()?.let { noteInfo ->
                                    if (noteInfo.isSucceed()) {
                                        appExecutors.diskIO().execute {
                                            noteInfo.data?.receiveDetailList?.let {
                                                val keyList = ArrayMap<String, ReceiveDetailDto>()
                                                it.filter { TextUtils.isEmpty(it.parent) }.map { receiveDetailDto ->
                                                    keyList.put(receiveDetailDto.id, receiveDetailDto)
                                                }.toList()
                                                it.filter { !TextUtils.isEmpty(it.parent) }.map { receiveDetailDto ->
                                                    keyList[receiveDetailDto.parent]?.child?.add(receiveDetailDto)
                                                }.toList()
                                                noteInfo.data?.receiveDetailList = keyList.values.toList()
                                                noteInfo.data?.receiveDetailList?.let {
                                                    it.forEach { parent ->
                                                        parent.receiveNum = parent.requireNum - parent.lackNum - parent.unqualifyNum
                                                        parent.child.forEach { child ->
                                                            child.receiveNum = parent.receiveNum * child.componentCount
                                                        }
                                                    }
                                                }
                                            }
                                            appExecutors.mainThread().execute {
                                                mvpView?.hideProgressDialog()
                                                mvpView?.getDeliveryNoteInfoByCode(noteInfo.data)
                                            }
                                        }
                                    } else {
                                        mvpView?.hideProgressDialog()
                                        mvpView?.showMessage(noteInfo.message)
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

        override fun normalNoteReceive(response: DeliveryNoteInfoResponse) {
            val requestList = ArrayList<MobileNoteRecvRequest>()
            mvpView?.showProgressDialog(R.string.loading_submit_receive_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let {
                    val request = MobileNoteRecvRequest()
                    request.userId = it.userId
                    request.slCode = response.deliveryNoteInfo?.storageLocationCode
                    request.noteCode = response.deliveryNoteInfo?.noteCode
                    request.postTime = response.deliveryNoteInfo?.postTime
                    response.receiveDetailList?.forEach { parent ->
                        request.noteDetail.add(ReceiveConfirmRequest.newInstance(parent))
                        parent.child.forEach { child ->
                            child.receiveNum = parent.receiveNum * child.componentCount
                            request.noteDetail.add(ReceiveConfirmRequest.newInstance(child))
                        }
                    }
                    requestList.add(request)
                    appExecutors.mainThread().execute {
                        dataManager.normalNoteReceive(requestList).enqueue(object : Callback<ApiResponse<ReceiveConfirmResponse>> {
                            override fun onFailure(call: Call<ApiResponse<ReceiveConfirmResponse>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it) }
                            }

                            override fun onResponse(call: Call<ApiResponse<ReceiveConfirmResponse>>?, response: Response<ApiResponse<ReceiveConfirmResponse>>) {
                                mvpView?.hideProgressDialog()
                                response.body()?.let {
                                    if (it.isSucceed()) {
                                        mvpView?.normalNoteReceiveSucceed()
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