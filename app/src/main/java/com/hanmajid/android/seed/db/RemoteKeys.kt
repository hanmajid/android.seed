package com.hanmajid.android.seed.db

import androidx.room.Entity

@Entity(
    tableName = "remote_keys",
    primaryKeys = [
        "remoteId",
        "type"
    ]
)
data class RemoteKeys(
    val remoteId: Long,
    val type: String,
    val prevKey: Int?,
    val nextKey: Int?
)