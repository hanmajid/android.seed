package com.hanmajid.android.seed.util

import androidx.paging.PagingState
import com.hanmajid.android.seed.data.PagingItem
import com.hanmajid.android.seed.db.AppDatabase
import com.hanmajid.android.seed.db.RemoteKeys

class PagingUtil {
    companion object {
        @JvmStatic
        suspend fun getRemoteKeyForLastItem(
            state: PagingState<Int, PagingItem>,
            database: AppDatabase
        ): RemoteKeys? {
            // Get the last page that was retrieved, that contained items.
            // From that last page, get the last item
            return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { item ->
                    // Get the remote keys of the last item retrieved
                    database.remoteKeysDao().getByRemoteId(item.id)
                }
        }

        @JvmStatic
        suspend fun getRemoteKeyForFirstItem(
            state: PagingState<Int, PagingItem>,
            database: AppDatabase
        ): RemoteKeys? {
            // Get the first page that was retrieved, that contained items.
            // From that first page, get the first item
            return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { item ->
                    // Get the remote keys of the first items retrieved
                    database.remoteKeysDao().getByRemoteId(item.id)
                }
        }

        @JvmStatic
        suspend fun getRemoteKeyClosestToCurrentPosition(
            state: PagingState<Int, PagingItem>,
            database: AppDatabase
        ): RemoteKeys? {
            // The paging library is trying to load data after the anchor position
            // Get the item closest to the anchor position
            return state.anchorPosition?.let { position ->
                state.closestItemToPosition(position)?.id?.let { id ->
                    database.remoteKeysDao().getByRemoteId(id)
                }
            }
        }
    }
}