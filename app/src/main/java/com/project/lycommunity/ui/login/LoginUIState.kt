package com.project.lycommunity.ui.login

data class LoginUIState(
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
//    val isOtpSuccess: Boolean = false,
    val invalidInput: String? = null
)
