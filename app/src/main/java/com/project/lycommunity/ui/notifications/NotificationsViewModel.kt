package com.project.lycommunity.ui.notifications

import androidx.lifecycle.ViewModel
import com.project.lycommunity.data.NotificationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationsViewModel(
    private val repository: NotificationsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUIState())
    val uiState: StateFlow<NotificationsUIState> = _uiState.asStateFlow()

    // Flow to observe notifications in real-time
    val notificationsFlow = repository.getNotificationsFlow()

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}