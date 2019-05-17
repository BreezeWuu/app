package com.zotye.wms.ui.manualboard

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.zotye.wms.R
import com.zotye.wms.data.api.ApiResponse
import com.zotye.wms.data.api.AppApiHelper
import com.zotye.wms.data.api.model.*
import com.zotye.wms.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_manual_material_require.*
import kotlinx.android.synthetic.main.outbound_check_info.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

/**
 * Created by hechuangju on 2019/05/17
 */
class ManualMaterialRequireFragment : BaseFragment(), ManualMaterialRequireContract.ManualMaterialRequireView {

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
//                showResultMessage("")
//                (recyclerView.adapter as? BookUnitWordAdapter)?.setNewData(null)
            }
        }
        mBackgroundTimer?.cancel()
    }

    override fun queryMateiralInfos(storagePackageMaterialInfoList: ManuaMaterialInfo) {
        materialIdEditText?.removeTextChangedListener(textWatcher)
        materialIdEditText?.setText(storagePackageMaterialInfoList.materialId)
        materialIdEditText?.setSelection(storagePackageMaterialInfoList.materialId.length)
        materialIdEditText?.addTextChangedListener(textWatcher)
        supplierSpinner?.apply {
            adapter = ArrayAdapter<Supplier>(context!!, android.R.layout.simple_spinner_dropdown_item, storagePackageMaterialInfoList.suppliers.toMutableList())
        }
        gongWeiSpinner?.apply {
            adapter = ArrayAdapter<Station>(context!!, android.R.layout.simple_spinner_dropdown_item, storagePackageMaterialInfoList.stations.toMutableList())
        }
    }

    override fun onDestroyView() {
        presenter.onDetach()
        searchCall?.cancel()
        super.onDestroyView()
    }
}