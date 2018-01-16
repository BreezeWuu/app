package com.zotye.wms.data.db.tables

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.zotye.wms.data.api.model.User

/**
 * Created by hechuangju on 2017/8/4 下午1:31.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User): Long

    @Query("SELECT * FROM user WHERE userId = :userId")
    fun getUser(userId: String): User?

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>
}