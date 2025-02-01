package com.project.lycommunity.ui.notificationsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.lycommunity.data.NotificationsRepository

class NotificationsListViewModelFactory(
    private val repository: NotificationsRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}