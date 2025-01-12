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
import com.project.lycommunity.data.Announcement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserAnnouncementAdapter(
    private val onLikeClicked: (String) -> Unit,
    private val onDislikeClicked: (String) -> Unit
) : ListAdapter<Announcement, UserAnnouncementAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_announcement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.announcement_title)
        private val description = itemView.findViewById<TextView>(R.id.announcement_description)
        private val timestamp = itemView.findViewById<TextView>(R.id.announcement_timestamp)
        private val likeIcon = itemView.findViewById<ImageView>(R.id.like_icon)
        private val dislikeIcon = itemView.findViewById<ImageView>(R.id.dislike_icon)
        private val likeCount = itemView.findViewById<TextView>(R.id.like_count)
        private val dislikeCount = itemView.findViewById<TextView>(R.id.dislike_count)
        private val department = itemView.findViewById<TextView>(R.id.department_rv_announcement)

        fun bind(announcement: Announcement) {
            title.text = announcement.title
            description.text = announcement.description
            timestamp.text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                .format(Date(announcement.timestamp))
            likeCount.text = announcement.likes.toString()
            dislikeCount.text = announcement.dislikes.toString()
            department.text = announcement.department // Bind the department

            // Handle like and dislike interactions
            likeIcon.setOnClickListener {
                onLikeClicked(announcement.id)
            }
            dislikeIcon.setOnClickListener {
                onDislikeClicked(announcement.id)
            }
        }
    }

//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val title = itemView.findViewById<TextView>(R.id.announcement_title)
//        private val description = itemView.findViewById<TextView>(R.id.announcement_description)
//        private val timestamp = itemView.findViewById<TextView>(R.id.announcement_timestamp)
//        private val likeIcon = itemView.findViewById<ImageView>(R.id.like_icon)
//        private val dislikeIcon = itemView.findViewById<ImageView>(R.id.dislike_icon)
//        private val likeCount = itemView.findViewById<TextView>(R.id.like_count)
//        private val dislikeCount = itemView.findViewById<TextView>(R.id.dislike_count)
//
//        fun bind(announcement: Announcement) {
//            title.text = announcement.title
//            description.text = announcement.description
//            timestamp.text = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
//                .format(Date(announcement.timestamp))
//            likeCount.text = announcement.likes.toString()
//            dislikeCount.text = announcement.dislikes.toString()
//
//            // Handle like and dislike interactions
//            likeIcon.setOnClickListener {
//                onLikeClicked(announcement.id)
//            }
//            dislikeIcon.setOnClickListener {
//                onDislikeClicked(announcement.id)
//            }
//        }
//    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Announcement>() {
            override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean =
                oldItem == newItem
        }
    }
}


