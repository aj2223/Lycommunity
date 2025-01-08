package com.project.lycommunity.ui.adminLogin.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lycommunity.data.AdminUserRepository
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminLoginViewModel(
    private val repository: AdminUserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminLoginUIState())
    val uiState: StateFlow<AdminLoginUIState> get() = _uiState

    fun loginAdmin(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AdminLoginUIState(message = "Fields cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = AdminLoginUIState(isLoading = true)
            when (val result = repository.verifyAdminCredentials(email, password)) {
                is ResultsWrapper.Success -> {
                    if (result.data) {
                        _uiState.value = AdminLoginUIState(isSuccess = true, message = "Login successful")
                    } else {
                        _uiState.value = AdminLoginUIState(message = "Invalid credentials")
                    }
                }
                is ResultsWrapper.Error -> {
                    _uiState.value = AdminLoginUIState(message = result.exception.message ?: "Unknown error")
                }

                else -> {}
            }
        }
    }
}