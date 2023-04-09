package com.mtaner.android_ticmeet.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.databinding.ItemAttendeesViewHolderBinding
import com.mtaner.android_ticmeet.ui.activities.EventDetailActivity
import javax.inject.Inject

class AttendeesEventAdapter @Inject constructor(): RecyclerView.Adapter<AttendeesEventAdapter.AttendeesViewHolder>() {

    private val userList = arrayListOf<User>()

    var attendeesEventClickListener: AttendeesEventClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setUserList(list: List<User>) {
        this.userList.clear()
        this.userList.addAll(list)
        notifyDataSetChanged()
    }


    class AttendeesViewHolder(itemView: ItemAttendeesViewHolderBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : ItemAttendeesViewHolderBinding = itemView
        fun bind(user: User) {
            Glide.with(binding.root.context).load(user.userImage).into(binding.imageViewUserPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeesViewHolder {
        return AttendeesViewHolder(ItemAttendeesViewHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: AttendeesViewHolder, position: Int) {
        holder.bind(userList[position])

        holder.itemView.setOnClickListener {
            attendeesEventClickListener?.onClick()
        }

    }

    interface AttendeesEventClickListener {
        fun onClick()
    }

}