package com.project.lycommunity.ui.forum

import com.project.lycommunity.data.Announcement
import com.project.lycommunity.data.Comment
import com.project.lycommunity.data.Events

data class ForumUIState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val events: List<Events> = emptyList(),
    val announcements: List<Announcement> = emptyList(),
    val comments: List<Comment> = emptyList()
)
