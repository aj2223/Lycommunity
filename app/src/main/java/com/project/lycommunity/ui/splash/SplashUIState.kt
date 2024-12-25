package com.project.lycommunity.ui.splash

data class SplashUIState(
    val isLoading: Boolean = true,
    val finishedPlaying: Boolean = true,
    val errorMessage: String? = null,
)