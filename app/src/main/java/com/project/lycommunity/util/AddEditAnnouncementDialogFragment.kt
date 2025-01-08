package com.project.lycommunity.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.project.lycommunity.R
import com.project.lycommunity.data.Announcement


class AddEditAnnouncementDialogFragment : DialogFragment() {

    private var announcement: Announcement? = null
    private var onAnnouncementActionListener: ((String, String) -> Unit)? = null

    companion object {
        fun newInstance(announcement: Announcement?): AddEditAnnouncementDialogFragment {
            val fragment = AddEditAnnouncementDialogFragment()
            val args = Bundle()
            args.putParcelable("announcement", announcement)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the newer `getParcelable` with type safety
        announcement = arguments?.getParcelable<Announcement>("announcement")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_edit_announcement, null)

        val titleInput = view.findViewById<EditText>(R.id.title_input_add_edit)
        val descriptionInput = view.findViewById<EditText>(R.id.description_input_add_edit)

        // Prepopulate fields if editing
        announcement?.let {
            titleInput.setText(it.title)
            descriptionInput.setText(it.description)
        }

        builder.setView(view)
            .setTitle(if (announcement == null) "Add Announcement" else "Edit Announcement")
            .setPositiveButton("Save") { _, _ ->
                val title = titleInput.text.toString()
                val description = descriptionInput.text.toString()

                if (title.isBlank() || description.isBlank()) {
                    Toast.makeText(context, "All fields are required.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                onAnnouncementActionListener?.invoke(title, description)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        return builder.create()
    }

    fun setOnAnnouncementActionListener(listener: (String, String) -> Unit) {
        onAnnouncementActionListener = listener
    }
}
