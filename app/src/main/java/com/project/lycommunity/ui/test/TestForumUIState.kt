package com.project.lycommunity.ui.test

import com.project.lycommunity.data.Announcement
import com.project.lycommunity.data.Comment
import com.project.lycommunity.data.Events
import com.project.lycommunity.data.TestComment

data class TestForumUIState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val events: List<Events> = emptyList(),
    val announcements: List<Announcement> = emptyList(),
    val comments: List<TestComment> = emptyList()
)
