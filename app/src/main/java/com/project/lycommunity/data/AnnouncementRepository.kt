package com.project.lycommunity.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AnnouncementRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val announcementsCollection = firestore.collection("announcements")
    //added
    private val notificationsCollection = firestore.collection("notifications")


    fun getAnnouncementsFlow1(): Flow<List<Announcement>> = callbackFlow {
        val listener = announcementsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val announcements = snapshot?.documents?.mapNotNull {
                    it.toObject(Announcement::class.java)?.copy(id = it.id)
                } ?: emptyList()

                trySend(announcements).isSuccess
            }
        awaitClose { listener.remove() }
    }

    suspend fun addAnnouncement(title: String, description: String, timestamp: Long) {
        val announcementData = mapOf(
            "title" to title,
            "description" to description,
            "timestamp" to timestamp
        )
        try {
            // Add announcement to Firestore
            announcementsCollection.add(announcementData).await()

            // Add notification for the announcement
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

//    suspend fun addAnnouncement(title: String, description: String, timestamp: Long) {
//        val announcementData = mapOf(
//            "title" to title,
//            "description" to description,
//            "timestamp" to timestamp,
//            "likes" to 0,
//            "dislikes" to 0
//        )
//        announcementsCollection.add(announcementData).await()
//    }

    suspend fun updateAnnouncement1(announcementId: String, title: String, description: String) {
        announcementsCollection.document(announcementId).update(
            "title", title,
            "description", description
        ).await()
    }

    suspend fun deleteAnnouncement1(announcementId: String) {
        announcementsCollection.document(announcementId).delete().await()
    }

    suspend fun likeAnnouncement1(announcementId: String) {
        val docRef = announcementsCollection.document(announcementId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val currentLikes = snapshot.getLong("likes") ?: 0
            transaction.update(docRef, "likes", currentLikes + 1)
        }.await()
    }

    suspend fun dislikeAnnouncement1(announcementId: String) {
        val docRef = announcementsCollection.document(announcementId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val currentDislikes = snapshot.getLong("dislikes") ?: 0
            transaction.update(docRef, "dislikes", currentDislikes + 1)
        }.await()
    }

    fun getAnnouncementsFlow(): Flow<List<Announcement>> = callbackFlow {
        val listener = announcementsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val announcements = snapshot?.documents?.mapNotNull {
                    it.toObject(Announcement::class.java)?.copy(id = it.id)
                } ?: emptyList()

                trySend(announcements).isSuccess
            }
        awaitClose { listener.remove() }
    }

    suspend fun addAnnouncement1(title: String, description: String, timestamp: Long) {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: throw Exception("User not authenticated")
        val adminSnapshot = firestore.collection("admins").document(currentUserEmail).get().await()

        val department = adminSnapshot.getString("department") ?: "Unknown Department"

        val announcementData = mapOf(
            "title" to title,
            "description" to description,
            "timestamp" to timestamp,
            "department" to department,
            "likes" to 0,
            "dislikes" to 0
        )
        try {
            // Add announcement to Firestore
            announcementsCollection.add(announcementData).await()

            // Add notification for the announcement
            val notificationData = mapOf(
                "type" to "announcement",
                "title" to "New Announcement",
                "description" to "Admin from $department posted: $title",
                "timestamp" to timestamp
            )
            notificationsCollection.add(notificationData).await()
        } catch (e: Exception) {
            throw Exception("Failed to add announcement and notification: ${e.message}")
        }
    }

    suspend fun updateAnnouncement(announcementId: String, title: String, description: String) {
        announcementsCollection.document(announcementId).update(
            "title", title,
            "description", description
        ).await()
    }

    suspend fun deleteAnnouncement(announcementId: String) {
        announcementsCollection.document(announcementId).delete().await()
    }

    suspend fun likeAnnouncement(announcementId: String) {
        val docRef = announcementsCollection.document(announcementId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val currentLikes = snapshot.getLong("likes") ?: 0
            transaction.update(docRef, "likes", currentLikes + 1)
        }.await()
    }

    suspend fun dislikeAnnouncement(announcementId: String) {
        val docRef = announcementsCollection.document(announcementId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val currentDislikes = snapshot.getLong("dislikes") ?: 0
            transaction.update(docRef, "dislikes", currentDislikes + 1)
        }.await()
    }

}