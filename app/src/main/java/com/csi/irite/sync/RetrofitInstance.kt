package com.csi.irite.sync

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://103.76.181.219:3000/"

    val api: SyncApiService by lazy {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // or HEADERS
        }

        val interceptor = Interceptor { chain ->
            val original = chain.request()

            // Log or clone the original body
            val requestBody = original.body
            val buffer = Buffer()
            requestBody?.writeTo(buffer)
            val bodyString = buffer.readUtf8()

            // Log it
            Log.d("HTTP_BODY", bodyString)

            // Proceed with the original request (unchanged)
            chain.proceed(original)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, BooleanIntAdapter())
            .registerTypeAdapter(java.lang.Boolean::class.java, BooleanIntAdapter()) // For nullable
            .create()

        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SyncApiService::class.java)
    }

    val userApi: UserService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)
    }
}