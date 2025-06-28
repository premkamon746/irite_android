package com.csi.irite.sync

import com.csi.irite.AuthInterceptor
import com.csi.irite.BuildConfig
import okhttp3.OkHttpClient

object RetrofitClient {
    private const val BASE_URL = "http://${BuildConfig.host}:3200" // localhost for Android emulator

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor()) // Add auth interceptor
        .build()


    val instance: ApiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}