package com.hanmajid.android.seed.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hanmajid.android.seed.model.Chat

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<Chat>)

    @Query("SELECT * FROM chats ORDER BY id ASC")
    fun getAllChats(): PagingSource<Int, Chat>

    @Query("DELETE FROM chats")
    fun clearChats()
}