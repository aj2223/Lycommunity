package com.project.lycommunity.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState

    fun fetchUserFullName(userEmail: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            firestore.collection("Users")
                .document(userEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val firstName = document.getString("firstName") ?: ""
                        val lastName = document.getString("lastName") ?: ""
                        val fullName = "$firstName $lastName".trim()
                        val department = document.getString("department") ?: "Unknown"
                        _uiState.update { it.copy(isLoading = false, fullName = fullName, department = department) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "User not found.") }
                    }
                }
                .addOnFailureListener {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to fetch user details.") }
                }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

}