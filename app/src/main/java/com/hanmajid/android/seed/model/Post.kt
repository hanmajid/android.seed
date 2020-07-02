package com.hanmajid.android.seed.model

data class Post(
    val id: Long,
    val text: String,
    val pictures: List<String>?,
    val timestamp: String
)