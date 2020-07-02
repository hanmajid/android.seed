package com.hanmajid.android.seed.data

import com.hanmajid.android.seed.api.AppService
import com.hanmajid.android.seed.db.AppDatabase
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val service: AppService,
    private val database: AppDatabase
) {

//    fun getPostsStream(): Flow<PagingData<Post>> {
//        val pagingSourceFactory = {
//            database.postDao().getPagedPosts()
//        }
//        return Pager(
//            config = PagingConfig(
//                pageSize = NETWORK_PAGE_SIZE
//            ),
//            remoteMediator = PostRemoteMediator(
//                service,
//                database
//            ),
//            pagingSourceFactory = pagingSourceFactory
//        ).flow
//    }

    companion object {
        private const val TAG = "PostRepository"
        private const val NETWORK_PAGE_SIZE = 20
    }
}