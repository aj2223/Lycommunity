package com.project.lycommunity.ui.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.project.lycommunity.R
import com.project.lycommunity.databinding.FragmentTestForumBinding
import com.project.lycommunity.ui.adapters.AnnouncementAdapter
import com.project.lycommunity.ui.adapters.EventsAdapter
import com.project.lycommunity.ui.adapters.TestAnnouncementAdapter
import com.project.lycommunity.ui.adapters.TestEventsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TestForumFragment : Fragment() {

    private var _binding: FragmentTestForumBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TestForumViewModel by viewModels { TestForumViewModelFactory() }

    private lateinit var testEventsAdapter: TestEventsAdapter
    private lateinit var testAnnouncementsAdapter: TestAnnouncementAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTestForumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()
        viewModel.fetchCurrentUser()

    }

    private fun setupRecyclerViews() {
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.announcementsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        testEventsAdapter = TestEventsAdapter(
            onCommentClicked = { eventId -> viewModel.fetchComments(eventId, true) },
            onCommentSubmit = { eventId, commentText ->
                viewModel.postComment(eventId, true, commentText)
            },
            fetchComments = { eventId, callback ->
                lifecycleScope.launch {
                    viewModel.uiState.collectLatest { state ->
                        callback(state.comments)
                    }
                }
            }
        )

        testAnnouncementsAdapter = TestAnnouncementAdapter(
            onCommentClicked = { announcementId -> viewModel.fetchComments(announcementId, false) },
            onCommentSubmit = { announcementId, commentText ->
                viewModel.postComment(announcementId, false, commentText)
            },
            fetchComments = { announcementId, callback ->
                lifecycleScope.launch {
                    viewModel.uiState.collectLatest { state ->
                        callback(state.comments) //
                    }
                }
            }
        )

        binding.eventsRecyclerView.adapter = testEventsAdapter
        binding.announcementsRecyclerView.adapter = testAnnouncementsAdapter
    }

    private fun observeViewModel1(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                if (firebaseUser == null) {
                    println("DEBUG: No authenticated user! Redirecting to login screen.")
                }

                println("DEBUG: Firebase User -> UID: ${firebaseUser!!.uid}, Email: ${firebaseUser.email}")

                viewModel.currentUser.collectLatest { user ->
                    if (user != null) {
                        val fullName = "${user.firstName ?: ""} ${user.lastName ?: ""}".trim()
                        binding.usernameTextView.text =
                            if (fullName.isNotEmpty()) fullName else "Guest"
                        println("DEBUG: Current User in Firestore -> $fullName")
                    } else {
                        println("DEBUG: No user data found in Firestore for UID -> ${firebaseUser.uid}")
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    binding.progressBar.visibility =
                        if (state.isLoading) View.VISIBLE else View.GONE

                    state.errorMessage?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        println("DEBUG: UI Error Message -> $it")
                    }

                    println("DEBUG: Events Loaded -> ${state.events.size}")
                    println("DEBUG: Announcements Loaded -> ${state.announcements.size}")
                    println("DEBUG: Comments Loaded -> ${state.comments.size}")

                    if (state.events.isNotEmpty()) {
                        testEventsAdapter.submitList(state.events)
                        binding.eventsRecyclerView.visibility = View.VISIBLE
                    } else {
                        binding.eventsRecyclerView.visibility = View.GONE
                    }

                    if (state.announcements.isNotEmpty()) {
                        testAnnouncementsAdapter.submitList(state.announcements)
                        binding.announcementsRecyclerView.visibility = View.VISIBLE
                    } else {
                        binding.announcementsRecyclerView.visibility = View.GONE
                    }

                    testEventsAdapter.notifyDataSetChanged()
                    testAnnouncementsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                    state.errorMessage?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        println("DEBUG: UI Error Message -> $it")
                    }

                    println("DEBUG: Events Loaded -> ${state.events.size}")
                    println("DEBUG: Announcements Loaded -> ${state.announcements.size}")
                    println("DEBUG: Comments Loaded -> ${state.comments.size}")

                    if (state.events.isNotEmpty()) {
                        testEventsAdapter.submitList(state.events)
                        binding.eventsRecyclerView.visibility = View.VISIBLE
                    } else {
                        binding.eventsRecyclerView.visibility = View.GONE
                    }

                    if (state.announcements.isNotEmpty()) {
                        testAnnouncementsAdapter.submitList(state.announcements)
                        binding.announcementsRecyclerView.visibility = View.VISIBLE
                    } else {
                        binding.announcementsRecyclerView.visibility = View.GONE
                    }

                    testEventsAdapter.notifyDataSetChanged()
                    testAnnouncementsAdapter.notifyDataSetChanged()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUser.collectLatest { user ->
                    val fullName = "${user?.firstName ?: ""} ${user?.lastName ?: ""}".trim()
                    binding.usernameTextView.text = if (fullName.isNotEmpty()) fullName else "Guest"
                    println("DEBUG: Current Logged-in User -> $fullName (UID: ${FirebaseAuth.getInstance().currentUser?.uid})")
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}