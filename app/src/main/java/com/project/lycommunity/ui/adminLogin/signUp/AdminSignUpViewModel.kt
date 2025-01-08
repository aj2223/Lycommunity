package com.project.lycommunity.ui.adminLogin.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lycommunity.data.AdminUserRepository
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminSignUpViewModel (
    private val repository: AdminUserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AdminSignUpUIState())
    val uiState: StateFlow<AdminSignUpUIState> get() = _uiState

    fun registerAdmin(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = AdminSignUpUIState(message = "Fields cannot be empty")
            return
        }

        if (password != confirmPassword) {
            _uiState.value = AdminSignUpUIState(message = "Passwords do not match")
            return
        }

        viewModelScope.launch {
            _uiState.value = AdminSignUpUIState(isLoading = true)
            when (val result = repository.registerAdmin(email, password)) {
                is ResultsWrapper.Success -> {
                    _uiState.value = AdminSignUpUIState(isSuccess = true, message = "Registration successful")
                }
                is ResultsWrapper.Error -> {
                    _uiState.value = AdminSignUpUIState(message = result.exception.message ?: "Unknown error")
                }

                else -> {}
            }
        }
    }
}