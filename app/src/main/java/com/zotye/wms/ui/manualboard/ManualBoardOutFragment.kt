package com.zotye.wms.ui.manualboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zotye.wms.R
import com.zotye.wms.ui.common.BaseFragment

/**
 * Created by hechuangju on 2019/04/12
 */
class ManualBoardOutFragment : BaseFragment() {

    companion object {
        fun newInstance(title: String): ManualBoardOutFragment {
            val fragment = ManualBoardOutFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manual_board_out, container, false)
    }

}