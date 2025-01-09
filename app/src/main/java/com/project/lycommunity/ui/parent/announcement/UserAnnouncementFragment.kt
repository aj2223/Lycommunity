package com.project.lycommunity.ui.parent.announcement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.lycommunity.data.AnnouncementRepository
import com.project.lycommunity.databinding.FragmentUserAnnouncementBinding
import com.project.lycommunity.ui.adapters.UserAnnouncementAdapter
import kotlinx.coroutines.launch


class UserAnnouncementFragment : Fragment() {

    private var _binding: FragmentUserAnnouncementBinding? = null
    private val binding get() = _binding!!

    private val viewModelOfAnnouncement: UserAnnouncementViewModel by viewModels {
        UserAnnouncementViewModelFactory(AnnouncementRepository())
    }
    private lateinit var adapter: UserAnnouncementAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserAnnouncementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
        observeAnnouncements()
    }

    private fun setupRecyclerView() {
        adapter = UserAnnouncementAdapter(
            onLikeClicked = { announcementId -> viewModelOfAnnouncement.likeAnnouncement(announcementId) },
            onDislikeClicked = { announcementId -> viewModelOfAnnouncement.dislikeAnnouncement(announcementId) }
        )
        binding.userAnnouncementRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.userAnnouncementRecyclerView.adapter = adapter

    }

    private fun observeAnnouncements() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModelOfAnnouncement.announcementsFlow.collect { announcements ->
                        adapter.submitList(announcements)
                    }
                }
                launch {
                    viewModelOfAnnouncement.uiState.collect { state ->
                        binding.progressBar.isVisible = state.isLoading

                        state.errorMessage?.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            viewModelOfAnnouncement.clearErrorMessage()
                        }
                    }
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}