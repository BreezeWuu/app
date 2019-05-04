package com.zotye.wms.data.api.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.zotye.wms.R
import com.zotye.wms.data.db.Converters
import com.zotye.wms.ui.manualboard.ManualBoardOutFragment
import com.zotye.wms.ui.picklist.ShelfConfirmFragment
import com.zotye.wms.ui.picklist.StoragePositionInfoFragment
import com.zotye.wms.ui.storageunit.StorageUnitOnlineFragment
import com.zotye.wms.util.Log


/**
 * Created by hechuangju on 2017/7/5.
 */
@Entity
@TypeConverters(Converters::class)
data class User(
        @PrimaryKey
        @SerializedName("userId") var userId: String = "", //819e5d46-1547-4be5-82a1-b6bc2cde312e
        @SerializedName("defaultFactoryCode") var defaultFactoryCode: String = "",
        @SerializedName("resources") var resources: List<Resource> = listOf()
)

class Resource(
        @SerializedName("name") var name: String = "", //部门信息维护
        @SerializedName("code") var code: String = "", //ORG-MAINTAIN
        @SerializedName("type") var type: String = "", //menu
        @SerializedName("desc") var desc: String = "", //null
        @SerializedName("sort") var sort: Int = 0, //1
        @SerializedName("level") var level: Int = 0, //2
        @SerializedName("icon") var icon: String = "", //null
        @SerializedName("url") var url: String = "", //org/init
        @SerializedName("method") var method: String = "", //get
        @SerializedName("leaf") var leaf: Boolean = false //true
) {
    fun getResourceDrawable(): Int {
        return when (ResourceType.fromCode(code)) {
            ResourceType.PALLETRECV,ResourceType.APPHB -> {
                R.drawable.ic_group_receipt
            }
            ResourceType.PACKAGERECV -> {
                R.drawable.ic_package_receipt
            }
            ResourceType.STQUERY, ResourceType.CKPCD, ResourceType.SJXXCX -> {
                R.drawable.ic_package_query
            }
            ResourceType.ThreePLDELIVERY -> {
                R.drawable.ic_loading_list
            }
            ResourceType.ThreePLPRCREATE, ResourceType.CB001 -> {
                R.drawable.ic_check_bad
            }
            ResourceType.ThreePLSOLDOUT -> {
                R.drawable.ic_under_shell_confirm
            }
            ResourceType.PRODUCE_ANDROID -> {
                R.drawable.ic_get_product
            }
            ResourceType.ThreePLRECVCONFIRM -> {
                R.drawable.ic_receipt_confirm
            }
            ResourceType.ThreePLADJUST -> {
                R.drawable.ic_unit_modify
            }
            ResourceType.STRICT_RECV -> {
                R.drawable.ic_loading_list_receipt
            }
            ResourceType.NORMAL_RECV ,ResourceType.JHDZDSHAPP-> {
                R.drawable.ic_delivery_note_receipt
            }
            ResourceType.OUTBOUND_CHECK -> {
                R.drawable.ic_outcheck
            }
            ResourceType.TEST_RECV -> {
                R.mipmap.ic_launcher
            }
            ResourceType.STSLINFO_CHECK -> {
                R.drawable.ic_under_shell_confirm
            }
            ResourceType.PICKSTATIONINFO_CHECK -> {
                R.drawable.ic_receipt_confirm
            }
            ResourceType.MANUAL_BOARD_OUT -> {
                R.drawable.ic_get_product
            }
            else -> {
                R.mipmap.ic_launcher
            }
        }
    }
}

enum class ResourceType(val code: String) {
    PALLETRECV("PALLETRECV"),//组托收货
    PACKAGERECV("PACKAGERECV"),//包装收货
    ThreePLDELIVERY("3PLDELIVERY"),//创建装车单
    ThreePLPRCREATE("3PLPRCREATE"),//外检不良品生成拣配单
    ThreePLSOLDOUT("3PLSOLDOUT"),//下架确认
    PRODUCE_ANDROID("PRODUCE_ANDROID"),//生产领料
    ANDROID("ANDROID"),//移动端功能
    ThreePLRECVCONFIRM("3PLRECVCONFIRM"),//确认收货
    ThreePLADJUST("3PLADJUST"),//库位调整
    STQUERY("STQUERY"),//包装查询
    STRICT_RECV("STRICT_RECV"),//装车单收货
    NORMAL_RECV("NORMAL_RECV"),//交货单收货
    OUTBOUND_CHECK("OUTBOUND_CHECK"),//出库确认

    STSLINFO_CHECK("sjqr"),//上架确认
    PICKSTATIONINFO_CHECK("sxqr"),//上线确认
    MANUAL_BOARD_OUT("sgkbckapp"),//手工看板出库
    SJXXCX("sjxxcx"),//上架信息查询
    CKPCD("ckpcd1"),//查看配车单
    TEST_RECV("TEST_RECV"),//测试
    CB001("CB001"),//拆包
    APPHB("APPHB"),//合包
    JHDZDSHAPP("jhdzdshAPP"),//交货单整单收货
    Unknown("");


    companion object {
        fun fromCode(code: String): ResourceType {
            ResourceType.values().forEach { resourceType ->
                if (resourceType.code == code)
                    return resourceType
            }
            return ResourceType.Unknown
        }
    }
}