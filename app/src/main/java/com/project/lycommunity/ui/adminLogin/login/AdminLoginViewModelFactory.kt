package com.project.lycommunity.ui.adminLogin.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.lycommunity.data.AdminUserRepository

class AdminLoginViewModelFactory(
    private val repository: AdminUserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminLoginViewModel::class.java)) {
            return AdminLoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}