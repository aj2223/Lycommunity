package com.project.lycommunity.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.project.lycommunity.data.UserRepository
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AnalyticsViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    // Backing property for UI state flow.
    private val _uiState = MutableStateFlow<AnalyticsUIState>(AnalyticsUIState.Loading)
    val uiState: StateFlow<AnalyticsUIState> = _uiState

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            _uiState.value = AnalyticsUIState.Loading
            try {
                // Fetch user analytics
                val userResult = userRepository.fetchUserAnalytics()
                if (userResult.isFailure) {
                    throw userResult.exceptionOrNull()!!
                }

                val (totalUsers, activeUsers, dormantUsers) = userResult.getOrNull()!!

                // Fetch most liked data
                val mostLikedAnnouncements = userRepository.getMostLikedAnnouncements()
                val mostLikedEvents = userRepository.getMostLikedEvents()

                _uiState.value = AnalyticsUIState.Success(
                    totalUsers = totalUsers,
                    activeUsers = activeUsers,
                    dormantUsers = dormantUsers,
                    mostLikedAnnouncements = mostLikedAnnouncements,
                    mostLikedEvents = mostLikedEvents
                )
            } catch (e: Exception) {
                _uiState.value = AnalyticsUIState.Error("Failed to load analytics: ${e.message}")
            }
        }
    }

    fun fetchMostLikedData() {
        viewModelScope.launch {
            try {
                val announcements = userRepository.getMostLikedAnnouncements()
                val events = userRepository.getMostLikedEvents()

                _uiState.value = AnalyticsUIState.Success(
                    totalUsers = 100, // Replace with actual value
                    activeUsers = 75, // Replace with actual value
                    dormantUsers = 25, // Replace with actual value
                    mostLikedAnnouncements = announcements,
                    mostLikedEvents = events
                )
            } catch (e: Exception) {
                _uiState.value = AnalyticsUIState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

}