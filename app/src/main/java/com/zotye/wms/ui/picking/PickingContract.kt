package com.zotye.wms.ui.picking

import com.zotye.wms.data.AppExecutors
import com.zotye.wms.data.DataManager
import com.zotye.wms.ui.common.BasePresenter
import com.zotye.wms.ui.common.MvpPresenter
import com.zotye.wms.ui.common.MvpView
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/02/07
 */
object PickingContract {
    interface PickingView : MvpView {

    }

    interface PickingPresenter : MvpPresenter<PickingView> {

    }

    class PickingPresenterImpl@Inject constructor(private val dataManager: DataManager, private val appExecutors: AppExecutors) : BasePresenter<PickingView>(), PickingPresenter {

    }
}