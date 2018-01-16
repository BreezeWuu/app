package com.zotye.wms.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.zotye.wms.data.api.model.User
import com.zotye.wms.data.db.tables.UserDao

/**
 * Created by hechuangju on 2017/8/4 下午1:27.
 */
@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

}