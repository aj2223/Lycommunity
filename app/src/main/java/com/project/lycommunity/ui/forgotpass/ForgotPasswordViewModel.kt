package com.project.lycommunity.ui.forgotpass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lycommunity.data.UserRepository
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUIState())
    val uiState: StateFlow<ForgotPasswordUIState> = _uiState

    fun resetPassword(email: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val result = repository.resetPassword(email)) {
                is ResultsWrapper.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "A password reset link has been sent to your email."
                        )
                    }
                }
                is ResultsWrapper.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message ?: "Failed to reset password."
                        )
                    }
                }

                else -> {}
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(successMessage = null, errorMessage = null) }
    }
}