package com.project.lycommunity.ui.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.project.lycommunity.data.Announcement
import com.project.lycommunity.data.Comment
import com.project.lycommunity.data.Events
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForumViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(ForumUIState())
    val uiState: StateFlow<ForumUIState> = _uiState

    init {
        fetchForumData()
    }

    fun fetchForumData() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        firestore.collection("events")
            .get()
            .addOnSuccessListener { eventDocs ->
                val events = eventDocs.map { it.toObject(Events::class.java).copy(id = it.id) }

                firestore.collection("announcements")
                    .get()
                    .addOnSuccessListener { announcementDocs ->
                        val announcements = announcementDocs.map { it.toObject(Announcement::class.java).copy(id = it.id) }

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            events = events,
                            announcements = announcements
                        )
                    }
                    .addOnFailureListener {
                        _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = it.message)
                    }
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = it.message)
            }
    }

    fun fetchComments(postId: String, isEvent: Boolean) {
        val collectionPath = if (isEvent) "events" else "announcements"

        firestore.collection(collectionPath).document(postId).collection("comments")
            .get()
            .addOnSuccessListener { commentDocs ->
                val comments = commentDocs.map { it.toObject(Comment::class.java) }
                _uiState.value = _uiState.value.copy(comments = comments)
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(errorMessage = it.message)
            }
    }

    fun addComment(postId: String, isEvent: Boolean, comment: Comment) {
        val collectionPath = if (isEvent) "events" else "announcements"

        firestore.collection(collectionPath).document(postId).collection("comments")
            .add(comment)
            .addOnSuccessListener {
                fetchComments(postId, isEvent) // Refresh comments after adding
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(errorMessage = it.message)
            }
    }


}