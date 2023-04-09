package com.mtaner.android_ticmeet.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtaner.android_ticmeet.data.model.Comment
import com.mtaner.android_ticmeet.databinding.EventCommentViewHolderBinding
import com.mtaner.android_ticmeet.ui.activities.EventDetailActivity
import javax.inject.Inject

class EventCommentAdapter @Inject constructor(): RecyclerView.Adapter<EventCommentAdapter.AttendeesViewHolder>() {

    private val commentList = arrayListOf<Comment>()

    @SuppressLint("NotifyDataSetChanged")
    fun setCommentList(list: List<Comment>) {
        this.commentList.clear()
        this.commentList.addAll(list)
        notifyDataSetChanged()
    }


    class AttendeesViewHolder(itemView: EventCommentViewHolderBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : EventCommentViewHolderBinding = itemView
        @SuppressLint("SetTextI18n")
        fun bind(comment: Comment) {
            binding.textViewUserName.text = comment.commentUserName
            binding.textViewUserUsername.text = "@${comment.commentUserUsername}"
            binding.textViewComment.text = comment.comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeesViewHolder {
        return AttendeesViewHolder(EventCommentViewHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: AttendeesViewHolder, position: Int) {

        holder.bind(commentList[position])

    }


}