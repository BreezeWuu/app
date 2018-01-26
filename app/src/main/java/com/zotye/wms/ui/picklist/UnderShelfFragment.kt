package com.zotye.wms.ui.picklist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.Gson
import com.zotye.wms.R
import com.zotye.wms.data.api.model.*
import com.zotye.wms.databinding.ItemPickListInfoUnderShelfBinding
import com.zotye.wms.databinding.ItemPickListMaterialInfoBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_pick_list_switcher.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * Created by hechuangju on 2018/01/25
 */
class UnderShelfFragment : BaseFragment(), UnderShelfContract.UnderShelfView, ScannerDelegate {
    private lateinit var adapter: PickListAdapter

    companion object {
        fun newInstance(title: String): UnderShelfFragment {
            val fragment = UnderShelfFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var presenter: UnderShelfContract.UnderShelfPresenter

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pick_list_switcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_under_shelf)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        pickListScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@UnderShelfFragment)
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
        adapter = PickListAdapter(null)
        val emptyView = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        emptyView.find<TextView>(R.id.text_error).text = getString(R.string.pick_list_empty)
        adapter.emptyView = emptyView
        pickListRecyclerView.layoutManager = LinearLayoutManager(context)
        pickListRecyclerView.adapter = adapter
        adapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.deleteButton -> {
                    val item = adapter.getItem(position) as PickListInfo
                    AlertDialog.Builder(view!!.context).setMessage(view.resources.getString(R.string.delete_pic_item_info, item.code)).setNegativeButton(R.string.ok) { _, _ ->
                        adapter.remove(position)
                        if (viewFlipper.childCount == 2)
                            viewFlipper.removeViewAt(1)
                    }.setPositiveButton(R.string.cancel, null).show()
                }
                R.id.addPackageButton -> {
                    viewFlipper.showNext()
                    updateTitle()
                }
            }
        }
    }

    override fun succeed(result: String) {
        presenter.getPickListInfoByCode(result)
    }

    override fun getPickListInfo(pickListInfo: PickListInfo) {
        if (viewFlipper.childCount == 2)
            viewFlipper.removeViewAt(1)
        pickListInfo.subItems = pickListInfo.materialInfoList
        adapter.setNewData(null)
        adapter.addData(pickListInfo)

        val pickListInfoAddPackageView = LayoutInflater.from(context).inflate(R.layout.layout_pick_list_add_package, viewFlipper, false)
        pickListInfoAddPackageView.find<Button>(R.id.packageCodeInput).onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                presenter.getStorageUnitDetailInfoByCode(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        pickListInfoAddPackageView.find<Button>(R.id.packageCodeScanner).onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(object : ScannerDelegate {
                override fun succeed(result: String) {
                    presenter.getStorageUnitDetailInfoByCode(result)
                }
            })
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }

        viewFlipper.addView(pickListInfoAddPackageView)
    }

    override fun getBarCodeInfo(barCodeInfo: BarcodeInfo?) {
        barCodeInfo?.let { info ->
            val barcodeType = BarCodeType.fromCodeType(info.barCodeType)
            barcodeType?.let {
                when (it) {
                    BarCodeType.Package -> {
                        getStorageUnitPackage(Gson().fromJson<StorageUnitPackageInfo>(info.barCodeInfo, StorageUnitPackageInfo::class.java))
                    }
                    BarCodeType.Pallet -> {
                        getStorageUnitPallet(Gson().fromJson<StorageUnitPalletInfo>(info.barCodeInfo, StorageUnitPalletInfo::class.java))
                    }
                }
            }
        }
    }

    private fun getStorageUnitPackage(packageInfo: StorageUnitPackageInfo) {

    }

    private fun getStorageUnitPallet(palletInfo: StorageUnitPalletInfo) {

    }
    class PickListAdapter(data: MutableList<MultiItemEntity>?) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

        init {
            addItemType(PickListInfo.TYPE_PICK_LIST, R.layout.item_pick_list_info_under_shelf)
            addItemType(PickListMaterialInfo.TYPE_PICK_LIST_MATERIAL_INFO, R.layout.item_pick_list_material_info);
        }

        override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
            when (helper.itemViewType) {
                PickListInfo.TYPE_PICK_LIST -> {
                    val dataBind = DataBindingUtil.bind<ItemPickListInfoUnderShelfBinding>(helper.itemView)
                    dataBind.info = item as PickListInfo
                    helper.getView<Button>(R.id.expandButton).onClick { view ->
                        if (item.isExpanded) {
                            collapse(helper.adapterPosition)
                        } else {
                            expand(helper.adapterPosition)
                        }
                    }
                    helper.getView<Button>(R.id.addPackageButton).visibility = View.VISIBLE
                    helper.addOnClickListener(R.id.addPackageButton)
                    helper.addOnClickListener(R.id.deleteButton)
                    helper.getView<Button>(R.id.expandButton).setText(if (item.isExpanded) R.string.picklist_material_collapse else R.string.picklist_material_expand)
                }
                PickListMaterialInfo.TYPE_PICK_LIST_MATERIAL_INFO -> {
                    val dataBind = DataBindingUtil.bind<ItemPickListMaterialInfoBinding>(helper.itemView)
                    dataBind.info = item as PickListMaterialInfo
                }
            }
        }
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    private fun updateTitle() {
        when (viewFlipper.displayedChild) {
            0 -> {
                toolbar_base.title = arguments?.getString("title") ?: getString(R.string.title_storage_unit_info)
            }
            1 -> {
                toolbar_base.titleResource = R.string.title_pick_list_add_package
            }
        }
    }

    override fun canBackPressed(): Boolean {
        return if (viewFlipper.displayedChild != 0) {
            viewFlipper.showPrevious()
            updateTitle()
            false
        } else
            true
    }
}