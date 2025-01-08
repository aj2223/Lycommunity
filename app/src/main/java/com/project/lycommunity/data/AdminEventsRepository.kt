package com.project.lycommunity.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminEventsRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val eventsCollection = firestore.collection("events")
    private val notificationsCollection = firestore.collection("notifications")

    suspend fun addEvent(title: String, description: String, timestamp: Long) {
        val eventData = mapOf(
            "title" to title,
            "description" to description,
            "timestamp" to timestamp
        )
        try {
            // Add event to Firestore
            eventsCollection.add(eventData).await()

            // Add a corresponding notification
            val notificationData = mapOf(
                "type" to "event",
                "title" to "New Event",
                "description" to "Admin has posted a new event: $title",
                "timestamp" to timestamp
            )
            notificationsCollection.add(notificationData).await()
        } catch (e: Exception) {
            throw Exception("Failed to add event and notification: ${e.message}")
        }
    }
}