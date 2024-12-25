package com.project.lycommunity.ui.splash

data class SplashUIState(
    val isLoading: Boolean = true,
    val finishedPlaying: Boolean = false,
    val errorMessage: String? = null,
)