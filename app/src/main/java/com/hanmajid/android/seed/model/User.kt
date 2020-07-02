package com.hanmajid.android.seed.model

data class User(
    val id: Long,
    val email: String,
    val username: String,
    val name: String?,
    val avatar: String?,
    val bio: String?
)