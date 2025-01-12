package com.project.lycommunity.ui.notifications

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.project.lycommunity.R
import com.project.lycommunity.data.NotificationsRepository
import com.project.lycommunity.databinding.FragmentNotificationsBinding
import com.project.lycommunity.ui.adapters.NotificationsAdapter
import com.project.lycommunity.ui.login.LoginFragment
import kotlinx.coroutines.launch


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationsViewModel by viewModels {
        NotificationsViewModelFactory(NotificationsRepository())
    }

    private lateinit var adapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showLogoutConfirmationDialog()
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = NotificationsAdapter()
        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(requireContext()) // Set LayoutManager
        binding.notificationsRecyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.notificationsFlow.collect { notifications ->
                        Log.d("NotificationsFragment", "Notifications received: $notifications")
                        adapter.submitList(notifications)
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

    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                navigateToLoginFragment()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun navigateToLoginFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, LoginFragment())
            .addToBackStack(null) // Optional, depending on navigation flow
            .commit()
        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}