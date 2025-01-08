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

class UserEventsAdapter(
    private val onLikeClicked: (Events) -> Unit,
    private val onDislikeClicked: (Events) -> Unit
) : ListAdapter<Events, UserEventsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_events, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.events_title)
        private val description: TextView = itemView.findViewById(R.id.events_description)
        private val timestamp: TextView = itemView.findViewById(R.id.events_timestamp)
        private val likeIcon: ImageView = itemView.findViewById(R.id.like_icon_events)
        private val dislikeIcon: ImageView = itemView.findViewById(R.id.dislike_icon_events)
        private val likeCount: TextView = itemView.findViewById(R.id.like_count_events)
        private val dislikeCount: TextView = itemView.findViewById(R.id.dislike_count_events)

        fun bind(event: Events) {
            title.text = event.title
            description.text = event.description
            timestamp.text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                .format(Date(event.timestamp))
            likeCount.text = event.likes.toString()
            dislikeCount.text = event.dislikes.toString()

            // Handle Like interaction
            likeIcon.setOnClickListener {
                onLikeClicked(event)
            }

            // Handle Dislike interaction
            dislikeIcon.setOnClickListener {
                onDislikeClicked(event)
            }
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
