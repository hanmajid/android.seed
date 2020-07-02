package com.hanmajid.android.seed.data

//@OptIn(ExperimentalPagingApi::class)
//class PostRemoteMediator(
//    private val service: AppService,
//    private val database: AppDatabase
//): RemoteMediator<Int, Post>() {
//    override suspend fun load(loadType: LoadType, state: PagingState<Int, Post>): MediatorResult {
//        val page = when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                remoteKeys?.nextKey?.minus(1) ?: 0
//            }
//            LoadType.PREPEND -> {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                if (remoteKeys == null) {
//                    // The LoadType is PREPEND so some data was loaded before,
//                    // so we should have been able to get remote keys
//                    // If the remoteKeys are null, then we're an invalid state and we have a bug
//                    throw InvalidObjectException("Remote key and the prevKey should not be null")
//                }
//                // If the previous key is null, then we can't request more data
//                val prevKey = remoteKeys.prevKey
//                if (prevKey == null) {
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                }
//                remoteKeys.prevKey
//            }
//            LoadType.APPEND -> {
//                val remoteKeys = getRemoteKeyForLastItem(state)
//                if (remoteKeys == null) {
//                    throw InvalidObjectException("Remote key should not be null for $loadType")
//                }
//                val nextKey = remoteKeys.nextKey
//                if (nextKey == null) {
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                }
//                remoteKeys.nextKey
//            }
//        }
//
//        try {
//            val response = service.getChats(page, state.config.pageSize)
//
//            val chats = response
//            val endOfPaginationReached = true
//            database.withTransaction {
//                // clear all tables in the database
//                if (loadType == LoadType.REFRESH) {
//                    database.remoteKeysDao().clearRemoteKeys()
//                    database.chatDao().clearChats()
//                }
//                val prevKey = if (page == 0) null else page - 1
//                val nextKey = if (endOfPaginationReached) null else page + 1
//                val keys = chats.map {
//                    RemoteKeys(
//                        remoteId = it.id,
//                        type = ChatRemoteMediator.TAG,
//                        prevKey = prevKey,
//                        nextKey = nextKey
//                    )
//                }
//                database.remoteKeysDao().insertAll(keys)
//                database.chatDao().insertAll(chats)
//            }
//            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
//        } catch (e: IOException) {
//            return MediatorResult.Error(e)
//        } catch (e: HttpException) {
//            return MediatorResult.Error(e)
//        }
//    }
//}