package com.hanmajid.android.seed.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hanmajid.android.seed.api.AppService
import com.hanmajid.android.seed.db.AppDatabase
import com.hanmajid.android.seed.model.Chat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val service: AppService,
    private val database: AppDatabase
) {

    fun getChatsStream(): Flow<PagingData<Chat>> {
//        // Use API only.
//        return Pager(
//            config = PagingConfig(pageSize = 20),
//            pagingSourceFactory = { ChatPagingSource(service) }
//        ).flow

        val pagingSourceFactory = { database.chatDao().getAllChats() }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
            remoteMediator = ChatRemoteMediator(
                service,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 2
    }
}