package com.project.lycommunity.ui.adminAnnouncement

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
import com.project.lycommunity.data.Announcement
import com.project.lycommunity.data.AnnouncementRepository
import com.project.lycommunity.databinding.FragmentAdminAnnouncementBinding
import com.project.lycommunity.ui.adapters.AdminAnnouncementAdapter
import com.project.lycommunity.util.AddEditAnnouncementDialogFragment
import kotlinx.coroutines.launch

class AdminAnnouncementFragment : Fragment() {

    private var _binding: FragmentAdminAnnouncementBinding? = null
    private val binding get() = _binding!!

    private val viewModelOfAdminAnnouncement: AdminAnnouncementViewModel by viewModels {
        AdminAnnouncementViewModelFactory(AnnouncementRepository())
    }
    private lateinit var adapter: AdminAnnouncementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminAnnouncementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeAnnouncements()
        handleFabClick()
    }

    private fun setupRecyclerView() {
        adapter = AdminAnnouncementAdapter(
            onEditClicked = { announcement -> showEditDialog(announcement) },
            onDeleteClicked = { announcement -> viewModelOfAdminAnnouncement.deleteAnnouncement(announcement.id) }
        )
        binding.adminAnnouncementRecyclerView.layoutManager = LinearLayoutManager(requireContext()) // Set LayoutManager
        binding.adminAnnouncementRecyclerView.adapter = adapter // Assign Adapter
    }

    private fun observeAnnouncements() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModelOfAdminAnnouncement.announcementsFlow.collect { announcements ->
                        adapter.submitList(announcements)
                    }
                }
                launch {
                    viewModelOfAdminAnnouncement.uiState.collect { state ->
                        binding.progressBar.isVisible = state.isLoading

                        state.successMessage?.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            viewModelOfAdminAnnouncement.clearSuccessMessage() // Clear message after showing
                        }

                        state.errorMessage?.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                            viewModelOfAdminAnnouncement.clearErrorMessage() // Clear message after showing
                        }
                    }
                }
            }
        }
    }


    private fun handleFabClick() {
        binding.addAnnouncementFab.setOnClickListener {
            showAddAnnouncementDialog()
        }
    }

    private fun showAddAnnouncementDialog() {
        val dialog = AddEditAnnouncementDialogFragment.newInstance(null)
        dialog.setOnAnnouncementActionListener { title, description ->
            viewModelOfAdminAnnouncement.addAnnouncement(title, description)
        }
        dialog.show(parentFragmentManager, "AddAnnouncementDialog")
    }

    private fun showEditDialog(announcement: Announcement) {
        val dialog = AddEditAnnouncementDialogFragment.newInstance(announcement)
        dialog.setOnAnnouncementActionListener { title, description ->
            viewModelOfAdminAnnouncement.updateAnnouncement(announcement.id, title, description)
        }
        dialog.show(parentFragmentManager, "EditAnnouncementDialog")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}