package com.mtaner.android_ticmeet.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mtaner.android_ticmeet.R
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.data.model.UsersMessage
import com.mtaner.android_ticmeet.data.repository.AuthRepository
import com.mtaner.android_ticmeet.data.repository.EventsRepository
import com.mtaner.android_ticmeet.databinding.ItemAttendedUserViewHolderBinding
import com.mtaner.android_ticmeet.databinding.ItemSendMessageUserViewHolderBinding
import com.mtaner.android_ticmeet.ui.activities.MessageActivity
import com.mtaner.android_ticmeet.ui.activities.UserProfileActivity
import javax.inject.Inject

class SendMessageUserAdapter @Inject constructor(): RecyclerView.Adapter<SendMessageUserAdapter.SendMessageUserViewHolder>() {

    private val userList = arrayListOf<User>()

    @SuppressLint("NotifyDataSetChanged")
    fun setUserList(list: ArrayList<User>) {
        this.userList.clear()
        this.userList.addAll(list)
        notifyDataSetChanged()
    }


    class SendMessageUserViewHolder(itemView: ItemSendMessageUserViewHolderBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : ItemSendMessageUserViewHolderBinding = itemView
        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            Glide.with(binding.root.context).load(user.userImage).into(binding.imageViewUserPhoto)
            binding.textViewUserName.text = user.userName
            binding.textViewUserUserName.text = "@${user.userUsername}"

            binding.imageViewUserPhoto.setOnClickListener {
                val intent = Intent(binding.root.context,UserProfileActivity::class.java)
                intent.putExtra("user",user)
                binding.root.context.startActivity(intent)
            }

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context,MessageActivity::class.java)
                intent.putExtra("user",user)
                binding.root.context.startActivity(intent)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendMessageUserViewHolder {
        return SendMessageUserViewHolder(ItemSendMessageUserViewHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: SendMessageUserViewHolder, position: Int) {

        holder.bind(userList[position])



    }


}