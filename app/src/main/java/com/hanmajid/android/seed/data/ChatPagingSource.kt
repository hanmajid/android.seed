package com.hanmajid.android.seed.data

import androidx.paging.PagingSource
import com.hanmajid.android.seed.api.AppService
import com.hanmajid.android.seed.model.Chat
import retrofit2.HttpException
import java.io.IOException

class ChatPagingSource(
    private val service: AppService
) : PagingSource<Int, Chat>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Chat> {
        val position = params.key ?: 0
        return try {
            val response = service.getChats(position, params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (position == 0) null else position - 1,
//                nextKey = if (response.isEmpty()) null else position + 1
                nextKey = if (position >= 5) null else position + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}