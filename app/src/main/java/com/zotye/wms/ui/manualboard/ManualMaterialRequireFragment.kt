package com.zotye.wms.ui.manualboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zotye.wms.R
import com.zotye.wms.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_manual_material_require.*

/**
 * Created by hechuangju on 2019/05/17
 */
class ManualMaterialRequireFragment : BaseFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        materialIdEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

}