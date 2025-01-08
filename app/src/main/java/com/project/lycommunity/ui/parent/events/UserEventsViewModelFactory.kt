package com.project.lycommunity.ui.parent.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.lycommunity.data.EventsRepository

class UserEventsViewModelFactory(
    private val repository: EventsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserEventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserEventsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}