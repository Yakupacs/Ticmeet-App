package com.mtaner.android_ticmeet.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mtaner.android_ticmeet.R
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.data.repository.EventsRepository
import com.mtaner.android_ticmeet.databinding.ItemEventBinding
import com.mtaner.android_ticmeet.ui.activities.EventDetailActivity
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EventItemAdapter @Inject constructor(): RecyclerView.Adapter<EventItemAdapter.EventItemViewHolder>() {

    private val eventItemList = arrayListOf<EventItem>()

    var eventOnClickListener : EventOnClickListener? = null

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var eventsRepository: EventsRepository

    @SuppressLint("NotifyDataSetChanged")
    fun setEventItemList(list: List<EventItem>,eventName: String) {
        this.eventItemList.clear()
        this.eventItemList.addAll(list.filter { it.eventCategory == eventName })
        notifyDataSetChanged()
    }

    fun getEventItemList(eventName: String): Boolean {
        return this.eventItemList.none { it.eventCategory == eventName }
    }


    class EventItemViewHolder(itemView: ItemEventBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : ItemEventBinding = itemView
        fun bind(event: EventItem) {
            Glide.with(binding.cardView.context).load(event.eventImage).into(binding.imageViewEvent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemViewHolder {
        return EventItemViewHolder(ItemEventBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return eventItemList.size
    }

    override fun onBindViewHolder(holder: EventItemViewHolder, position: Int) {
        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        holder.bind(eventItemList[position])
        val event : EventItem = eventItemList[position]
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,EventDetailActivity::class.java)
            intent.putExtra("event",event)
            holder.itemView.context.startActivity(intent)
        }



        changeButton(event,holder)

        holder.binding.buttonAdd.setOnClickListener {

            val isCheck = holder.binding.textViewCheck.text.toString()
            if (isCheck == "add") {
                eventsRepository.joinEvent(event)

            } else if (isCheck == "check") {
                eventsRepository.removeJoinEvent(event)
            }


        }



    }

    private fun goToDetail(event: EventItem, context: Context) {
        val intent = Intent(context,EventDetailActivity::class.java)
        intent.putExtra("event",event)
        context.startActivity(intent)
    }

    private fun changeButton(event: EventItem, holder: EventItemViewHolder) {
        var emailArray = arrayListOf<String>()
        firebaseFirestore.collection("Event").whereEqualTo("eventID",event.eventID).addSnapshotListener { value, error ->


            value?.documents?.forEach { document ->
                val userEmail = document.get("eventUsersEmail")
                val eventAttented  = document.data?.get("eventAttented")
                println("userEmail"+ userEmail)
                println("eventAttented"+ eventAttented)

                if (userEmail != null)
                    emailArray = userEmail as ArrayList<String>


            }

            if (emailArray.find { it == auth.currentUser!!.email.toString() }.isNullOrEmpty()) {
                holder.binding.buttonAdd.setBackgroundResource(R.drawable.add)
                holder.binding.textViewCheck.text = "add"
            } else  {
                holder.binding.buttonAdd.setBackgroundResource(R.drawable.check)
                holder.binding.textViewCheck.text = "check"
            }

        }
    }






    interface EventOnClickListener {
        fun onClick(event: EventItem, button : AppCompatButton)
    }

}
