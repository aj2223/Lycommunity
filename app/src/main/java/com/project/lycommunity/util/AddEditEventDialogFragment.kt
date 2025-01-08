package com.project.lycommunity.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.project.lycommunity.R
import com.project.lycommunity.data.Events

class AddEditEventDialogFragment : DialogFragment() {

    private var events: Events? = null
    private var onEventActionListener: ((String, String) -> Unit)? = null

    companion object {
        fun newInstance(events: Events?): AddEditEventDialogFragment {
            val fragment = AddEditEventDialogFragment()
            val args = Bundle()
            args.putParcelable("events", events)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the newer `getParcelable` with type safety
        events = arguments?.getParcelable<Events>("events")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_edit_event, null)

        val titleInput = view.findViewById<EditText>(R.id.event_title_dialog)
        val descriptionInput = view.findViewById<EditText>(R.id.event_description_dialog)

        // Prepopulate fields if editing
        events?.let {
            titleInput.setText(it.title)
            descriptionInput.setText(it.description)
        }

        builder.setView(view)
            .setTitle(if (events == null) "Add Events" else "Edit Events")
            .setPositiveButton("Save") { _, _ ->
                val title = titleInput.text.toString()
                val description = descriptionInput.text.toString()

                if (title.isBlank() || description.isBlank()) {
                    Toast.makeText(context, "All fields are required.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                onEventActionListener?.invoke(title, description)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        return builder.create()
    }

    fun setOnEventsActionListener(listener: (String, String) -> Unit) {
        onEventActionListener = listener
    }
}
