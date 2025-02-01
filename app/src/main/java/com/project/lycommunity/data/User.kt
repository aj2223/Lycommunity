package com.project.lycommunity.data

import com.google.firebase.Timestamp

data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val department: String? = null,
    val passwordHash: String? = null,
    val bio: String = "",
    val likes: String = "",
    val hobbies: String = "",
    val lastActive: Timestamp? = null // Nullable for backward compatibility
)
