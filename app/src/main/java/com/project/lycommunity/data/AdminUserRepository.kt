package com.project.lycommunity.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.project.lycommunity.util.ResultsWrapper
import com.project.lycommunity.util.SecurityUtils
import com.project.lycommunity.util.SecurityUtils.hashPassword
import kotlinx.coroutines.tasks.await

class AdminUserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val adminsCollection = firestore.collection("admins")

    suspend fun verifyAdminCredentials(email: String, password: String): ResultsWrapper<Boolean> {
        return try {
            Log.d("AdminLogin", "Querying Firestore for admin email: $email")
            val query = adminsCollection.whereEqualTo("email", email).get().await()

            if (query.isEmpty) {
                Log.e("AdminLogin", "Admin account not found for email: $email")
                return ResultsWrapper.Error(Exception("Admin account not found"))
            }

            val document = query.documents.first()
            val storedHash = document.getString("passwordHash")
            Log.d("AdminLogin", "Retrieved admin document: $document")

            if (storedHash != null && SecurityUtils.verifyPassword(password, storedHash)) {
                Log.d("AdminLogin", "Admin credentials verified successfully")
                ResultsWrapper.Success(true)
            } else {
                Log.e("AdminLogin", "Invalid credentials for email: $email")
                ResultsWrapper.Error(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Log.e("AdminLogin", "Error verifying admin credentials: ${e.message}")
            ResultsWrapper.Error(e)
        }
    }

    suspend fun registerAdmin1(email: String, password: String): ResultsWrapper<Void?> {
        return try {
            // Check if email already exists
            val query = adminsCollection.whereEqualTo("email", email).get().await()
            if (!query.isEmpty) {
                return ResultsWrapper.Error(Exception("Admin account already exists"))
            }

            // Hash the password before storing
            val passwordHash = SecurityUtils.hashPassword(password)

            val adminData = mapOf(
                "email" to email,
                "passwordHash" to passwordHash
            )

            adminsCollection.document().set(adminData).await()
            ResultsWrapper.Success(null)
        } catch (e: Exception) {
            ResultsWrapper.Error(e)
        }
    }

    suspend fun registerAdmin(email: String, password: String, department: String): ResultsWrapper<Boolean> {
        return try {
            val hashedPassword = hashPassword(password)
            val adminData = mapOf(
                "email" to email,
                "passwordHash" to hashedPassword,
                "department" to department
            )

            firestore.collection("admins")
                .document(email)
                .set(adminData)
                .await()

            ResultsWrapper.Success(true)
        } catch (e: Exception) {
            ResultsWrapper.Error(e)
        }
    }

    suspend fun getAllAdmins(): ResultsWrapper<List<AdminUser>> {
        return try {
            val query = adminsCollection.get().await()
            val adminList = query.documents.mapNotNull { document ->
                document.toObject(AdminUser::class.java)?.copy(id = document.id)
            }
            ResultsWrapper.Success(adminList)
        } catch (e: Exception) {
            ResultsWrapper.Error(e)
        }
    }
}