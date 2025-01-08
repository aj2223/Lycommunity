package com.project.lycommunity.ui.adminEvents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lycommunity.data.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminEventsViewModel (
    private val repository: EventsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminEventsUIState())
    val uiState: StateFlow<AdminEventsUIState> = _uiState.asStateFlow()

    val eventsFlow = repository.getEventsFlow()

    fun addEvent(title: String, description: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                repository.addEvents(title, description, System.currentTimeMillis())
                _uiState.update { it.copy(isLoading = false, successMessage = "Event added successfully!") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun updateEvent(eventId: String, title: String, description: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                repository.updateEvents(eventId, title, description)
                _uiState.update { it.copy(isLoading = false, successMessage = "Event updated successfully!") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun deleteEvent(eventId: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                repository.deleteEvents(eventId)
                _uiState.update { it.copy(isLoading = false, successMessage = "Event deleted successfully!") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
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