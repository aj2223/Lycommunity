package com.project.lycommunity.ui.adminLogin.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.lycommunity.data.AdminUserRepository

class AdminSignUpViewModelFactory(
    private val repository: AdminUserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminSignUpViewModel::class.java)) {
            return AdminSignUpViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}