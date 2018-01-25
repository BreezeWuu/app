package com.zotye.wms.ui.picklist

import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/25
 */
object UnderShelfContract {
    interface UnderShelfView : MvpView {
        fun getPickListInfo()
    }

    interface UnderShelfPresenter : MvpPresenter<UnderShelfView> {
        fun getPickListInfoByCode(barCode: String)
    }

    class UnderShelfPresenterImpl @Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<UnderShelfView>(), UnderShelfPresenter {
        override fun getPickListInfoByCode(barCode: String) {

        }
    }
}