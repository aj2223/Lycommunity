package com.project.lycommunity.data

data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val department: String? = null,
    val passwordHash: String? = null
)
