package com.hanmajid.android.seed.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey @field:SerializedName("id") val id: String,
    @field:SerializedName("text") val text: String,
//    @field:SerializedName("pictures") val pictures: List<String>?,
    @field:SerializedName("timestamp") val timestamp: String,

    // TODO: Refactor these into User.
    @field:SerializedName("userId") val userId: String,
    @field:SerializedName("userAvatarPath") val userAvatarPath: String?,
    @field:SerializedName("userName") val userName: String
)