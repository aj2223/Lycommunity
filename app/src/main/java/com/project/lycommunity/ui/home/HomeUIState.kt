package com.project.lycommunity.ui.home

data class HomeUIState(
    val isLoading: Boolean = false,
    val fullName: String? = null,
    val department: String? = null,
    val errorMessage: String? = null
)
