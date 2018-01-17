package com.zotye.wms.data.db

import com.zotye.wms.data.api.model.User

/**
 * Created by hechuangju on 2017/8/29 下午12:22.
 */
interface DbHelper {

    fun insertUser(user: User): Long

    fun getUser(userId: String): User?

    fun getAllUsers(): List<User>

    fun updateUser(user: User): Int

}