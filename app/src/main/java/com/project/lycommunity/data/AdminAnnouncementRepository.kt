package com.project.lycommunity.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminAnnouncementRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val announcementsCollection = firestore.collection("announcements")
    private val notificationsCollection = firestore.collection("notifications")

    suspend fun addAnnouncement(title: String, description: String, timestamp: Long) {
        val announcementData = mapOf(
            "title" to title,
            "description" to description,
            "timestamp" to timestamp
        )
        try {
            announcementsCollection.add(announcementData).await()

            // Add notification
            val notificationData = mapOf(
                "type" to "announcement",
                "title" to "New Announcement",
                "description" to "Admin posted: $title",
                "timestamp" to timestamp
            )
            notificationsCollection.add(notificationData).await()
        } catch (e: Exception) {
            throw Exception("Failed to add announcement and notification: ${e.message}")
        }
    }
}