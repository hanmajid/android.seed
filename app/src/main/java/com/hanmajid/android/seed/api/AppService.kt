package com.hanmajid.android.seed.api

import com.hanmajid.android.seed.model.Chat
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {
    @GET("chats")
    suspend fun getChats(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<Chat>

    companion object {
        const val BASE_URL = "https://hanmajid.com/api/"
    }
}