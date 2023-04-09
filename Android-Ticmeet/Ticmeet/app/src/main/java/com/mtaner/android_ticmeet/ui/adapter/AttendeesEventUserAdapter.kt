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
import com.mtaner.android_ticmeet.data.repository.AuthRepository
import com.mtaner.android_ticmeet.data.repository.EventsRepository
import com.mtaner.android_ticmeet.databinding.ItemAttendedUserViewHolderBinding
import com.mtaner.android_ticmeet.ui.activities.UserProfileActivity
import javax.inject.Inject

class AttendeesEventUserAdapter @Inject constructor(): RecyclerView.Adapter<AttendeesEventUserAdapter.AttendeesViewHolder>() {

    private val userList = arrayListOf<User>()

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    @Inject
    lateinit var authRepository: AuthRepository

    @SuppressLint("NotifyDataSetChanged")
    fun setUserList(list: List<User>) {
        this.userList.clear()
        this.userList.addAll(list)
        notifyDataSetChanged()
    }


    class AttendeesViewHolder(itemView: ItemAttendedUserViewHolderBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : ItemAttendedUserViewHolderBinding = itemView
        @SuppressLint("SetTextI18n")
        fun bind(user: User, auth: FirebaseAuth, firebaseFirestore: FirebaseFirestore,authRepository: AuthRepository) {
            Glide.with(binding.root.context).load(user.userImage).into(binding.imageViewUserPhoto)
            binding.textViewUserName.text = user.userName
            binding.textViewUserUserName.text = "@${user.userUsername}"
            binding.textViewUserEventCount.text = user.userEventsID?.size.toString() + " etkinliğe katıldı."

            if (auth.currentUser!!.email == user.userEmail) {
                binding.buttonFollow.visibility = View.INVISIBLE
            } else {
                binding.root.setOnClickListener {
                    val intent = Intent(binding.root.context,UserProfileActivity::class.java)
                    intent.putExtra("user",user)
                    binding.root.context.startActivity(intent)
                }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeesViewHolder {
        return AttendeesViewHolder(ItemAttendedUserViewHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: AttendeesViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        holder.bind(userList[position],auth,firebaseFirestore,authRepository)



    }


}