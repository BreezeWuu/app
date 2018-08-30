package com.zotye.wms.ui.goods

import android.app.AlertDialog
import android.content.Context
import com.zotye.wms.R
import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.model.SimpleAjaxResult
import com.zotye.wms.data.api.model.outcheck.OutBoundBadNewsDto
import com.zotye.wms.data.api.model.outcheck.OutBoundCheckDto
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import com.zotye.wms.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by yyf on 2018/08/25
 */
object OutBoundCheckContract{

    interface OutBoundCheckView : MvpView{
        fun getOutBoundCheckInfoByCode(data: List<OutBoundCheckDto>)

        fun getBadGoodsNewsInfo(data:OutBoundBadNewsDto?,outBoundCheck:OutBoundCheckDto)

        fun showDiaLog(msg : String?)
    }

    interface OutBoundCheckPresenter : MvpPresenter<OutBoundCheckView>{
        fun getOutBoundCheckInfoByCode(barCode: String)

        fun getBadGoodsNewsInfo(data:OutBoundCheckDto)

        fun poerBadGoodsNewsInfo(data:OutBoundCheckDto,num: String,reason: String)

        fun delBadGoodsNewsInfo(data:OutBoundCheckDto)
    }
    class OutBoundCheckPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors): BasePresenter<OutBoundCheckView>(), OutBoundCheckPresenter {
        override fun poerBadGoodsNewsInfo(data: OutBoundCheckDto, num: String, reason: String) {
            mvpView?.showProgressDialog(R.string.loading_query_pick_info)
            appExecutors.diskIO().execute{
                dataManager.getCurrentUser()?.let { it ->
                    appExecutors.mainThread().execute {
                        dataManager.operBadGoodsNews(it.userId, data.pickNum!!, data.materialId!!, data.supplierId!!, data.batchNum,num,reason).enqueue(object : Callback<ApiResponse<String>>{
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable?) {
                                mvpView?.hideProgressDialog()
                                t!!.message?.let { mvpView?.showMessage(it)}
                            }
                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>?) {
                                mvpView?.hideProgressDialog()
                                response!!.body()?.let {
                                    if(it.isSucceed() && it.message != null){
                                        appExecutors.mainThread().execute {
                                            mvpView?.hideProgressDialog()
                                            mvpView?.showDiaLog(it.message)
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }

        override fun delBadGoodsNewsInfo(data: OutBoundCheckDto) {
            mvpView?.showProgressDialog(R.string.loading_query_pick_info)
            appExecutors.diskIO().execute{
                dataManager.getCurrentUser()?.let { it ->
                    appExecutors.mainThread().execute {
                        dataManager.delBadGoodsNews(it.userId, data.pickNum!!, data.materialId!!, data.supplierId!!, data.batchNum).enqueue(object : Callback<ApiResponse<String>>{
                            override fun onFailure(call: Call<ApiResponse<String>>?, t: Throwable?) {
                                mvpView?.hideProgressDialog()
                                t!!.message?.let { mvpView?.showMessage(it)}
                            }
                            override fun onResponse(call: Call<ApiResponse<String>>?, response: Response<ApiResponse<String>>?) {
                                response!!.body()?.let {
                                    mvpView?.hideProgressDialog()
                                    if(it.isSucceed() && it.message != null){
                                        appExecutors.mainThread().execute {
                                            mvpView?.hideProgressDialog()
                                            mvpView?.showDiaLog(it.message)
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }

        override fun getOutBoundCheckInfoByCode(barCode: String) {
            mvpView?.showProgressDialog(R.string.loading_query_pick_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let { it ->
                    appExecutors.mainThread().execute {
                        dataManager.getPickInfo(it.userId, barCode).enqueue(object : Callback<ApiResponse<List<OutBoundCheckDto>>> {
                            override fun onFailure(call: Call<ApiResponse<List<OutBoundCheckDto>>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it)}
                            }
                            override fun onResponse(call: Call<ApiResponse<List<OutBoundCheckDto>>>?, response: Response<ApiResponse<List<OutBoundCheckDto>>>?) {
                                mvpView?.hideProgressDialog()
                                response!!.body()?.let {
                                    if(it.isSucceed() && it.data != null){
                                            it.data?.let {
                                                it.forEach { outBoundCheck ->
                                                    outBoundCheck.pickNum = barCode
                                                }
                                            }
                                        appExecutors.mainThread().execute {
                                            mvpView?.hideProgressDialog()
                                            mvpView?.getOutBoundCheckInfoByCode(it.data as List<OutBoundCheckDto>)
                                        }
                                    }else{
                                        appExecutors.mainThread().execute {
                                            mvpView?.hideProgressDialog()
                                            mvpView?.showDiaLog(it.message)
                                        }
                                    }
                                }
                            }
                        })
                    }
                }
            }
        }

        override fun getBadGoodsNewsInfo(data:OutBoundCheckDto) {
            mvpView?.showProgressDialog(R.string.loading_query_pick_Bad_info)
            appExecutors.diskIO().execute {
                dataManager.getCurrentUser()?.let { it ->
                    appExecutors.mainThread().execute {
                        dataManager.getBadGoodsNews(it.userId, data.pickNum!!, data.materialId!!, data.supplierId!!, data.batchNum).enqueue(object : Callback<ApiResponse<OutBoundBadNewsDto>> {
                            override fun onFailure(call: Call<ApiResponse<OutBoundBadNewsDto>>?, t: Throwable) {
                                mvpView?.hideProgressDialog()
                                t.message?.let { mvpView?.showMessage(it)}
                            }
                            override fun onResponse(call: Call<ApiResponse<OutBoundBadNewsDto>>?, response: Response<ApiResponse<OutBoundBadNewsDto>>?) {
                                mvpView?.hideProgressDialog()
                                response!!.body()?.let {
                                    if(it.isSucceed() && it.data != null){
                                        appExecutors.mainThread().execute {
                                            mvpView?.hideProgressDialog()
                                            mvpView?.getBadGoodsNewsInfo(it.data as OutBoundBadNewsDto,data)
                                        }
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