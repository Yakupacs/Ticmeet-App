package com.mtaner.android_ticmeet.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mtaner.android_ticmeet.data.model.Message
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.data.model.UsersMessage
import com.mtaner.android_ticmeet.databinding.ItemMyMessagesViewHolderBinding
import com.mtaner.android_ticmeet.ui.activities.MessageActivity
import com.mtaner.android_ticmeet.ui.activities.UserProfileActivity
import javax.inject.Inject

class MyMessagesAdapter @Inject constructor(): RecyclerView.Adapter<MyMessagesAdapter.SendMessageUserViewHolder>() {

    private val userList = arrayListOf<UsersMessage>()

    @SuppressLint("NotifyDataSetChanged")
    fun setUserMessagesList(list: List<UsersMessage>) {
        this.userList.clear()
        this.userList.addAll(list)
        notifyDataSetChanged()
    }



    class SendMessageUserViewHolder(itemView: ItemMyMessagesViewHolderBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : ItemMyMessagesViewHolderBinding = itemView
        @SuppressLint("SetTextI18n")
        fun bind(user: User, lastMessage: Message) {
            Glide.with(binding.root.context).load(user.userImage).into(binding.imageViewUserPhoto)
            binding.textViewUserName.text = user.userName
            binding.textViewLastMessage.text = lastMessage.message

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
        return SendMessageUserViewHolder(ItemMyMessagesViewHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: SendMessageUserViewHolder, position: Int) {

        holder.bind(userList[position].user,userList[position].lastMessage)



    }


}