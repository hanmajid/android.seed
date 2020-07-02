package com.hanmajid.android.seed.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.hanmajid.android.seed.data.PagingItem

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey @field:SerializedName("id") override val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("avatarPath") val avatarPath: String,
    @Embedded @field:SerializedName("lastMessage") val lastMessage: Message?
) : PagingItem

data class Message(
    @field:SerializedName("content") val content: String
)