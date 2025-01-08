package com.project.lycommunity.ui.profile

import com.project.lycommunity.data.User

data class ProfileUIState (
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null
)