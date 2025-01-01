package com.project.lycommunity.data

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val department: String,
    val passwordHash: String
)
