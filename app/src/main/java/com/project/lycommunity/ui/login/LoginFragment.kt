package com.project.lycommunity.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.project.lycommunity.R
import com.project.lycommunity.data.UserRepository
import com.project.lycommunity.databinding.FragmentLoginBinding
import com.project.lycommunity.ui.parent.ParentFragment
import com.project.lycommunity.ui.signup.SignUpFragment
import com.project.lycommunity.util.LoginValidationHelper
import kotlinx.coroutines.flow.collectLatest
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
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModelOfLogin.loginStateFlow.collect { state ->
                    binding.signInButton.isEnabled = !state.isLoading

                    if (state.isSuccess) {
                        Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                        goToParentFragment()
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

    private fun goToParentFragment(){
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, ParentFragment())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}