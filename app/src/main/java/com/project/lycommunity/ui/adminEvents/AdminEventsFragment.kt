package com.project.lycommunity.ui.adminEvents

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
import com.project.lycommunity.data.Events
import com.project.lycommunity.data.EventsRepository
import com.project.lycommunity.databinding.FragmentAdminEventsBinding
import com.project.lycommunity.ui.adapters.AdminEventsAdapter
import com.project.lycommunity.util.AddEditEventDialogFragment
import kotlinx.coroutines.launch


class AdminEventsFragment : Fragment() {

    private var _binding : FragmentAdminEventsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminEventsViewModel by viewModels {
        AdminEventsViewModelFactory(EventsRepository())
    }

    private lateinit var adapter: AdminEventsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminEventsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }
    private fun setupRecyclerView() {
        adapter = AdminEventsAdapter(
            onEditClicked = { event -> showEditEventDialog(event) },
            onDeleteClicked = { event -> viewModel.deleteEvent(event.id) }
        )
        binding.adminEventsRecyclerView.layoutManager = LinearLayoutManager(requireContext()) // Set LayoutManager
        binding.adminEventsRecyclerView.adapter = adapter
    }

    private fun setupListeners() {
        binding.addEventFab.setOnClickListener {
            showAddEventDialog()
        }
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

                        state.successMessage?.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            viewModel.clearSuccessMessage()
                        }

                        state.errorMessage?.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            viewModel.clearErrorMessage()
                        }
                    }
                }
            }
        }
    }


    private fun showAddEventDialog() {
        val dialog = AddEditEventDialogFragment.newInstance(null)
        dialog.setOnEventsActionListener { title, description ->
            viewModel.addEvent(title, description)
        }
        dialog.show(parentFragmentManager, "AddEventDialog")
    }

    private fun showEditEventDialog(event: Events) {
        val dialog = AddEditEventDialogFragment.newInstance(event)
        dialog.setOnEventsActionListener { title, description ->
            viewModel.updateEvent(event.id, title, description)
        }
        dialog.show(parentFragmentManager, "EditEventDialog")
    }



}