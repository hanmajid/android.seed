package com.hanmajid.android.seed.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hanmajid.android.seed.model.Chat

@Database(
    entities = [Chat::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}