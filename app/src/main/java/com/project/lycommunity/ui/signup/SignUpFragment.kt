package com.project.lycommunity.ui.signup

import SignUpValidationHelper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.project.lycommunity.data.UserRepository
import com.project.lycommunity.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModelOfSignUp: SignUpViewModel by activityViewModels() {
        SignUpViewModelFactory(UserRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()


    }

    private fun setupListeners() {
        val validationHelper = SignUpValidationHelper()
        binding.signUpButton.setOnClickListener {
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()
            val firstName = binding.registerName.text.toString()
            val lastName = binding.registerLastName.text.toString()
            val department = binding.registerDepartment.text.toString()
            val enteredId = binding.inputId.text.toString()

            val validations = validationHelper.validateAllFields(
                email, password, firstName, lastName, department, enteredId
            )
            val firstError = validations.firstOrNull { !it.isValid }

            if (firstError != null) {
                Toast.makeText(context, firstError.errorMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModelOfSignUp.register(email, password, firstName, lastName, department, enteredId)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModelOfSignUp.signUpStateFlow.collect { state ->
                    binding.signUpButton.isEnabled = !state.isLoading

                    if (state.isSuccess) {
                        Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                        clearInputFields()
                        viewModelOfSignUp.resetState()
                    }

                    if (!state.errorMessage.isNullOrEmpty()) {
                        Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
                        viewModelOfSignUp.resetState()
                    }
                }
            }
        }
    }

    private fun clearInputFields() {
        binding.registerEmail.text?.clear()
        binding.registerPassword.text?.clear()
        binding.registerName.text?.clear()
        binding.registerLastName.text?.clear()
        binding.registerDepartment.text?.clear()
        binding.inputId.text?.clear()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}