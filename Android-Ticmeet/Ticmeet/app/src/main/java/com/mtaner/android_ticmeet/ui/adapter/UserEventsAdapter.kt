package com.mtaner.android_ticmeet.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.databinding.ItemEventBinding
import com.mtaner.android_ticmeet.ui.activities.EventDetailActivity
import javax.inject.Inject

class UserEventsAdapter @Inject constructor(): RecyclerView.Adapter<UserEventsAdapter.UserEventsViewHolder>() {

    private val eventList = arrayListOf<EventItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setEventsList(list: List<EventItem>) {
        this.eventList.clear()
        this.eventList.addAll(list)
        notifyDataSetChanged()
    }


    class UserEventsViewHolder(itemView: ItemEventBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : ItemEventBinding = itemView
        fun bind(event: EventItem) {
            binding.buttonAdd.visibility = View.GONE
            Glide.with(binding.root.context).load(event.eventImage).into(binding.imageViewEvent)

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, EventDetailActivity::class.java)
                intent.putExtra("event",event)
                binding.root.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserEventsViewHolder {
        return UserEventsViewHolder(ItemEventBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: UserEventsViewHolder, position: Int) {
        holder.bind(eventList[position])



    }


}