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
import com.project.lycommunity.data.Events
import com.project.lycommunity.data.TestComment

class TestEventsAdapter (
    private val onCommentClicked: (String) -> Unit,
    private val onCommentSubmit: (String, String) -> Unit,
    private val fetchComments: (String, (List<TestComment>) -> Unit) -> Unit
) : RecyclerView.Adapter<TestEventsAdapter.ViewHolder>() {

    private var eventList: List<Events> = emptyList()
    private val eventComments = mutableMapOf<String, List<TestComment>>() // Store comments per event

    fun submitList(events: List<Events>) {
        this.eventList = events
        notifyDataSetChanged()
    }

    fun getComments(eventId: String): List<TestComment> {
        return eventComments[eventId] ?: emptyList()
    }

    fun setComments(eventId: String, comments: List<TestComment>) {
        if (comments != getComments(eventId)) {
            eventComments[eventId] = comments
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_test_event, parent, false)
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

            if (commentsRecyclerView.layoutManager == null) {
                commentsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            }

            btnComment.setOnClickListener {
                if (commentSection.visibility == View.GONE) {
                    commentSection.visibility = View.VISIBLE
                    onCommentClicked(event.id)
                    fetchComments(event.id) { comments ->
                        if (comments != getComments(event.id)) {
                            commentsRecyclerView.adapter = TestCommentsAdapter().apply {
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