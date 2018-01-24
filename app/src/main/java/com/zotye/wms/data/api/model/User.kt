package com.zotye.wms.data.api.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.zotye.wms.data.db.Converters
import org.simpleframework.xml.Root


/**
 * Created by hechuangju on 2017/7/5.
 */
@Entity
@TypeConverters(Converters::class)
@Root(strict = false)

data class User(
        @PrimaryKey
        @SerializedName("userId") var userId: String = "", //819e5d46-1547-4be5-82a1-b6bc2cde312e
        @SerializedName("resources") var resources: List<Resource> = listOf()
)

data class Resource(
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
)

enum class ResourceType(val code: String) {
    PALLETRECV("PALLETRECV"),//组托收货
    PACKAGERECV("PACKAGERECV"),//包装收货
    ThreePLDELIVERY("3PLDELIVERY"),//创建装车单
    ThreePLPRCREATE("3PLPRCREATE"),//外检不良品生成拣配单
    ThreePLSOLDOUT("3PLSOLDOUT"),//下架确认
    ANDROID("ANDROID"),//移动端功能
    ThreePLRECVCONFIRM("3PLRECVCONFIRM"),//确认收货
    ThreePLADJUST("3PLADJUST"),//库位调整
    STQUERY("STQUERY"),//包装查询
    Unknown("");


    companion object {
        fun fromCode(code: String): ResourceType {
            ResourceType.values().forEach { resourceType ->
                if (resourceType.code == code)
                    return resourceType
            }
            return Unknown
        }
    }
}