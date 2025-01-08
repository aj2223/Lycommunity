package com.project.lycommunity.ui.parent.announcement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.lycommunity.data.AnnouncementRepository

class UserAnnouncementViewModelFactory(
    private val repository: AnnouncementRepository = AnnouncementRepository()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserAnnouncementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserAnnouncementViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}