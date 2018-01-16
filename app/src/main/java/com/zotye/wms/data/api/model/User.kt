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