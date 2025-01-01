package com.project.lycommunity.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.firestore.FirebaseFirestore
import com.project.lycommunity.data.UserRepository
import com.project.lycommunity.util.LoginValidationHelper
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUIState> = MutableStateFlow(LoginUIState())
    var loginStateFlow: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val loginResult = userRepository.loginUser(email, password)
                if (loginResult is ResultsWrapper.Success) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    }
                } else if (loginResult is ResultsWrapper.Error) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = loginResult.exception.message
                        )
                    }
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
    }

}