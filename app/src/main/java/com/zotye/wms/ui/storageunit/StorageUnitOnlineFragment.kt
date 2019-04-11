package com.zotye.wms.ui.storageunit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zotye.wms.R
import com.zotye.wms.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_storage_unit_online.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/04/11
 */
class StorageUnitOnlineFragment : BaseFragment(), StorageUnitOnlineContract.StorageUnitOnlineView {

    @Inject
    lateinit var presenter: StorageUnitOnlineContract.StorageUnitOnlinePresenter

    companion object {
        fun newInstance(title: String): StorageUnitOnlineFragment {
            val fragment = StorageUnitOnlineFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_storage_unit_online, container, false)
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
        codeScanner.setOnClickListener {

        }
        codeInput.setOnClickListener {

        }
        stCodeScanner.setOnClickListener {

        }
        stCodeInput.setOnClickListener {

        }
        onlineSubmit.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }
}