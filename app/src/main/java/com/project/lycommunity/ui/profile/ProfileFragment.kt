package com.project.lycommunity.ui.profile

import android.os.Bundle
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.project.lycommunity.R
import com.project.lycommunity.data.ProfileRepository
import com.project.lycommunity.databinding.FragmentProfileBinding
import com.project.lycommunity.ui.login.LoginFragment
import com.project.lycommunity.util.AddDetailsDialogFragment
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the user's email passed during navigation
        val email = arguments?.getString("USER_EMAIL")
            ?: throw IllegalStateException("User email not found in arguments")

        observeViewModel()
        setupListeners()
        viewModel.loadUserDetails(email)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showLogoutConfirmationDialog()
            }
        })
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading

                    state.user?.let { user ->
                        binding.firstName.text = user.firstName
                        binding.lastName.text = user.lastName
                        binding.department.text = user.department
                        binding.email.text = user.email

                        binding.bio.text = if (user.bio.isNotBlank()) user.bio else "No Bio Added"
                        binding.likes.text = if (user.likes.isNotBlank()) user.likes else "No Likes Added"
                        binding.hobbies.text = if (user.hobbies.isNotBlank()) user.hobbies else "No Hobbies Added"
                    }

                    state.errorMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.clearErrorMessage()
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.addDetailsButton.setOnClickListener {
            showAddDetailsDialog()
        }
    }

    private fun showAddDetailsDialog() {
        val dialog = AddDetailsDialogFragment()
        dialog.setOnDetailsAddedListener { bio, likes, hobbies ->
            val email = arguments?.getString("USER_EMAIL")
                ?: throw IllegalStateException("User email not found in arguments")
            viewModel.addUserDetails(email, bio, likes, hobbies)
        }
        dialog.show(parentFragmentManager, "AddDetailsDialog")
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