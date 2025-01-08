package com.project.lycommunity.ui.adminLogin.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.project.lycommunity.R
import com.project.lycommunity.data.AdminUserRepository
import com.project.lycommunity.databinding.FragmentAdminSignUpBinding
import com.project.lycommunity.ui.adminLogin.login.AdminLoginFragment
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.launch


class AdminSignUpFragment : Fragment() {

    private var _binding : FragmentAdminSignUpBinding? = null
    private val binding get() = _binding!!
    private val repository = AdminUserRepository()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerAdminButton.setOnClickListener {
            val email = binding.adminEmailInput.text.toString()
            val password = binding.adminPasswordInput.text.toString()
            val confirmPassword = binding.adminConfirmPasswordInput.text.toString()

            if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                binding.progressBar.visibility = View.VISIBLE
                when (val result = repository.registerAdmin(email, password)) {
                    is ResultsWrapper.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Admin registered successfully!", Toast.LENGTH_SHORT).show()
                        navigateToAdminLogin()
                    }
                    is ResultsWrapper.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun registerAdmin(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            when (val result = repository.registerAdmin(email, password)) {
                is ResultsWrapper.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Admin registered successfully!", Toast.LENGTH_SHORT).show()
                    navigateToAdminLogin()
                }
                is ResultsWrapper.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    private fun navigateToAdminLogin(){
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, AdminLoginFragment())
            .addToBackStack(null)
            .commit()
    }


}