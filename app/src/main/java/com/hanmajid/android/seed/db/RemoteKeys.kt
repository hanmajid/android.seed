package com.hanmajid.android.seed.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val chatId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)