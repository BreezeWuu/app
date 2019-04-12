package com.zotye.wms.ui.manualboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zotye.wms.R
import com.zotye.wms.data.api.model.ManualBoardDeliveryDto
import com.zotye.wms.data.api.model.MaterialPullResult
import com.zotye.wms.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_base.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/04/12
 */
class ManualBoardOutFragment : BaseFragment(), ManualBoardOutContract.ManualBoardOutView {

    @Inject
    lateinit var presenter: ManualBoardOutContract.ManualBoardOutPresenter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title")
                ?: getString(R.string.action_storage_unit_online)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun getManualBoardList(manualBoardList: List<ManualBoardDeliveryDto>) {

    }

    override fun saveManualBoardOutSucceed(result: List<MaterialPullResult>) {

    }
}