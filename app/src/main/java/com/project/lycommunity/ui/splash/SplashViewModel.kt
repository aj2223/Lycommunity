package com.project.lycommunity.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel : ViewModel() {

    private var _uiState: MutableStateFlow<SplashUIState> = MutableStateFlow(SplashUIState())
    val splashStateFlow : StateFlow<SplashUIState> = _uiState.asStateFlow()


    fun showSplash(){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, finishedPlaying = false)

            delay(4000)

            _uiState.value = _uiState.value.copy(isLoading = false, finishedPlaying = true)
        }
//        Handler(Looper.getMainLooper()).postDelayed({
//
//        }, 4000) // Adjust time as needed
    }

    fun showSplash2(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//            _uiState.update {
//                    it.copy(
//                        isLoading = true,
//                        finishedPlaying = false
//                    )
//            }
                delay(4000)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        finishedPlaying = true
                    )
                }
            }
        }
//        Handler(Looper.getMainLooper()).postDelayed({
//
//        }, 4000) // Adjust time as needed
    }




}