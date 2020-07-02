package com.hanmajid.android.seed.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanmajid.android.seed.model.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getPagedPosts(): PagingSource<Int, Post>

    @Query("DELETE FROM posts")
    fun clearPosts()
}