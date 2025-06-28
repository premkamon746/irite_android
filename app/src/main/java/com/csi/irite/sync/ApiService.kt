package com.csi.irite.sync

import com.csi.irite.room.data.LoginRequest
import com.csi.irite.room.data.LoginResponse
import com.csi.irite.room.data.RefreshRequest
import com.csi.irite.room.data.RefreshResponse
import com.csi.irite.room.data.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST



interface ApiService {
    /*@POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>*/

    @POST("/api/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/refresh")
    fun refresh(@Body token: RefreshRequest): Call<RefreshResponse>

    @GET("api/protected")
    fun getProtectedData(@Header("Authorization") authHeader: String): Call<Any>
}