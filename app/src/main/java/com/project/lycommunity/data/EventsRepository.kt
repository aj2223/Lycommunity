package com.project.lycommunity.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class EventsRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val eventsCollection = firestore.collection("events")
    fun getEventsFlow(): Flow<List<Events>> = callbackFlow {
        val listener = eventsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val events = snapshot?.documents?.mapNotNull {
                    it.toObject(Events::class.java)?.copy(id = it.id)
                } ?: emptyList()

                trySend(events).isSuccess
            }
        awaitClose { listener.remove() }
    }

    suspend fun addEvents(title: String, description: String, timestamp: Long) {
        val eventsData = mapOf(
            "title" to title,
            "description" to description,
            "timestamp" to timestamp,
            "likes" to 0,
            "dislikes" to 0
        )
        try {
            eventsCollection.add(eventsData).await()
        } catch (e: Exception) {
            throw Exception("Failed to add event: ${e.message}")
        }
    }

    suspend fun updateEvents(eventsId: String, title: String, description: String) {
        try {
            eventsCollection.document(eventsId).update(
                "title", title,
                "description", description
            ).await()
        } catch (e: Exception) {
            throw Exception("Failed to update event: ${e.message}")
        }
    }

    suspend fun deleteEvents(eventsId: String) {
        try {
            eventsCollection.document(eventsId).delete().await()
        } catch (e: Exception) {
            throw Exception("Failed to delete event: ${e.message}")
        }
    }

    suspend fun likeEvents(eventsId: String) {
        val docRef = eventsCollection.document(eventsId)
        try {
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(docRef)
                val currentLikes = snapshot.getLong("likes") ?: 0
                transaction.update(docRef, "likes", currentLikes + 1)
            }.await()
        } catch (e: Exception) {
            throw Exception("Failed to like event: ${e.message}")
        }
    }

    suspend fun dislikeEvents(eventsId: String) {
        val docRef = eventsCollection.document(eventsId)
        try {
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(docRef)
                val currentDislikes = snapshot.getLong("dislikes") ?: 0
                transaction.update(docRef, "dislikes", currentDislikes + 1)
            }.await()
        } catch (e: Exception) {
            throw Exception("Failed to dislike event: ${e.message}")
        }
    }
}