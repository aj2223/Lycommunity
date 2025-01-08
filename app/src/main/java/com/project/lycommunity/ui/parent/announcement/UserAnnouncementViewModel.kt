package com.project.lycommunity.ui.parent.announcement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lycommunity.data.AnnouncementRepository
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserAnnouncementViewModel (
    private val repository: AnnouncementRepository
            )
    : ViewModel() {

    private val _uiState = MutableStateFlow(UserAnnouncementUIState())
    val uiState: StateFlow<UserAnnouncementUIState> = _uiState.asStateFlow()

    val announcementsFlow = repository.getAnnouncementsFlow()

    fun likeAnnouncement(announcementId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                repository.likeAnnouncement(announcementId)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to like announcement.") }
            }
        }
    }

    fun dislikeAnnouncement(announcementId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                repository.dislikeAnnouncement(announcementId)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to dislike announcement.") }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}