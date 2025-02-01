package com.project.lycommunity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.lycommunity.R
import com.project.lycommunity.data.TestComment

class TestCommentsAdapter : ListAdapter<TestComment, TestCommentsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val commentUserName: TextView = itemView.findViewById(R.id.comment_user_name)
        private val commentText: TextView = itemView.findViewById(R.id.comment_text)
        private val commentTimestamp: TextView = itemView.findViewById(R.id.comment_timestamp)

        fun bind(comment: TestComment) {
            val fullName = "${comment.firstName} ${comment.lastName}".trim()
            commentUserName.text = if (fullName.isNotEmpty()) fullName else "Anonymous"
            commentText.text = comment.text
            commentTimestamp.text = formatTimestamp(comment.timestamp)
        }

        private fun formatTimestamp(timestamp: Long): String {
            val date = java.util.Date(timestamp)
            val format = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
            return format.format(date)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TestComment>() {
            override fun areItemsTheSame(oldItem: TestComment, newItem: TestComment): Boolean =
                oldItem.userId == newItem.userId && oldItem.timestamp == newItem.timestamp

            override fun areContentsTheSame(oldItem: TestComment, newItem: TestComment): Boolean =
                oldItem == newItem
        }
    }
}