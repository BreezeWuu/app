package com.zotye.wms.ui.goods

import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.receipt.MobilePickReceiptRecvDto
import com.zotye.wms.data.api.model.receipt.MobileSinglePickReceiptRecvDto
import com.zotye.wms.data.api.model.receipt.PickReceiptDetailReceiveDto
import com.zotye.wms.data.api.model.picking.PickReceiptDto
import com.zotye.wms.data.api.response.ReceiveConfirmResponse
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
        fun truckReceiveSucceed()
    }

    interface StrictReceivePresenter : MvpPresenter<StrictReceiveView> {
        fun getPickReceiptInfoByCode(barCode: String)
        fun truckReceive(pickReceiptDto: PickReceiptDto?)
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

        override fun truckReceive(pickReceiptDto: PickReceiptDto?) {
            pickReceiptDto?.let {
                mvpView?.showProgressDialog(R.string.loading_submit_receive_info)
                appExecutors.diskIO().execute {
                    dataManager.getCurrentUser()?.let {
                        val recvInfoList = ArrayList<MobilePickReceiptRecvDto>()
                        val mobilePickReceiptRecvDto = MobilePickReceiptRecvDto()
                        mobilePickReceiptRecvDto.userId = it.userId
                        mobilePickReceiptRecvDto.recvDetail = ArrayList()
                        val mobileSinglePickReceiptRecvDto = MobileSinglePickReceiptRecvDto()
                        mobileSinglePickReceiptRecvDto.pickReceiptId = pickReceiptDto.pickReceiptId
                        mobileSinglePickReceiptRecvDto.pickReceiptDetail = ArrayList()
                        pickReceiptDto.pickReceiptDetail?.forEach {
                            val pickReceiptDetailReceiveDto = PickReceiptDetailReceiveDto()
                            pickReceiptDetailReceiveDto.lackNum = it.lackNum
                            pickReceiptDetailReceiveDto.pickReceiptDetailId = it.pickReceiptDetailId
                            pickReceiptDetailReceiveDto.recvNum = it.reciprocalNum
                            pickReceiptDetailReceiveDto.unqualifyNum = it.unqualifyNum
                            (mobileSinglePickReceiptRecvDto.pickReceiptDetail as ArrayList).add(pickReceiptDetailReceiveDto)
                        }
                        (mobilePickReceiptRecvDto.recvDetail as ArrayList).add(mobileSinglePickReceiptRecvDto)
                        recvInfoList.add(mobilePickReceiptRecvDto)
                        appExecutors.mainThread().execute {
                            dataManager.truckReceive(recvInfoList).enqueue(object : Callback<ApiResponse<ReceiveConfirmResponse>> {
                                override fun onFailure(call: Call<ApiResponse<ReceiveConfirmResponse>>?, t: Throwable) {
                                    mvpView?.hideProgressDialog()
                                    t.message?.let { mvpView?.showMessage(it) }
                                }

                                override fun onResponse(call: Call<ApiResponse<ReceiveConfirmResponse>>?, response: Response<ApiResponse<ReceiveConfirmResponse>>) {
                                    mvpView?.hideProgressDialog()
                                    response.body()?.let {
                                        if (it.isSucceed()) {
                                            mvpView?.truckReceiveSucceed()
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
}