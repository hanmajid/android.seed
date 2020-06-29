package com.hanmajid.android.seed.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hanmajid.android.seed.api.AppService
import com.hanmajid.android.seed.model.Chat
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val service: AppService
) {

    fun getChatsStream(): Flow<PagingData<Chat>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ChatPagingSource(service) }
        ).flow
    }
}