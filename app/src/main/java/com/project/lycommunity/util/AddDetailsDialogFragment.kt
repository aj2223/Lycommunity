package com.project.lycommunity.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.project.lycommunity.databinding.FragmentAddDetailsDialogBinding

class AddDetailsDialogFragment : DialogFragment() {

    private var _binding: FragmentAddDetailsDialogBinding? = null
    private val binding get() = _binding!!

    private var onDetailsAddedListener: ((String, String, String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDetailsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener {
            val bio = binding.bioInput.text.toString()
            val likes = binding.likesInput.text.toString()
            val hobbies = binding.hobbiesInput.text.toString()

            if (bio.isBlank() || likes.isBlank() || hobbies.isBlank()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            onDetailsAddedListener?.invoke(bio, likes, hobbies)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    fun setOnDetailsAddedListener(listener: (String, String, String) -> Unit) {
        onDetailsAddedListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
