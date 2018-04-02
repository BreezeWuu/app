package com.zotye.wms.ui.goods

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zotye.wms.R
import com.zotye.wms.data.api.model.receipt.ReceiveDetailDto
import com.zotye.wms.data.binding.FragmentDataBindingComponent
import com.zotye.wms.databinding.ItemDeliveryNoteMaterialInfoBinding
import com.zotye.wms.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.simple_recycler_view.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource

/**
 * Created by hechuangju on 2018/03/08
 */
class DeliveryNoteChildDetailFragment : BaseFragment() {

    companion object {
        fun newInstance(receiveDetailDto: ReceiveDetailDto): DeliveryNoteChildDetailFragment {
            val fragment = DeliveryNoteChildDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable("data", receiveDetailDto)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.simple_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveDetailDto: ReceiveDetailDto = arguments!!.getSerializable("data") as ReceiveDetailDto
        toolbar_base.visibility = View.VISIBLE
        toolbar_base.title = receiveDetailDto.materialName + getString(R.string.child_material_detail)
        toolbar_base.navigationIconResource = R.drawable.ic_arrow_back
        toolbar_base.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MaterialDetailAdapter(receiveDetailDto)
        recyclerView.adapter = adapter
        adapter.setNewData(receiveDetailDto.child)
    }

    class MaterialDetailAdapter(private val receiveDetailDto: ReceiveDetailDto) : BaseQuickAdapter<ReceiveDetailDto, BaseViewHolder>(R.layout.item_delivery_note_material_info) {
        private var fragmentDataBindingComponent: FragmentDataBindingComponent = FragmentDataBindingComponent()
        override fun convert(helper: BaseViewHolder, item: ReceiveDetailDto) {
            val dataBind = DataBindingUtil.bind<ItemDeliveryNoteMaterialInfoBinding>(helper.itemView, fragmentDataBindingComponent)
            dataBind?.info = item
            dataBind?.parent = receiveDetailDto
        }
    }
}
