package com.project.lycommunity.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TestForumViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestForumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TestForumViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}