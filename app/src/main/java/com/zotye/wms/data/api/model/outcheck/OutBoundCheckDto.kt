package com.zotye.wms.data.api.model.outcheck



class OutBoundCheckDto{

    var materialId:String? = null

    var materialNum:String? = null


    var wrkst:String? = null

    var desc:String? = null

    var unit:String? = null

    var supplierId:String? = null

    var supplierName:String? = null

    lateinit var batchNum:String

    var totalNum:String? = null

    var pickNum:String? = null

    var badNum:String? = null

    var badNews:String? = null
}