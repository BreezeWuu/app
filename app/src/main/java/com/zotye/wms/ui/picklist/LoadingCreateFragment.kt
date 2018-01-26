package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zotye.wms.R
import com.zotye.wms.data.api.model.PickListInfo
import com.zotye.wms.data.api.model.PickListMaterialInfo
import com.zotye.wms.databinding.ItemPickListInfoBinding
import com.zotye.wms.databinding.ItemPickListMaterialInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.QRCodeScannerFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_loading_create.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.find
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
        carInput.onClick {
            if (adapter.data.size == 0) {
                showMessage(R.string.picklist_create_empty_error)
            } else {
                val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
                val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
                editText.setHint(R.string.picklist_code)
                AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_picklist_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                    pickListCreate(editText.text.toString())
                    hideKeyboard(editText)
                }.setPositiveButton(R.string.cancel, null).show()
                showKeyboard(editText)
            }
        }
        carScanner.onClick {
            if (adapter.data.size == 0) {
                showMessage(R.string.picklist_create_empty_error)
            } else {
                val fragment = QRCodeScannerFragment()
                fragment.setScannerDelegate(object : ScannerDelegate {
                    override fun succeed(result: String) {
                        pickListCreate(result)
                    }
                })
                fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
            }
        }
        adapter = PickListAdapter(null)
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.pick_list_empty)
        adapter.emptyView = emptyView
        pickListRecyclerView.layoutManager = LinearLayoutManager(context)
        pickListRecyclerView.adapter = adapter
    }

    private fun pickListCreate(carNumber: String) {
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(getString(R.string.picklist_create_car_number, carNumber)).setNegativeButton(R.string.ok) { _, _ ->
            presenter.createLoadingList(carNumber, adapter.data as List<PickListInfo>)
        }.setPositiveButton(R.string.cancel, null).create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun createLoadingListSucceed(message: String) {
        val dialog = AlertDialog.Builder(context!!).setTitle(R.string.info).setMessage(Html.fromHtml(message)).setNegativeButton(R.string.ok) { _, _ ->
            adapter.setNewData(null)
        }.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun succeed(result: String) {
        if (adapter.data.contains(PickListInfo(result))) {
            showMessage(R.string.same_pick_list_error)
        } else {
            presenter.getPickListInfoByCode(result)
        }
    }

    override fun getPickListInfo(pickListInfo: PickListInfo) {
        pickListRecyclerView.post {
            pickListInfo.subItems = pickListInfo.materialInfoList
            adapter.addData(pickListInfo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
    }

    class PickListAdapter(data: MutableList<MultiItemEntity>?) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

        init {
            addItemType(PickListInfo.TYPE_PICK_LIST, R.layout.item_pick_list_info)
            addItemType(PickListMaterialInfo.TYPE_PICK_LIST_MATERIAL_INFO, R.layout.item_pick_list_material_info);
        }

        override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
            when (helper.itemViewType) {
                PickListInfo.TYPE_PICK_LIST -> {
                    val dataBind = DataBindingUtil.bind<ItemPickListInfoBinding>(helper.itemView)
                    dataBind.info = item as PickListInfo
                    helper.getView<Button>(R.id.expandButton).onClick { view ->
                        if (item.isExpanded) {
                            collapse(helper.adapterPosition)
                        } else {
                            expand(helper.adapterPosition)
                        }
                    }
                    helper.getView<Button>(R.id.deleteButton).onClick { view ->
                        AlertDialog.Builder(view!!.context).setMessage(view.resources.getString(R.string.delete_pic_item_info, item.code)).setNegativeButton(R.string.ok) { _, _ ->
                            remove(helper.adapterPosition)
                        }.setPositiveButton(R.string.cancel, null).show()
                    }
                    helper.getView<Button>(R.id.expandButton).setText(if (item.isExpanded) R.string.picklist_material_collapse else R.string.picklist_material_expand)
                }
                PickListMaterialInfo.TYPE_PICK_LIST_MATERIAL_INFO -> {
                    val dataBind = DataBindingUtil.bind<ItemPickListMaterialInfoBinding>(helper.itemView)
                    dataBind.info = item as PickListMaterialInfo
                }
            }
        }
    }
}