package com.project.lycommunity.data

import com.google.firebase.firestore.PropertyName

data class TestComment(
    @PropertyName("userId") val userId: String = "",
    @PropertyName("firstName") val firstName: String = "",
    @PropertyName("lastName") val lastName: String = "",
    @PropertyName("text") val text: String = "",
    @PropertyName("timestamp") val timestamp: Long = System.currentTimeMillis()
)
