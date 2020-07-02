package com.hanmajid.android.seed.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.hanmajid.android.seed.api.AppService
import com.hanmajid.android.seed.db.AppDatabase
import com.hanmajid.android.seed.db.RemoteKeys
import com.hanmajid.android.seed.model.Chat
import com.hanmajid.android.seed.util.PagingUtil
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class ChatRemoteMediator(
    private val service: AppService,
    private val database: AppDatabase
) : RemoteMediator<Int, Chat>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Chat>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                Log.wtf(TAG, "Refresh")
                val remoteKeys = PagingUtil.getRemoteKeyClosestToCurrentPosition(
                    state as PagingState<Int, PagingItem>,
                    database
                )
                remoteKeys?.nextKey?.minus(1) ?: 0
            }
            LoadType.PREPEND -> {
                Log.wtf(TAG, "Prepend")
                val remoteKeys = PagingUtil.getRemoteKeyForFirstItem(
                    state as PagingState<Int, PagingItem>,
                    database
                )
                if (remoteKeys == null) {
                    // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    // If the remoteKeys are null, then we're an invalid state and we have a bug
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                // If the previous key is null, then we can't request more data
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                Log.wtf(TAG, "Append")
                val remoteKeys = PagingUtil.getRemoteKeyForLastItem(
                    state as PagingState<Int, PagingItem>,
                    database
                )
                if (remoteKeys == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                val nextKey = remoteKeys.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.nextKey
            }
        }
        Log.wtf(TAG, "Page: $page")

        try {
            val response = service.getChats(page, state.config.pageSize)

            val chats = response
            val endOfPaginationReached = page == 1
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.chatDao().clearChats()
                }
                val prevKey = if (page == 0) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = chats.map {
                    RemoteKeys(
                        remoteId = it.id,
                        type = TAG,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                database.remoteKeysDao().insertAll(keys)
                database.chatDao().insertAll(chats)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    companion object {
        private const val TAG = "ChatRemoteMediator"
    }
}