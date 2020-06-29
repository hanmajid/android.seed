package com.hanmajid.android.seed.model

import com.google.gson.annotations.SerializedName

data class Chat(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("avatarPath") val avatarPath: String,
    @field:SerializedName("lastMessage") val lastMessage: Message
)

data class Message(
    @field:SerializedName("content") val content: String
)