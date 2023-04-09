package com.mtaner.android_ticmeet.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.data.repository.AuthRepository
import com.mtaner.android_ticmeet.databinding.ItemFollowViewHolderBinding
import com.mtaner.android_ticmeet.ui.activities.UserProfileActivity
import javax.inject.Inject

class FollowAdapter @Inject constructor(): RecyclerView.Adapter<FollowAdapter.SendMessageUserViewHolder>() {

    private val userList = arrayListOf<User>()


    @Inject
    lateinit var authRepository: AuthRepository

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    @SuppressLint("NotifyDataSetChanged")
    fun setUserList(list: ArrayList<User>) {
        this.userList.clear()
        this.userList.addAll(list)
        notifyDataSetChanged()
    }


    class SendMessageUserViewHolder(itemView: ItemFollowViewHolderBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : ItemFollowViewHolderBinding = itemView
        @SuppressLint("SetTextI18n")
        fun bind(
            user: User,
            authRepository: AuthRepository,
            firebaseFirestore: FirebaseFirestore,
            auth: FirebaseAuth
        ) {
            Glide.with(binding.root.context).load(user.userImage).into(binding.imageViewUserPhoto)
            binding.textViewUserName.text = user.userName
            binding.textViewUserUserName.text = "@${user.userUsername}"

            binding.imageViewUserPhoto.setOnClickListener {
                val intent = Intent(binding.root.context,UserProfileActivity::class.java)
                intent.putExtra("user",user)
                binding.root.context.startActivity(intent)
            }

            authRepository.checkFollow(user, auth, firebaseFirestore,binding.buttonFollow)

            binding.buttonFollow.setOnClickListener {
                val isFollow = binding.buttonFollow.text.toString()
                if (isFollow == "Takip et") {
                    authRepository.followUser(user,firebaseFirestore,auth,binding.buttonFollow)
                } else if (isFollow == "Takiptesin") {
                    authRepository.unfollowUser(user,firebaseFirestore,auth,binding.buttonFollow)
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendMessageUserViewHolder {
        return SendMessageUserViewHolder(ItemFollowViewHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: SendMessageUserViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        holder.bind(userList[position],authRepository,firebaseFirestore,auth)



    }


}