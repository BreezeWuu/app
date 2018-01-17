package com.zotye.wms.data.db

import com.zotye.wms.data.api.model.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by hechuangju on 2017/8/29 下午12:29.
 */
@Singleton
class AppDbHelper @Inject constructor(private val database: AppDatabase) : DbHelper {

    override fun insertUser(user: User) = database.getUserDao().insert(user)

    override fun getAllUsers() = database.getUserDao().getAllUsers()

    override fun getUser(userId: String) = database.getUserDao().getUser(userId)

    override fun updateUser(user: User) = database.getUserDao().updateUser(user)
}