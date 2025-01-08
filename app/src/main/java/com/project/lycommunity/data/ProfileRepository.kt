package com.project.lycommunity.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getUserDetails(email: String): User {
        return try {
            val querySnapshot = firestore.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.first().toObject(User::class.java)
                    ?: throw Exception("Invalid user data")
            } else {
                throw Exception("User not found")
            }
        } catch (e: Exception) {
            throw Exception("Failed to fetch user details: ${e.message}")
        }
    }


    suspend fun updateUserDetails(email: String, bio: String, likes: String, hobbies: String) {
        try {
            val querySnapshot = firestore.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val documentId = querySnapshot.documents.first().id
                firestore.collection("Users").document(documentId).update(
                    mapOf(
                        "bio" to bio,
                        "likes" to likes,
                        "hobbies" to hobbies
                    )
                ).await()
            } else {
                throw Exception("User not found")
            }
        } catch (e: Exception) {
            throw Exception("Failed to update user details: ${e.message}")
        }
    }
}