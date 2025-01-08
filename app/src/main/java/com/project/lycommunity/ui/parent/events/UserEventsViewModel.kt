package com.project.lycommunity.ui.parent.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lycommunity.data.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserEventsViewModel(
    private val repository: EventsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserEventsUIState())
    val uiState: StateFlow<UserEventsUIState> = _uiState.asStateFlow()

    val eventsFlow = repository.getEventsFlow()

    fun likeEvent(eventId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                repository.likeEvents(eventId)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun dislikeEvent(eventId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                repository.dislikeEvents(eventId)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}