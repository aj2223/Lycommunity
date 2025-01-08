package com.project.lycommunity.data

data class Notification(
    val id: String = "",
    val type: String = "", // e.g., "announcement" or "event"
    val title: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
