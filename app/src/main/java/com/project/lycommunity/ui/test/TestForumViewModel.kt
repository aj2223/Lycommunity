package com.project.lycommunity.ui.test

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.lycommunity.data.Announcement
import com.project.lycommunity.data.Comment
import com.project.lycommunity.data.Events
import com.project.lycommunity.data.TestComment
import com.project.lycommunity.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestForumViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(TestForumUIState())
    val uiState: StateFlow<TestForumUIState> = _uiState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        fetchForumData()
        fetchCurrentUser()
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

                        println("DEBUG: Events Loaded - ${events.size}")
                        println("DEBUG: Announcements Loaded - ${announcements.size}")

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
    fun fetchCurrentUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            println("DEBUG: No authenticated user found in Firebase!")
            return
        }

        val userId = firebaseUser.uid
        println("DEBUG: Fetching user data for UID -> $userId")

        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    _currentUser.value = user
                    println("DEBUG: User data loaded -> $user")
                } else {
                    println("DEBUG: User document not found in Firestore for UID -> $userId")
                }
            }
            .addOnFailureListener { e ->
                println("DEBUG: Failed to fetch user data -> ${e.message}")
            }
    }





    fun fetchComments(postId: String, isEvent: Boolean) {
        val collectionPath = if (isEvent) "events" else "announcements"

        println("DEBUG: Fetching comments for Post ID: $postId (${if (isEvent) "Event" else "Announcement"})")

        firestore.collection(collectionPath).document(postId).collection("comments")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { commentDocs ->
                val comments = commentDocs.map { it.toObject(TestComment::class.java) }
                println("DEBUG: Comments Fetched -> Count: ${comments.size}")
                _uiState.value = _uiState.value.copy(comments = comments)
            }
            .addOnFailureListener { e ->
                println("DEBUG: Failed to fetch comments -> ${e.message}")
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
    }



    fun postComment(postId: String, isEvent: Boolean, commentText: String) {
        val user = _currentUser.value
        if (user == null) {
            println("DEBUG: User data not available! Fetching user data again.")
            fetchCurrentUser()
            return
        }

        val firstName = user.firstName ?: "Unknown"
        val lastName = user.lastName ?: ""

        val comment = TestComment(
            userId = auth.currentUser?.uid ?: "unknown",
            firstName = firstName,
            lastName = lastName,
            text = commentText,
            timestamp = System.currentTimeMillis()
        )

        val collectionPath = if (isEvent) "events" else "announcements"

        println("DEBUG: Posting comment to Firestore -> Post ID: $postId, Type: ${if (isEvent) "Event" else "Announcement"}")
        println("DEBUG: Comment Data -> $comment")

        firestore.collection(collectionPath).document(postId).collection("comments")
            .add(comment)
            .addOnSuccessListener {
                println("DEBUG: Comment successfully added to Firestore!")
                fetchComments(postId, isEvent)
            }
            .addOnFailureListener { e ->
                println("DEBUG: Failed to post comment -> ${e.message}")
            }
    }



}