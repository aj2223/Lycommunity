package com.project.lycommunity.ui.forum

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.lycommunity.R
import com.project.lycommunity.data.Comment
import com.project.lycommunity.databinding.FragmentForumBinding
import com.project.lycommunity.ui.adapters.AdminAnnouncementAdapter
import com.project.lycommunity.ui.adapters.AdminEventsAdapter
import com.project.lycommunity.ui.adapters.AnnouncementAdapter
import com.project.lycommunity.ui.adapters.CommentsAdapter
import com.project.lycommunity.ui.adapters.EventsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForumViewModel by viewModels { ForumViewModelFactory() }


    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var announcementsAdapter: AnnouncementAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForumBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()

    }

    private fun setupRecyclerViews() {
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.announcementsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        eventsAdapter = EventsAdapter(
            onCommentClicked = { eventId ->
                if (!eventsAdapter.getComments(eventId).isNullOrEmpty()) return@EventsAdapter
                viewModel.fetchComments(eventId, true)
            },
            onCommentSubmit = { eventId, commentText ->
                val comment = Comment(userId = "user123", text = commentText, timestamp = System.currentTimeMillis())
                viewModel.addComment(eventId, true, comment)
            },
            fetchComments = { eventId, callback ->
                lifecycleScope.launch {
                    viewModel.uiState.collectLatest { state ->
                        val newComments = state.comments
                        if (newComments != eventsAdapter.getComments(eventId)) {
                            callback(newComments)
                            eventsAdapter.setComments(eventId, newComments)
                        }
                    }
                }
            }
        )

        announcementsAdapter = AnnouncementAdapter(
            onCommentClicked = { announcementId ->
                if (!announcementsAdapter.getComments(announcementId).isNullOrEmpty()) return@AnnouncementAdapter
                viewModel.fetchComments(announcementId, false)
            },
            onCommentSubmit = { announcementId, commentText ->
                val comment = Comment(userId = "user123", text = commentText, timestamp = System.currentTimeMillis())
                viewModel.addComment(announcementId, false, comment)
            },
            fetchComments = { announcementId, callback ->
                lifecycleScope.launch {
                    viewModel.uiState.collectLatest { state ->
                        val newComments = state.comments
                        if (newComments != announcementsAdapter.getComments(announcementId)) {
                            callback(newComments)
                            announcementsAdapter.setComments(announcementId, newComments)
                        }
                    }
                }
            }
        )

        binding.eventsRecyclerView.adapter = eventsAdapter
        binding.announcementsRecyclerView.adapter = announcementsAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    binding.progressBar.visibility =
                        if (state.isLoading) View.VISIBLE else View.GONE

                    state.errorMessage?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }

                    eventsAdapter.submitList(state.events)
                    announcementsAdapter.submitList(state.announcements)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}