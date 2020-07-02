package com.hanmajid.android.seed.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey @field:SerializedName("id") val id: Long,
    @field:SerializedName("text") val text: String,
//    @field:SerializedName("pictures") val pictures: List<String>?,
    @field:SerializedName("timestamp") val timestamp: String
)