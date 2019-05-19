package com.zotye.wms.ui.manualboard

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import com.zotye.wms.R
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.AppApiHelper
import com.zotye.wms.data.api.model.*
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_goods_receive_group.*
import kotlinx.android.synthetic.main.fragment_manual_material_require.*
import kotlinx.android.synthetic.main.fragment_manual_material_require.packageInput
import kotlinx.android.synthetic.main.fragment_manual_material_require.packageScanner
import kotlinx.android.synthetic.main.fragment_manual_material_require.view.*
import kotlinx.android.synthetic.main.outbound_check_info.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/05/17
 */
class ManualMaterialRequireFragment : BaseFragment(), ManualMaterialRequireContract.ManualMaterialRequireView, ScannerDelegate {

    private var mBackgroundTimer: Timer? = null
    private val mHandler = Handler()
    @Inject
    lateinit var apiHelper: AppApiHelper
    @Inject
    lateinit var presenter: ManualMaterialRequireContract.ManualMaterialRequirePresenter

    companion object {
        fun newInstance(title: String): ManualMaterialRequireFragment {
            val fragment = ManualMaterialRequireFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manual_material_require, container, false)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            startSearchTimer(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = arguments?.getString("title") ?: ""
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        materialIdEditText.addTextChangedListener(textWatcher)
        packageScanner.onClick {
            val fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@ManualMaterialRequireFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        packageInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.dialog_pda_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.packageCode)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_package_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                presenter.getPackageInfo(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
        submitButton.setOnClickListener {
            var boxNumber = 0
            var needNumber = 0
            try {
                boxNumber = boxNumberEditText.text.toString().toInt()
                needNumber = needNumberEditText.text.toString().toInt()
            } catch (e: Exception) {

            }
            if (materialIdEditText.text.toString().isEmpty()) {
                showMessage("请输入物料号！")
            } else if (supplierSpinner.selectedItem == null) {
                showMessage("请选择供应商！")
            } else if (boxNumber < 0) {
                showMessage("请正确填写箱数！")
            } else if (needNumber <= 0) {
                showMessage("请正确填写需求量！")
            } else if (gongWeiSpinner.selectedItem == null) {
                showMessage("请选择工位！")
            } else {
                val mProduceBean = ProduceBean()
                mProduceBean.modus = "0"
                mProduceBean.type = "1"
                mProduceBean.details = arrayListOf()
                mProduceBean.details.add(ProduceDetailBean().apply {
                    materialId = materialIdEditText.text.toString()
                    materialNo = manuaMaterialInfo?.materialNum
                    num = needNumber.toString()
                    cellNum = boxNumber
                    station = (gongWeiSpinner.selectedItem as Station).code
                    supplierCode = (supplierSpinner.selectedItem as Supplier).code
                })
                presenter.saveManualMaterialRequire(mProduceBean)
            }
        }
    }

    override fun getPackageInfo(fromJson: PackageInfo) {
        materialIdEditText.setText(fromJson.materialNum)
    }

    override fun succeed(result: String) {
        presenter.getPackageInfo(result)
    }

    private fun startSearchTimer(materialShort: String) {
        mBackgroundTimer?.cancel()
        mBackgroundTimer = Timer()
        mBackgroundTimer?.schedule(SearchWordTask(materialShort), 300)
    }

    private inner class SearchWordTask(val materialShort: String) : TimerTask() {

        override fun run() {
            mHandler.post { searchWord(materialShort) }
        }
    }

    private var searchCall: Call<ApiResponse<List<MaterialVatague>>>? = null
    private fun searchWord(mWordText: String?) {
        if (searchCall?.isCanceled == false)
            searchCall?.cancel()
        mWordText?.apply {
            if (trim().isNotBlank()) {
                searchCall = apiHelper.materialVatagueQuery(mWordText)
                searchCall?.enqueue(object : Callback<ApiResponse<List<MaterialVatague>>> {
                    override fun onFailure(call: Call<ApiResponse<List<MaterialVatague>>>, t: Throwable) {
                        if (call.isCanceled) return
                        context?.let {
                            showMessage(t.message ?: "")
                        }
                    }

                    override fun onResponse(call: Call<ApiResponse<List<MaterialVatague>>>, response: Response<ApiResponse<List<MaterialVatague>>>) {
                        response.body()?.apply {
                            if (isSucceed() && context != null) {
                                materialIdEditText?.setAdapter(ArrayAdapter<MaterialVatague>(context!!, android.R.layout.simple_spinner_dropdown_item, data?.toMutableList()
                                        ?: mutableListOf()))
                                materialIdEditText?.setOnItemClickListener { _, _, index, l ->
                                    data?.get(index)?.apply {
                                        presenter.queryMateiralInfos(materialNum)
                                        hideKeyboard(materialIdEditText)
                                    }
                                }
                                materialIdEditText?.showDropDown()
                            } else {
                                showMessage(message)
                            }
                        }
                    }
                })
            } else {
                materialNameLayout.visibility = View.GONE
//                showResultMessage("")
//                (recyclerView.adapter as? BookUnitWordAdapter)?.setNewData(null)
            }
        }
        mBackgroundTimer?.cancel()
    }

    private var manuaMaterialInfo: ManuaMaterialInfo? = null

    override fun queryMateiralInfos(storagePackageMaterialInfoList: ManuaMaterialInfo) {
        manuaMaterialInfo = storagePackageMaterialInfoList
        materialIdEditText?.removeTextChangedListener(textWatcher)
        materialIdEditText?.setText(storagePackageMaterialInfoList.materialId)
        materialIdEditText?.setSelection(storagePackageMaterialInfoList.materialId.length)
        materialIdEditText?.dismissDropDown()
        materialNameLayout.visibility = View.VISIBLE
        materialNameLayout.materialName.text = storagePackageMaterialInfoList.materialName
        materialNameLayout.packageNumber.text = storagePackageMaterialInfoList.packageNum.toString()
        materialNameLayout.unitText.text = storagePackageMaterialInfoList.unit
        supplierSpinner?.apply {
            if (storagePackageMaterialInfoList.suppliers != null)
                adapter = ArrayAdapter<Supplier>(context!!, android.R.layout.simple_spinner_dropdown_item, storagePackageMaterialInfoList.suppliers.toMutableList())
        }
        gongWeiSpinner?.apply {
            if (storagePackageMaterialInfoList.stations != null)
                adapter = ArrayAdapter<Station>(context!!, android.R.layout.simple_spinner_dropdown_item, storagePackageMaterialInfoList.stations.toMutableList())
        }
        materialIdEditText?.addTextChangedListener(textWatcher)
    }

    override fun saveManualMaterialRequireSucceed(message: String) {
        showMessage(message)
        materialIdEditText?.removeTextChangedListener(textWatcher)
        materialIdEditText?.setText("")
        materialIdEditText?.addTextChangedListener(textWatcher)
        supplierSpinner.adapter = null
        gongWeiSpinner.adapter = null
        boxNumberEditText.setText("")
        needNumberEditText.setText("")
    }

    override fun onDestroyView() {
        presenter.onDetach()
        searchCall?.cancel()
        super.onDestroyView()
    }
}