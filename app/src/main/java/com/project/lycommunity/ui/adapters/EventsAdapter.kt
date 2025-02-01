package com.project.lycommunity.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.lycommunity.R
import com.project.lycommunity.data.Comment
import com.project.lycommunity.data.Events
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventsAdapter(
    private val onCommentClicked: (String) -> Unit,
    private val onCommentSubmit: (String, String) -> Unit,
    private val fetchComments: (String, (List<Comment>) -> Unit) -> Unit
) : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    private var eventList: List<Events> = emptyList()
    private val eventComments = mutableMapOf<String, List<Comment>>() // Store comments per event

    fun submitList(events: List<Events>) {
        this.eventList = events
        notifyDataSetChanged()
    }

    fun getComments(eventId: String): List<Comment> {
        return eventComments[eventId] ?: emptyList()
    }

    fun setComments(eventId: String, comments: List<Comment>) {
        if (comments != getComments(eventId)) {
            eventComments[eventId] = comments
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    override fun getItemCount(): Int = eventList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.event_title)
        private val description: TextView = itemView.findViewById(R.id.event_description)
        private val timestamp: TextView = itemView.findViewById(R.id.event_timestamp)
        private val btnComment: Button = itemView.findViewById(R.id.btn_comment)
        private val commentSection: View = itemView.findViewById(R.id.commentSection)
        private val commentInput: EditText = itemView.findViewById(R.id.commentInput)
        private val commentButton: Button = itemView.findViewById(R.id.commentButton)
        private val commentsRecyclerView: RecyclerView = itemView.findViewById(R.id.commentsRecyclerView)

        fun bind(event: Events) {
            title.text = event.title
            description.text = event.description
            timestamp.text = event.timestamp.toString()

            // Ensure commentsRecyclerView has a LayoutManager
            if (commentsRecyclerView.layoutManager == null) {
                commentsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            }

            btnComment.setOnClickListener {
                if (commentSection.visibility == View.GONE) {
                    commentSection.visibility = View.VISIBLE
                    onCommentClicked(event.id)
                    fetchComments(event.id) { comments ->
                        if (comments != getComments(event.id)) {
                            commentsRecyclerView.adapter = CommentsAdapter().apply {
                                submitList(comments)
                            }
                            setComments(event.id, comments)
                        }
                    }
                } else {
                    commentSection.visibility = View.GONE
                }
            }

            commentButton.setOnClickListener {
                val commentText = commentInput.text.toString().trim()
                if (commentText.isNotEmpty()) {
                    onCommentSubmit(event.id, commentText)
                    commentInput.text.clear()
                }
            }
        }
    }
}