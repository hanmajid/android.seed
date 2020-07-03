package com.hanmajid.android.seed.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hanmajid.android.seed.model.Chat
import com.hanmajid.android.seed.model.Post

@Database(
    entities = [
        Chat::class,
        RemoteKeys::class,
        Post::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun postDao(): PostDao
}