package com.zotye.wms.data.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zotye.wms.data.api.model.Resource
import java.sql.Date


/**
 * Created by hechuangju on 2017/8/4 下午5:54.
 */
class Converters {
    @TypeConverter
    fun listToString(list: List<String>): String? = Gson().toJson(list)

    @TypeConverter
    fun stringToList(string: String): List<String>? = Gson().fromJson<List<String>>(string, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun listRoleToString(list: List<Resource>): String? = Gson().toJson(list)

    @TypeConverter
    fun stringToListRole(string: String): List<Resource>? = Gson().fromJson<List<Resource>>(string, object : TypeToken<List<Resource>>() {}.type)

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        value?.let { return Date(value) }
        return null
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        date?.let { return date.time }
        return null
    }
}