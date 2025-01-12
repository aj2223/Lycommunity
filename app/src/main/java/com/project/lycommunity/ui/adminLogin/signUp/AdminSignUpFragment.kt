package com.project.lycommunity.ui.adminLogin.signUp

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
import com.project.lycommunity.R
import com.project.lycommunity.data.AdminUserRepository
import com.project.lycommunity.databinding.FragmentAdminSignUpBinding
import com.project.lycommunity.ui.adminLogin.login.AdminLoginFragment
import com.project.lycommunity.ui.adminLogin.login.AdminLoginViewModel
import com.project.lycommunity.ui.adminLogin.login.AdminLoginViewModelFactory
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.launch


class AdminSignUpFragment : Fragment() {

    private var _binding: FragmentAdminSignUpBinding? = null
    private val binding get() = _binding!!
    private val repository = AdminUserRepository()

    private val viewModel: AdminSignUpViewModel by viewModels {
        AdminSignUpViewModelFactory(AdminUserRepository())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRegisterButton()
        observeUIState()
    }

    private fun setupRegisterButton() {
        binding.registerAdminButton.setOnClickListener {
            val email = binding.adminEmailInput.text.toString()
            val password = binding.adminPasswordInput.text.toString()
            val confirmPassword = binding.adminConfirmPasswordInput.text.toString()
            val department = binding.adminDepartmentInput.text.toString()

            if (email.isBlank() || password.isBlank() || confirmPassword.isBlank() || department.isBlank()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.registerAdmin(email, password, department)
        }
    }


    private fun observeUIState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.visibility =
                        if (state.isLoading) View.VISIBLE else View.GONE

                    state.message?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }

                    if (state.isSuccess) {
                        navigateToAdminLogin()
                    }
                }
            }
        }
    }

    private fun navigateToAdminLogin() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, AdminLoginFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}