package com.project.lycommunity.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuth
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

//    private var userEmail: String? = null // Store the authenticated user's email

    private val _userEmail = MutableStateFlow<String?>(null) // Store the authenticated user's email
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()



//    fun login(email: String, password: String) {
//        _uiState.update { it.copy(isLoading = true) }
//
//        viewModelScope.launch {
//            val result = userRepository.loginUser(email, password)
//            when (result) {
//                is ResultsWrapper.Success -> {
//                    userEmail = email // Save email for later use
//                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
//                }
//                is ResultsWrapper.Error -> {
//                    _uiState.update {
//                        it.copy(isLoading = false, errorMessage = result.exception.message)
//                    }
//                }
//
//                else -> {}
//            }
//        }
//    }

    fun login(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch() {
            val result = userRepository.loginUser(email, password)
            when (result) {
                is ResultsWrapper.Success -> {
                    _userEmail.value = email // Save the logged-in user's email
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                is ResultsWrapper.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.exception.message)
                    }
                }

                else -> {}
            }
        }
    }

    fun getUserEmail(): String? {
        return _userEmail.value
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