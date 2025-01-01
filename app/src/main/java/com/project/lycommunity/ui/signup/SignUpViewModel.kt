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
        Log.d("SignUpViewModel", "Registration started for email: $email")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userExists = repository.checkIfUserExists(email)
                if (userExists is ResultsWrapper.Success && userExists.data) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = "Registration Fail. Email is already used."
                        )
                    }
                    return@launch
                }

                val validationResult = repository.validateUser(email, enteredId)
                if (validationResult is ResultsWrapper.Success) {
                    // Proceed with registration in the users collection
                    val hashedPassword = SecurityUtils.hashPassword(password)
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
                    // Display the error from validateUser (email or ID issue)
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

    // Reset state to prevent duplicate toasts or UI updates
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