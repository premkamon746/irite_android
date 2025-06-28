package com.csi.irite.data.repo

import android.content.Context
import com.csi.irite.room.dao.UserDao
import com.csi.irite.room.data.LoginRequest
import com.csi.irite.room.data.User
import com.csi.irite.sync.ApiService

// AuthRepository.kt
class AuthRepository(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val context: Context
) {
    /*suspend fun login(username: String, password: String): Boolean {
        val response = apiService.login(LoginRequest(username, password))
        return if (response.isSuccessful) {
            response.body()?.let {
                TokenManager.saveTokens(it.accessToken, it.refreshToken)
                userDao.insertUser(it.user)
            }
            true
        } else false
    }*/

    suspend fun getCachedUser(): User? {
        return userDao.getLoggedInUser()
    }
}
