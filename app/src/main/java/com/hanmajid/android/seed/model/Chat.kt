package com.hanmajid.android.seed.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("avatarPath") val avatarPath: String,
    @Embedded @field:SerializedName("lastMessage") val lastMessage: Message?
)

data class Message(
    @field:SerializedName("content") val content: String
)