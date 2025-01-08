package com.project.lycommunity.ui.parent.events

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
import com.project.lycommunity.R
import com.project.lycommunity.data.EventsRepository
import com.project.lycommunity.databinding.FragmentUserEventsBinding
import com.project.lycommunity.ui.adapters.UserEventsAdapter
import kotlinx.coroutines.launch

class UserEventsFragment : Fragment() {

    private var _binding: FragmentUserEventsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserEventsViewModel by viewModels {
        UserEventsViewModelFactory(EventsRepository())
    }
    private lateinit var adapter: UserEventsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

    }

    private fun setupRecyclerView() {
        adapter = UserEventsAdapter(
            onLikeClicked = { event ->
                viewModel.likeEvent(event.id)
            },
            onDislikeClicked = { event ->
                viewModel.dislikeEvent(event.id)
            }
        )
        binding.userEventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.userEventsRecyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.eventsFlow.collect { events ->
                        adapter.submitList(events)
                    }
                }
                launch {
                    viewModel.uiState.collect { state ->
                        binding.progressBar.isVisible = state.isLoading

                        state.errorMessage?.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            viewModel.clearErrorMessage()
                        }
                    }
                }
            }
        }
    }
}