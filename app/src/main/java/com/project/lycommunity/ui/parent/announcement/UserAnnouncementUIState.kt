package com.project.lycommunity.ui.parent.announcement

import com.project.lycommunity.data.Announcement

data class UserAnnouncementUIState(
    val isLoading: Boolean = false,
//    val announcements: List<Announcement> = emptyList(),
    val errorMessage: String? = null
)
