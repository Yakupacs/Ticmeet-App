package com.mtaner.android_ticmeet.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.mtaner.android_ticmeet.R
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.databinding.FragmentChatBinding
import com.mtaner.android_ticmeet.databinding.FragmentEventsBinding
import com.mtaner.android_ticmeet.databinding.FragmentLoginBinding
import com.mtaner.android_ticmeet.databinding.FragmentProfileBinding
import com.mtaner.android_ticmeet.ui.activities.AuthenticationActivity
import com.mtaner.android_ticmeet.ui.activities.EditProfileActivity
import com.mtaner.android_ticmeet.ui.activities.MainActivity
import com.mtaner.android_ticmeet.ui.adapter.UserEventsAdapter
import com.mtaner.android_ticmeet.ui.viewmodel.AuthViewModel
import com.mtaner.android_ticmeet.ui.viewmodel.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private val eventsViewModel: EventsViewModel by viewModels()

    var currentUser: User? = null

    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var userEventsAdapter: UserEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        getUserData()

        initRv()
        getUserEvent()

        binding.buttonEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            intent.putExtra("user", currentUser)
            startActivity(intent)
        }

        binding.imageViewExit.setOnClickListener {
            authViewModel.logOut()
            val intent = Intent(requireContext(), AuthenticationActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.layoutFollowing.setOnClickListener {
            val bottomSheetDialog = FollowingFragment(emptyList())
            bottomSheetDialog.show(parentFragmentManager, "following")
        }

        binding.layoutFollowers.setOnClickListener {
            val bottomSheetDialog = FollowersFragment(emptyList())
            bottomSheetDialog.show(parentFragmentManager, "followers")
        }


        return binding.root
    }

    private fun getUserEvent() {
        val list = arrayListOf<EventItem>()
        eventsViewModel.getUserEvent(auth.currentUser!!.email!!)
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

    private fun initRv() {
        binding.recyclerViewUserEvents.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = userEventsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        getUserData()
    }

    private fun getUserData() {

        authViewModel.getUser(auth.currentUser!!.email!!)

        lifecycleScope.launch {
            authViewModel.userData.collect {
                if (it.isLoading) {

                }
                if (it.error.isNotBlank()) {

                }
                println("data----" + it.data)
                it.data?.let { user ->
                    currentUser = user
                    if (!user.userTopImage.isNullOrEmpty()) {
                        Glide.with(requireActivity()).load(user.userTopImage)
                            .into(binding.imageViewUserTopImage)
                    }
                    Glide.with(requireActivity()).load(user.userImage)
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