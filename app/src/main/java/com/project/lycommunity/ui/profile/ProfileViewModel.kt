package com.project.lycommunity.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lycommunity.data.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState: StateFlow<ProfileUIState> = _uiState.asStateFlow()

    fun loadUserDetails(email: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val user = repository.getUserDetails(email)
                _uiState.update { it.copy(isLoading = false, user = user) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }


    fun addUserDetails(email: String, bio: String, likes: String, hobbies: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                repository.updateUserDetails(email, bio, likes, hobbies)
                val updatedUser = _uiState.value.user?.copy(bio = bio, likes = likes, hobbies = hobbies)
                _uiState.update { it.copy(isLoading = false, user = updatedUser) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }


    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}