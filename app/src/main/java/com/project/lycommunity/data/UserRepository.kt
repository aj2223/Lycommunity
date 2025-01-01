package com.project.lycommunity.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.project.lycommunity.util.ResultsWrapper
import com.project.lycommunity.util.SecurityUtils
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("Users")
    private val studentsCollection = firestore.collection("Students")
    suspend fun registerUser(user: User): ResultsWrapper<Void?> {
        return try {
            val email = user.email ?: return ResultsWrapper.Error(Exception("Email cannot be null."))
            val userData = mapOf(
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "email" to user.email,
                "department" to user.department,
                "passwordHash" to user.passwordHash
            )
            usersCollection.document(email).set(userData).await()
            ResultsWrapper.Success(null) // Registration successful
        } catch (e: Exception) {
            ResultsWrapper.Error(e)
        }
    }

    suspend fun validateUser(email: String, enteredId: String): ResultsWrapper<Boolean> {
        return try {
            val querySnapshot = studentsCollection.whereEqualTo("IDNumber", enteredId).get().await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val validEmail = document.getString("email")

                if (validEmail == email) {
                    ResultsWrapper.Success(true) // Email and ID match
                } else {
                    ResultsWrapper.Error(Exception("Invalid email for the provided ID.")) // Email mismatch
                }
            } else {
                ResultsWrapper.Error(Exception("ID does not belong to an enrolled Student.")) // ID does not exist
            }
        } catch (e: Exception) {
            ResultsWrapper.Error(e)
        }
    }


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
                val isPasswordValid = SecurityUtils.verifyPassword(password, user.passwordHash ?: "")
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

    suspend fun checkIfPasswordExists(password: String): ResultsWrapper<Boolean> {
        return try {
            val querySnapshot = usersCollection.get().await()

            for (document in querySnapshot.documents) {
                val hashedPassword = document.getString("passwordHash")
                if (hashedPassword != null && SecurityUtils.verifyPassword(password, hashedPassword)) {
                    return ResultsWrapper.Success(true) // Password exists
                }
            }

            ResultsWrapper.Success(false) // Password does not exist
        } catch (e: Exception) {
            ResultsWrapper.Error(e)
        }
    }


}