package com.project.lycommunity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.lycommunity.R
import com.project.lycommunity.data.Announcement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminAnnouncementAdapter(
    private val onEditClicked: (Announcement) -> Unit,
    private val onDeleteClicked: (Announcement) -> Unit
) : ListAdapter<Announcement, AdminAnnouncementAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_announcement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.item_announcement_title)
        private val description = itemView.findViewById<TextView>(R.id.item_announcement_description)
        private val timestamp = itemView.findViewById<TextView>(R.id.item_announcement_timestamp)
        private val editButton = itemView.findViewById<ImageButton>(R.id.item_edit_button)
        private val deleteButton = itemView.findViewById<ImageButton>(R.id.item_delete_button)

        fun bind(announcement: Announcement) {
            title.text = announcement.title
            description.text = announcement.description
            timestamp.text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                .format(Date(announcement.timestamp))

            // Edit button callback
            editButton.setOnClickListener {
                onEditClicked(announcement)
            }

            // Delete button callback
            deleteButton.setOnClickListener {
                onDeleteClicked(announcement)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Announcement>() {
            override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean =
                oldItem == newItem
        }
    }
}


