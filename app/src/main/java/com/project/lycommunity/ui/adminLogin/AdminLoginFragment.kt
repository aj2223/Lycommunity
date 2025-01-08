package com.project.lycommunity.ui.adminLogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.project.lycommunity.R
import com.project.lycommunity.data.AdminUserRepository
import com.project.lycommunity.databinding.FragmentAdminLoginBinding
import com.project.lycommunity.ui.admin.AdminFragment
import com.project.lycommunity.util.ResultsWrapper
import kotlinx.coroutines.launch


class AdminLoginFragment : Fragment() {

    private var _binding : FragmentAdminLoginBinding? = null
    private val binding get() = _binding!!

    private val repository = AdminUserRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToAdminSignUp()
        binding.loginAdminButton.setOnClickListener {
            val email = binding.adminEmail.text.toString()
            val password = binding.adminPass.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            verifyAdminCredentials(email, password)
        }
    }

    private fun verifyAdminCredentials(email: String, password: String) {
        lifecycleScope.launch {
            when (val result = repository.verifyAdminCredentials(email, password)) {
                is ResultsWrapper.Success -> {
                    if (result.data) {
                        navigateToAdminFeatures()
                    } else {
                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
                is ResultsWrapper.Error -> {
                    Toast.makeText(context, "Error: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    private fun navigateToAdminFeatures() {
        binding.loginAdminButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, AdminFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun navigateToAdminSignUp(){
        binding.signUpAdmin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, AdminSignUpFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}