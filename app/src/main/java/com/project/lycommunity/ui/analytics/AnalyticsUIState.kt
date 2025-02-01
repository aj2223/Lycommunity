package com.project.lycommunity.ui.analytics

sealed class AnalyticsUIState {
    object Loading : AnalyticsUIState()
    data class Success(
        val totalUsers: Int,
        val activeUsers: Int,
        val dormantUsers: Int,
        val mostLikedAnnouncements: List<Pair<String, Int>> = emptyList(),
        val mostLikedEvents: List<Pair<String, Int>> = emptyList()
    ) : AnalyticsUIState()
    data class Error(val message: String) : AnalyticsUIState()
}
