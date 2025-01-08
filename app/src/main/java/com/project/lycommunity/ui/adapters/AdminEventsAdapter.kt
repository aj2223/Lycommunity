package com.project.lycommunity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.lycommunity.R
import com.project.lycommunity.data.Events
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminEventsAdapter(
    private val onEditClicked: (Events) -> Unit,
    private val onDeleteClicked: (Events) -> Unit
) : ListAdapter<Events, AdminEventsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_events, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.event_title)
        private val description: TextView = itemView.findViewById(R.id.event_description)
        private val timestamp: TextView = itemView.findViewById(R.id.event_timestamp)
        private val editButton: ImageView = itemView.findViewById(R.id.edit_button)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        fun bind(event: Events) {
            title.text = event.title
            description.text = event.description
            timestamp.text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                .format(Date(event.timestamp))

            editButton.setOnClickListener { onEditClicked(event) }
            deleteButton.setOnClickListener { onDeleteClicked(event) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Events>() {
            override fun areItemsTheSame(oldItem: Events, newItem: Events): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Events, newItem: Events): Boolean =
                oldItem == newItem
        }
    }
}
