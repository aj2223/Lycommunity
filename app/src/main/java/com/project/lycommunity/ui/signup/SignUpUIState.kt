package com.project.lycommunity.ui.signup

data class SignUpUIState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val userId: String? = null
)