package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.databinding.adapters.ExpandableListViewBindingAdapter
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.EditText
import android.widget.ExpandableListAdapter
import com.zotye.wms.R
import com.zotye.wms.data.api.model.PickListInfo
import com.zotye.wms.data.api.model.PickListMaterialInfo
import com.zotye.wms.databinding.LayoutPickListInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_loading_create.*
import kotlinx.android.synthetic.main.fragment_pick_list.*
import kotlinx.android.synthetic.main.layout_code_scanner.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/25
 */
class LoadingCreateFragment : BaseFragment(), LoadingCreateContract.LoadingCreateView, ScannerDelegate {

    companion object {
        fun newInstance(title: String): LoadingCreateFragment {
            val fragment = LoadingCreateFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: LoadingCreateContract.LoadingCreatePresenter
    private lateinit var adapter: PickListAdapter
    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_loading_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_loading_create)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        pickListScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@LoadingCreateFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        pickListInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            editText.setHint(R.string.picklist_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_picklist_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        adapter = PickListAdapter()
        pickListView.setAdapter(adapter)
    }

    override fun succeed(result: String) {
        presenter.getPickListInfoByCode(result)
    }

    override fun getPickListInfo(pickListInfo: PickListInfo) {
        adapter.addPickListInfo(pickListInfo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    class PickListAdapter : BaseExpandableListAdapter() {
        private val pickListInfoList = ArrayList<PickListInfo>()

        fun addPickListInfo(pickListInfo: PickListInfo) {
            pickListInfoList.add(pickListInfo)
            notifyDataSetChanged()
        }

        override fun getGroupCount(): Int {
            return pickListInfoList.size
        }

        override fun getGroup(groupPosition: Int): PickListInfo {
            return pickListInfoList[groupPosition]
        }

        override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
            if(convertView==null){

            }
            return View(parent.context)
        }

        override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
            return View(parent.context)
        }

        override fun getChild(groupPosition: Int, childPosition: Int): PickListMaterialInfo? {
            return pickListInfoList[groupPosition].materialInfoList?.get(childPosition)
        }

        override fun getChildrenCount(groupPosition: Int): Int {
            return pickListInfoList[groupPosition].materialInfoList?.size ?: 0
        }

        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
            return false
        }

        override fun hasStableIds(): Boolean {
            return false
        }

        override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return childPosition.toLong()
        }
    }

    class GroupViewHolder{

    }
}