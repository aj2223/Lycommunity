package com.project.lycommunity.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NotificationsRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val notificationsCollection = firestore.collection("notifications")


    fun getNotificationsFlow(): Flow<List<Notification>> = callbackFlow {
        val listener = notificationsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("NotificationsRepository", "Error fetching notifications: ${error.message}")
                    close(error)
                    return@addSnapshotListener
                }

                val notifications = snapshot?.documents?.mapNotNull {
                    it.toObject(Notification::class.java)?.copy(id = it.id)
                } ?: emptyList()

                Log.d("NotificationsRepository", "Fetched notifications: $notifications")
                trySend(notifications).isSuccess
            }
        awaitClose { listener.remove() }
    }


}