package com.project.lycommunity.ui.adminAnnouncement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.lycommunity.data.AnnouncementRepository

class AdminAnnouncementViewModelFactory(
    private val repository: AnnouncementRepository = AnnouncementRepository()
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminAnnouncementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminAnnouncementViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}