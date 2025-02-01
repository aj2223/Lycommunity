package com.project.lycommunity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.lycommunity.R
import com.project.lycommunity.data.Announcement
import com.project.lycommunity.data.TestComment

class TestAnnouncementAdapter(
    private val onCommentClicked: (String) -> Unit,
    private val onCommentSubmit: (String, String) -> Unit,
    private val fetchComments: (String, (List<TestComment>) -> Unit) -> Unit
) : RecyclerView.Adapter<TestAnnouncementAdapter.ViewHolder>() {

    private var announcementList: List<Announcement> = emptyList()
    private val announcementComments = mutableMapOf<String, List<TestComment>>() // Store comments per announcement

    fun submitList(announcements: List<Announcement>) {
        this.announcementList = announcements
        notifyDataSetChanged()
    }

    fun getComments(announcementId: String): List<TestComment> {
        return announcementComments[announcementId] ?: emptyList()
    }

    fun setComments(announcementId: String, comments: List<TestComment>) {
        if (comments != getComments(announcementId)) {
            announcementComments[announcementId] = comments
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test_announcement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(announcementList[position])
    }

    override fun getItemCount(): Int = announcementList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.item_announcement_title)
        private val description: TextView = itemView.findViewById(R.id.item_announcement_description)
        private val timestamp: TextView = itemView.findViewById(R.id.item_announcement_timestamp)
        private val btnComment: Button = itemView.findViewById(R.id.btn_comment)
        private val commentSection: View = itemView.findViewById(R.id.commentSection)
        private val commentInput: EditText = itemView.findViewById(R.id.commentInput)
        private val commentButton: Button = itemView.findViewById(R.id.commentButton)
        private val commentsRecyclerView: RecyclerView = itemView.findViewById(R.id.commentsRecyclerView)

        fun bind(announcement: Announcement) {
            title.text = announcement.title
            description.text = announcement.description
            timestamp.text = announcement.timestamp.toString()

            if (commentsRecyclerView.layoutManager == null) {
                commentsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            }

            btnComment.setOnClickListener {
                if (commentSection.visibility == View.GONE) {
                    commentSection.visibility = View.VISIBLE
                    onCommentClicked(announcement.id)
                    fetchComments(announcement.id) { comments ->
                        if (comments != getComments(announcement.id)) {
                            commentsRecyclerView.adapter = TestCommentsAdapter().apply {
                                submitList(comments)
                            }
                            setComments(announcement.id, comments)
                        }
                    }
                } else {
                    commentSection.visibility = View.GONE
                }
            }

            commentButton.setOnClickListener {
                val commentText = commentInput.text.toString().trim()
                if (commentText.isNotEmpty()) {
                    onCommentSubmit(announcement.id, commentText)
                    commentInput.text.clear()
                }
            }
        }
    }
}