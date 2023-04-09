package com.mtaner.android_ticmeet.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.mtaner.android_ticmeet.R
import com.mtaner.android_ticmeet.data.model.Message
import com.mtaner.android_ticmeet.data.model.Messages
import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Inject

class MessagesAdapter @Inject constructor() :
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    private val messageList = arrayListOf<Message>()

    private var messages : Messages? = null

    private var msg_right = 0
    private var msg_left = 1
    var right = ""

    private lateinit var auth: FirebaseAuth

    @SuppressLint("NotifyDataSetChanged")
    fun setMessageList(list: List<Message>) {
        this.messageList.clear()
        this.messageList.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMessages(messages: Messages) {
        this.messages = messages
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserMessage = itemView.findViewById<TextView>(R.id.textViewUserMessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == msg_right) {
            right = "right"
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.message_right_view_holder, parent, false)
            )
        } else {
            right = "left"
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.message_left_view_holder, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentList = messageList[position]



        holder.textViewUserMessage.text = currentList.message

        //holder.bind(currentList)

    }


    override fun getItemViewType(position: Int): Int {
        auth = FirebaseAuth.getInstance()

        if (messageList[position].messageUserEmail == auth.currentUser!!.email) {
            return msg_right
        } else {
            return msg_left
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }


}