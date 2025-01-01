package com.project.lycommunity.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lycommunity.data.User
import com.project.lycommunity.data.UserRepository
import com.project.lycommunity.util.ResultsWrapper
import com.project.lycommunity.util.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<SignUpUIState> = MutableStateFlow(SignUpUIState())
    var signUpStateFlow: StateFlow<SignUpUIState> = _uiState.asStateFlow()

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        department: String,
        enteredId: String
    ) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userExists = repository.checkIfUserExists(email)
                if (userExists is ResultsWrapper.Success && userExists.data) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = "Email is already registered."
                        )
                    }
                    return@launch
                }


                val passwordExists = repository.checkIfPasswordExists(password)
                if (passwordExists is ResultsWrapper.Success && passwordExists.data) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = "This password has already been used. Please choose a different password."
                        )
                    }
                    return@launch
                }
                val validationResult = repository.validateUser(email, enteredId)
                if (validationResult is ResultsWrapper.Success && validationResult.data) {
                    val hashedPassword = SecurityUtils.hashPassword(password)
                    // Proceed with registration
                    val user = User(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        department = department,
                        passwordHash = hashedPassword
                    )

                    val registrationResult = repository.registerUser(user)
                    if (registrationResult is ResultsWrapper.Success) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true,
                                errorMessage = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = false,
                                errorMessage = (registrationResult as ResultsWrapper.Error).exception.message
                            )
                        }
                    }
                } else if (validationResult is ResultsWrapper.Error) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = validationResult.exception.message
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = e.message ?: "An unexpected error occurred."
                    )
                }
            }
        }
    }

    fun resetState() {
        _uiState.update {
            it.copy(
                isSuccess = false,
                errorMessage = null
            )
        }
        Log.d("SignUpViewModel", "State reset after handling success or error.")
    }
}