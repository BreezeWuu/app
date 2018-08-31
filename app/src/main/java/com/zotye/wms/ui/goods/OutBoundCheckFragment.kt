package com.zotye.wms.ui.goods

import android.app.AlertDialog
import android.databinding.DataBindingUtil.bind
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.outcheck.OutBoundCheckDto
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.OutboundCheckInfoBinding
import com.zotye.wms.databinding.OutboundCheckBadNewsBinding
import com.zotye.wms.ui.common.BarCodeScannerFragment
import com.zotye.wms.ui.common.BaseFragment
import com.zotye.wms.ui.common.ScannerDelegate
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_outbound_check.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject
import com.zotye.wms.data.api.model.outcheck.OutBoundBadNewsDto

/**
 * Created by yyf on 2018/08/25
 */
class OutBoundCheckFragment: BaseFragment(), ScannerDelegate, OutBoundCheckContract.OutBoundCheckView {


    override fun showDiaLog(msg: String?) {
        AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_picklist_code).setMessage(msg).setNegativeButton(R.string.ok,null) .show()
    }

    @Inject
    lateinit var presenter : OutBoundCheckContract.OutBoundCheckPresenter

    companion object {
        fun newInstance(title: String): OutBoundCheckFragment {
            val fragment = OutBoundCheckFragment()
            val bundle = Bundle()
            bundle.putString("title", title)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_outbound_check, container, false)
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
        outBoundCheckScanner.onClick {
            var fragment = BarCodeScannerFragment()
            fragment.setScannerDelegate(this@OutBoundCheckFragment)
            fragmentManager!!.beginTransaction().add(R.id.main_content, fragment).addToBackStack(null).commit()
        }
        outBoundCheckInput.onClick {
            val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.outbound_pad_code_input, null)
            val editText = codeInputView.findViewById<EditText>(R.id.outBoundCode)
            editText.setHint(R.string.pick_code)
            AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_picklist_code).setView(codeInputView).setNegativeButton(R.string.ok) { _, _ ->
                succeed(editText.text.toString())
                hideKeyboard(editText)
            }.setPositiveButton(R.string.cancel, null).show()
            showKeyboard(editText)
        }
    }

    override fun succeed(result: String) {
        presenter.getOutBoundCheckInfoByCode(result)
    }

    override fun getOutBoundCheckInfoByCode(data: List<OutBoundCheckDto>) {
        toolbar_base.titleResource = R.string.un_receive_pick_list
        outBoundCheckView.layoutManager = LinearLayoutManager(context)
        val adapter = outBoundCheckAdapter(presenter)
        outBoundCheckView.adapter = adapter
        adapter.setNewData(data)

    }

    override fun getBadGoodsNewsInfo(data: OutBoundBadNewsDto?,outBouncCheck:OutBoundCheckDto) {
        val codeInputView = LayoutInflater.from(getContext()!!).inflate(R.layout.outbound_check_bad_news, null)
        val badNum = codeInputView.findViewById<EditText>(R.id.badNum)
        val badNews = codeInputView.findViewById<EditText>(R.id.badNews)
        badNum.setText(data?.num)
        badNews.setText(data?.reason)
        AlertDialog.Builder(getContext()!!).setTitle(R.string.action_input_picklist_code).setView(codeInputView)
                .setNegativeButton(R.string.ok) { app, _ ->
                    if(badNum.text.toString().toInt()<outBouncCheck.totalNum!!.toInt() && badNum.text.toString().toInt() >= 0 && badNum.text.toString() != null && badNum.text.toString() != ""){
                        presenter.poerBadGoodsNewsInfo(outBouncCheck,badNum.text.toString(),badNews.text.toString())
                    } else {
                        var dialog:AlertDialog.Builder
                        dialog = AlertDialog.Builder(context)
                        dialog.setMessage("不良品数量有误,请重新输入!!!")
                        dialog.setPositiveButton(R.string.ok,null).show()
                    }
        }.setNeutralButton("删除"){
            _, _ ->
                    presenter.delBadGoodsNewsInfo(outBouncCheck)
        }.setPositiveButton(R.string.cancel, null).show()
        showKeyboard(badNews)
        showKeyboard(badNum)
    }

    class outBoundCheckAdapter(var pre:OutBoundCheckContract.OutBoundCheckPresenter): BaseQuickAdapter<OutBoundCheckDto, BaseViewHolder>(R.layout.outbound_check_info){
        //初始化FragmentDataBindingComponent
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: OutBoundCheckDto) {
            val dataBind = bind<OutboundCheckInfoBinding>(helper.itemView, fragmentDataBindingComponent)
            //绑定数据  ???
            dataBind?.info = item
            //视图绑定onclick事件
            //helper.addOnClickListener(R.id.openNews)
            helper.getView<Button>(R.id.openNews).onClick { _ ->
                //pre.getBadGoodsNewsInfo(item.pickNum!!,item.materialId!!,item.supplierId!!,item.batchNum)
                pre.getBadGoodsNewsInfo(item)
            }
        }
    }

    class badGoodsNewsAdapter: BaseQuickAdapter<OutBoundBadNewsDto, BaseViewHolder>(R.layout.outbound_check_bad_news){
        //初始化FragmentDataBindingComponent
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: OutBoundBadNewsDto) {
            val dataBind = bind<OutboundCheckBadNewsBinding>(helper.itemView, fragmentDataBindingComponent)
            //绑定数据
            dataBind?.info = item
        }
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }
}