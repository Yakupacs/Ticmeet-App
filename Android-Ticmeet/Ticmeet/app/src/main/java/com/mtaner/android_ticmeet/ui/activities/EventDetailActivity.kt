package com.mtaner.android_ticmeet.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mtaner.android_ticmeet.R
import com.mtaner.android_ticmeet.data.model.Comment
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.data.repository.EventsRepository
import com.mtaner.android_ticmeet.databinding.ActivityEventDetailBinding
import com.mtaner.android_ticmeet.ui.adapter.AttendeesEventAdapter
import com.mtaner.android_ticmeet.ui.adapter.EventCommentAdapter
import com.mtaner.android_ticmeet.ui.fragment.EventAttendedFragment
import com.mtaner.android_ticmeet.ui.fragment.EventCommentFragment
import com.mtaner.android_ticmeet.ui.viewmodel.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var attendeesEventAdapter: AttendeesEventAdapter

    @Inject
    lateinit var eventCommentAdapter: EventCommentAdapter

    private val eventsViewModel: EventsViewModel by viewModels()

    var userList: List<User>? = null

    @Inject
    lateinit var eventsRepository: EventsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.imageViewBack.setOnClickListener {
            finish()
        }


        val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("event", EventItem::class.java)
        } else {
            intent.getParcelableExtra("event")
        }

        eventsViewModel.getEventEventId(event?.eventID!!)
        lifecycleScope.launch {
            eventsViewModel.eventEventId.collect {
                if (it.isLoading) {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.nestedScroolView.visibility = View.GONE
                }
                it.data.let {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.nestedScroolView.visibility = View.VISIBLE
                    println("event---------"+ it)
                }
                if (it.data != null) {
                    setData(it.data)
                    getUserAttendees(it.data)
                    getUserComment(it.data)
                    checkJoin(it.data)
                }
            }
        }

        initRv()
        initRvComment()


        binding.textViewSendComment.setOnClickListener {
            sendCommentDialog(event)
        }


        binding.imageViewCheck.setOnClickListener {

            val isCheck = binding.textViewCheck.text.toString()
            if (isCheck == "add") {
                eventsRepository.joinEvent(event)

            } else if (isCheck == "check") {
                eventsRepository.removeJoinEvent(event)
            }
        }

        attendeesEventAdapter.attendeesEventClickListener =
            object : AttendeesEventAdapter.AttendeesEventClickListener {
                override fun onClick() {
                    userList?.let { it1 -> sendAttendedUserDialog(it1) }
                }

            }

        binding.layoutEventAttended.setOnClickListener {
            userList?.let { it1 -> sendAttendedUserDialog(it1) }
        }


    }

    private fun checkJoin(event: EventItem) {
        var emailArray = arrayListOf<String>()
        firebaseFirestore.collection("Event").whereEqualTo("eventID",event.eventID).addSnapshotListener { value, error ->


            value?.documents?.forEach { document ->
                val userEmail = document.get("eventUsersEmail")
                val eventAttented  = document.data?.get("eventAttented")

                if (userEmail != null)
                    emailArray = userEmail as ArrayList<String>


            }

            if (emailArray.find { it == auth.currentUser!!.email.toString() }.isNullOrEmpty()) {
                binding.imageViewCheck.setBackgroundResource(R.drawable.check_mark)
                binding.textViewCheck.text = "add"
            } else  {
                binding.imageViewCheck.setBackgroundResource(R.drawable.uncheck)
                binding.textViewCheck.text = "check"
            }

        }
    }

    private fun sendCommentDialog(event: EventItem) {

        val bottomSheetDialog = EventCommentFragment(event)
        bottomSheetDialog.show(supportFragmentManager, "comment")

    }

    private fun sendAttendedUserDialog(userList: List<User>) {

        val bottomSheetDialog = EventAttendedFragment(userList)
        bottomSheetDialog.show(supportFragmentManager, "attended")

    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun getUserComment(event: EventItem) {
        val list = arrayListOf<Comment>()
        eventsViewModel.getUserComment(event)
        lifecycleScope.launch {
            eventsViewModel.comment.collect {
                if (it.isLoading) {
                    /*
                    binding.progressBarEventAttend.visibility = View.VISIBLE
                    binding.recyclerViewEventAttend.visibility = View.GONE

                     */
                }
                it.data?.let { commentList ->

                    //list.add(commentList)
                    eventCommentAdapter.setCommentList(commentList)
                }
            }
        }
    }

    private fun initRvComment() {
        binding.recyclerViewEventComments.apply {
            layoutManager =
                LinearLayoutManager(this@EventDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = eventCommentAdapter
        }
    }

    private fun getUserAttendees(event: EventItem) {

        val list = arrayListOf<User>()

        eventsViewModel.getAttendeesUser(event)
        lifecycleScope.launch {
            eventsViewModel.attendeesUser.collect {
                if (it.isLoading) {
                    binding.progressBarEventAttend.visibility = View.VISIBLE
                    binding.recyclerViewEventAttend.visibility = View.GONE
                }
                it.data?.let { uList ->
                    println("uList"+ uList)
                    binding.progressBarEventAttend.visibility = View.GONE
                    binding.recyclerViewEventAttend.visibility = View.VISIBLE
                    list.add(uList)
                    attendeesEventAdapter.setUserList(list)
                    userList = list
                }
            }
        }
    }

    private fun initRv() {
        binding.recyclerViewEventAttend.apply {
            layoutManager =
                LinearLayoutManager(this@EventDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = attendeesEventAdapter
        }
    }

    private fun setData(event: EventItem) {
        Glide.with(this).load(event.eventImage).into(binding.imageViewEvent)
        binding.textViewEventName.text = event.eventName
        binding.textViewAttentedCount.text = event.eventAttented.toString()
        binding.textViewEventLocation.text = event.eventLocation
        if (event.eventDetail.isNullOrEmpty()) binding.textVewDetail.text = event.eventDescription
        else binding.textVewDetail.text = event.eventDetail

    }
}