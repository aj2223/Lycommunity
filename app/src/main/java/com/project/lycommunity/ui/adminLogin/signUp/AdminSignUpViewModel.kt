package com.project.lycommunity.ui.adminLogin.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lycommunity.data.AdminUserRepository
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminSignUpViewModel (
    private val repository: AdminUserRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AdminSignUpUIState())
    val uiState: StateFlow<AdminSignUpUIState> get() = _uiState

    fun registerAdmin1(email: String, password: String, confirmPassword: String, department: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = AdminSignUpUIState(message = "Fields cannot be empty")
            return
        }

        if (password != confirmPassword) {
            _uiState.value = AdminSignUpUIState(message = "Passwords do not match")
            return
        }

//        viewModelScope.launch {
//            _uiState.value = AdminSignUpUIState(isLoading = true)
//            when (val result = repository.registerAdmin(email, password)) {
//                is ResultsWrapper.Success -> {
//                    _uiState.value = AdminSignUpUIState(isSuccess = true, message = "Registration successful")
//                }
//                is ResultsWrapper.Error -> {
//                    _uiState.value = AdminSignUpUIState(message = result.exception.message ?: "Unknown error")
//                }
//
//                else -> {}
//            }
//        }

        viewModelScope.launch {
            _uiState.value = AdminSignUpUIState(isLoading = true)
            when (val result = repository.registerAdmin(email, password, department)) {
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


    fun registerAdmin(email: String, password: String, department: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = repository.registerAdmin(email, password, department)) {
                is ResultsWrapper.Success -> {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, message = "Registration successful") }
                }
                is ResultsWrapper.Error -> {
                    _uiState.update { it.copy(isLoading = false, message = result.exception.message ?: "Unknown error") }
                }

                else -> {}
            }
        }
    }
}