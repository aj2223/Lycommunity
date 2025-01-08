package com.project.lycommunity.ui.adminEvents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.lycommunity.data.EventsRepository

class AdminEventsViewModelFactory(
    private val repository: EventsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminEventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminEventsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}