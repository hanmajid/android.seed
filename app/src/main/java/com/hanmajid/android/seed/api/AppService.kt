package com.hanmajid.android.seed.api

import com.hanmajid.android.seed.model.Chat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {
    @GET("chats")
    suspend fun getChats(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<Chat>

    companion object {
        private const val BASE_URL = "https://hanmajid.com/api/"

        fun create(): AppService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AppService::class.java)
        }
    }
}