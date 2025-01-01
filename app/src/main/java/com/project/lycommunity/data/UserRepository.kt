package com.project.lycommunity.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.project.lycommunity.util.ResultsWrapper
import com.project.lycommunity.util.SecurityUtils
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    private val studentsCollection = firestore.collection("students")
    suspend fun registerUser(user: User): ResultsWrapper<Void?> {
        return try {
            val userData = mapOf(
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "email" to user.email,
                "department" to user.department,
                "passwordHash" to user.passwordHash
            )
            usersCollection.document(user.email).set(userData).await()
            ResultsWrapper.Success(null) // Registration successful
        } catch (e: Exception) {
            ResultsWrapper.Error(e)
        }
    }

    suspend fun validateUser(email: String, enteredId: String): ResultsWrapper<String> {
        return try {
            // Query for the document with the provided ID
            val querySnapshot = studentsCollection.whereEqualTo("IDNumber", enteredId).get().await()

            if (!querySnapshot.isEmpty) {
                // ID exists; now validate the email
                val document = querySnapshot.documents.first()
                val validEmail = document.getString("email")

                if (validEmail == email) {
                    ResultsWrapper.Success("Valid") // Both email and ID match
                } else {
                    ResultsWrapper.Error(Exception("Invalid email for the provided ID.")) // Email mismatch
                }
            } else {
                ResultsWrapper.Error(Exception("ID does not belong to an Enrolled Student")) // ID does not exist
            }
        } catch (e: Exception) {
            ResultsWrapper.Error(e) // Handle Firestore or network errors
        }
    }

//    suspend fun validateUser(email: String, enteredId: String): ResultsWrapper<Boolean> {
//        return try {
//            // Query Firestore for a document where the email field matches
//            val querySnapshot = studentsCollection
//                .whereEqualTo("email", email)
//                .get()
//                .await()
//
//            if (!querySnapshot.isEmpty) {
//                // Document exists; retrieve the IDNumber field
//                val document = querySnapshot.documents.first()
//                val validId = document.getString("IDNumber")
//                Log.d("ValidateUser", "Document found. Retrieved IDNumber: $validId for email: $email")
//
//                return if (validId == enteredId) {
//                    Log.d("ValidateUser", "ID matches for email: $email")
//                    ResultsWrapper.Success(true) // Validation successful
//                } else {
//                    Log.e("ValidateUser", "Entered ID ($enteredId) does not match valid ID ($validId) for email: $email")
//                    ResultsWrapper.Success(false) // ID mismatch
//                }
//            } else {
//                Log.e("ValidateUser", "No document found for email: $email")
//                ResultsWrapper.Success(false) // Email not found
//            }
//        } catch (e: Exception) {
//            Log.e("ValidateUser", "Error validating user: ${e.message}")
//            ResultsWrapper.Error(e) // Handle errors
//        }
//    }
    suspend fun loginUser(email: String, password: String): ResultsWrapper<User> {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("email", email)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                return ResultsWrapper.Error(Exception("Invalid email or password."))
            }

            val document = querySnapshot.documents.first()
            val user = document.toObject(User::class.java)

            if (user != null) {
                val isPasswordValid = SecurityUtils.verifyPassword(password, user.passwordHash)
                if (isPasswordValid) {
                    ResultsWrapper.Success(user)
                } else {
                    ResultsWrapper.Error(Exception("Invalid email or password."))
                }
            } else {
                ResultsWrapper.Error(Exception("User data is corrupted."))
            }
        } catch (e: Exception) {
            ResultsWrapper.Error(e)
        }
    }

    suspend fun checkIfUserExists(email: String): ResultsWrapper<Boolean> {
        return try {
            val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
            if (!querySnapshot.isEmpty) {
                ResultsWrapper.Success(true) // User already exists
            } else {
                ResultsWrapper.Success(false) // User does not exist
            }
        } catch (e: Exception) {
            ResultsWrapper.Error(e) // Handle errors
        }
    }


}