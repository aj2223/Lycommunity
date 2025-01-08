package com.project.lycommunity.ui.adminAnnouncement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lycommunity.data.AnnouncementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminAnnouncementViewModel(
    private val repository: AnnouncementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminAnnouncementUIState())
    val uiState: StateFlow<AdminAnnouncementUIState> = _uiState.asStateFlow()

    val announcementsFlow = repository.getAnnouncementsFlow()

    fun addAnnouncement(title: String, description: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addAnnouncement(title, description, System.currentTimeMillis())
                _uiState.update { it.copy(isLoading = false, successMessage = "Announcement added successfully.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to add announcement.") }
            }
        }
    }

    fun updateAnnouncement(announcementId: String, title: String, description: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                repository.updateAnnouncement(announcementId, title, description)
                _uiState.update { it.copy(isLoading = false, successMessage = "Announcement updated successfully.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to update announcement.") }
            }
        }
    }

    fun deleteAnnouncement(announcementId: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                repository.deleteAnnouncement(announcementId)
                _uiState.update { it.copy(isLoading = false, successMessage = "Announcement deleted successfully.") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to delete announcement.") }
            }
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

}