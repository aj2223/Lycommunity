package com.project.lycommunity.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.firestore.FirebaseFirestore
import com.project.lycommunity.R
import com.project.lycommunity.databinding.FragmentLoginBinding
import com.project.lycommunity.ui.adminLogin.login.AdminLoginFragment
import com.project.lycommunity.ui.analytics.AnalyticsFragment
import com.project.lycommunity.ui.forgotpass.ForgotPasswordFragment
import com.project.lycommunity.ui.home.HomeFragment
import com.project.lycommunity.ui.signup.SignUpFragment
import com.project.lycommunity.util.LoginValidationHelper
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModelOfLogin : LoginViewModel by activityViewModels() {LoginViewModelFactory()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupListeners()
        observeViewModel()
        goToSignUpFragment()
        goToAdminFragment()
        goToForgotPasswordFragment()

    }

    private fun setupListeners() {
        val validationHelper = LoginValidationHelper()

        binding.signInButton.setOnClickListener {
            val email = binding.enterEmail.text.toString()
            val password = binding.enterPassword.text.toString()

            val emailValidation = validationHelper.validateCredentials(email, password).first
            val passwordValidation = validationHelper.validateCredentials(email, password).second

            if (!emailValidation.isValid) {
                Toast.makeText(context, emailValidation.errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!passwordValidation.isValid) {
                Toast.makeText(context, passwordValidation.errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModelOfLogin.login(email, password)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelOfLogin.loginStateFlow.collect { state ->
                    binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    binding.signInButton.isEnabled = !state.isLoading

                    if (state.isSuccess) {
                        Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                        navigateToHomeFragment()
                        viewModelOfLogin.resetState()
                    }

                    if (!state.errorMessage.isNullOrEmpty()) {
                        Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
                        viewModelOfLogin.resetState()
                    }
                }
            }
        }
    }




    private fun navigateToHomeFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, HomeFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun goToSignUpFragment(){
        binding.txtGoToSignUp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, SignUpFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun goToAdminFragment(){
        binding.txtSignInAsAdmin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, AdminLoginFragment())
                .addToBackStack(null)
                .commit()
        }
    }


    private fun goToForgotPasswordFragment(){
        binding.txtForgotPassword.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, ForgotPasswordFragment())
                .addToBackStack(null)
                .commit()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}