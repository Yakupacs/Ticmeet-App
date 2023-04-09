package com.mtaner.android_ticmeet.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.data.repository.AuthRepository
import com.mtaner.android_ticmeet.databinding.ActivityUserProfileBinding
import com.mtaner.android_ticmeet.ui.adapter.UserEventsAdapter
import com.mtaner.android_ticmeet.ui.viewmodel.AuthViewModel
import com.mtaner.android_ticmeet.ui.viewmodel.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    private val authViewModel: AuthViewModel by viewModels()
    private val eventsViewModel: EventsViewModel by viewModels()

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var userEventsAdapter: UserEventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        } else {
            intent.getParcelableExtra("user")
        }

        binding.imageViewBack.setOnClickListener {
            finish()
        }

        binding.buttonFollow.setOnClickListener {
            val isFollow = binding.buttonFollow.text.toString()
            if (isFollow == "Takip et") {
                authRepository.followUser(user!!,firebaseFirestore,auth,binding.buttonFollow)
            } else if (isFollow == "Takiptesin") {
                authRepository.unfollowUser(user!!,firebaseFirestore,auth,binding.buttonFollow)
            }
        }


        checkFollow(user!!)
        initRv()
        getUserData(user)
        getUserEvent(user)


    }

    private fun checkFollow(user: User) {
        authRepository.checkFollow(user,auth,firebaseFirestore,binding.buttonFollow)
    }

    private fun initRv() {
        binding.recyclerViewUserEvents.apply {
            layoutManager =
                LinearLayoutManager(this@UserProfileActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = userEventsAdapter
        }
    }

    private fun getUserEvent(user: User?) {
        val list = arrayListOf<EventItem>()
        eventsViewModel.getUserEvent(userEmail = user!!.userEmail!!)
        lifecycleScope.launch {
            eventsViewModel.userEvent.collect {

                if (it.isLoading) {
                    binding.progressBarEventLoading.visibility = View.VISIBLE
                    binding.recyclerViewUserEvents.visibility = View.GONE
                }
                it.data?.let { eventList ->
                    binding.progressBarEventLoading.visibility = View.GONE
                    binding.recyclerViewUserEvents.visibility = View.VISIBLE
                    list.addAll(eventList)
                    userEventsAdapter.setEventsList(list)
                }
            }
        }
    }

    private fun getUserData(user: User?) {

        authViewModel.getUser(userEmail = user!!.userEmail!!)

        lifecycleScope.launch {
            authViewModel.userData.collect {
                if (it.isLoading) {

                }
                if (it.error.isNotBlank()) {

                }
                println("data----" + it.data)
                it.data?.let { user ->
                    if (!user.userTopImage.isNullOrEmpty()) {
                        Glide.with(this@UserProfileActivity).load(user.userTopImage)
                            .into(binding.imageViewUserTopImage)
                    }
                    Glide.with(this@UserProfileActivity).load(user.userImage)
                        .into(binding.imageViewUserProfile)
                    binding.textViewUserNameToolbar.text = user.userName
                    binding.textViewUserName.text = user.userName
                    binding.textViewUserUserName.text = user.userUsername
                    binding.textViewBio.text = user.userBio
                    if (!user.userFollowers.isNullOrEmpty()) binding.textViewFollowers.text =
                        user.userFollowers?.size.toString()
                    if (!user.userFollowing.isNullOrEmpty()) binding.textViewFollowing.text =
                        user.userFollowing?.size.toString()
                    if (!user.userEventsID.isNullOrEmpty()) binding.textViewEventCount.text =
                        user.userEventsID?.size.toString()

                }
            }
        }
    }

}