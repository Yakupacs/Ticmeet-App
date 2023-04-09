package com.mtaner.android_ticmeet.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.mtaner.android_ticmeet.R
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.data.model.UsersMessage
import com.mtaner.android_ticmeet.databinding.FragmentChatBinding
import com.mtaner.android_ticmeet.databinding.FragmentEventsBinding
import com.mtaner.android_ticmeet.databinding.FragmentLoginBinding
import com.mtaner.android_ticmeet.ui.adapter.MyMessagesAdapter
import com.mtaner.android_ticmeet.ui.adapter.SendMessageUserAdapter
import com.mtaner.android_ticmeet.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    @Inject
    lateinit var myMessagesAdapter: MyMessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()



        binding.imageViewSendMessage.setOnClickListener {
            val bottomSheetDialog = SendMessageSelectUserFragment(emptyList())
            bottomSheetDialog.show(parentFragmentManager, "chat")
        }


        initRv()
        getUserData()




        return binding.root
    }

    private fun getUserData() {
        authViewModel.getMessageUser()

        val userList = arrayListOf<UsersMessage>()
        lifecycleScope.launch {
            authViewModel.userMessageList.collect {
                if (it.isLoading) {
                    binding.recyclerViewUserMessageList.visibility = View.GONE
                    binding.progressBarLoading.visibility = View.VISIBLE

                }
                it.data.let {
                    binding.recyclerViewUserMessageList.visibility = View.VISIBLE
                    binding.progressBarLoading.visibility = View.GONE
                    if (it != null) {
                        userList.add(it)
                    }

                    myMessagesAdapter.setUserMessagesList(userList.reversed())

                }
            }
        }
    }

    private fun initRv() {
        binding.recyclerViewUserMessageList.apply {
            layoutManager = LinearLayoutManager(requireContext(),)
            setHasFixedSize(true)
            adapter = myMessagesAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        getUserData()
    }


}