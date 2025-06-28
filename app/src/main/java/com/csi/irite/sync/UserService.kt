package com.csi.irite.sync

import com.csi.irite.room.data.User
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @GET("user/list")
    fun getUsers(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<User>

    @GET("user/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>

    @POST("user")
    suspend fun createUser(@Body user: User): Response<User>

    @PUT("user/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): Response<User>

    @PUT("user/{id}/password")
    suspend fun updatePassword(@Path("id") id: Int, @Body request: User): Response<User>

    @DELETE("user/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<User>
}
