package com.mtaner.android_ticmeet.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.mtaner.android_ticmeet.data.model.Message
import com.mtaner.android_ticmeet.data.model.Messages
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.databinding.ActivityMessageBinding
import com.mtaner.android_ticmeet.ui.adapter.MessagesAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    @Inject
    lateinit var messagesAdapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        } else {
            intent.getParcelableExtra("user")
        }

        binding.imageViewBack.setOnClickListener {
            finish()
        }

        initRv()
        initMessages(user!!)

        getUserData(user!!)





        binding.imageViewSend.setOnClickListener {
            val message = binding.editTextMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(user, message)
            }
        }


    }

    private fun initMessages(user: User) {

        firebaseFirestore.collection("Message")
            .where(
                Filter.equalTo(
                    "usersEmail",
                    arrayListOf(user.userEmail, auth.currentUser!!.email)
                )
            )
            .addSnapshotListener { value, error ->

                if (value != null) {
                    if (value.toObjects(Messages::class.java).isNotEmpty()) {
                        val messagesList = value.toObjects(Messages::class.java)[0].messages
                        if (messagesList != null) {
                            messagesAdapter.setMessageList(messagesList)
                            successSendMessage(messagesList)
                        }
                    } else {
                        getMyMessage(user)
                    }

                }

            }


    }

    private fun getMyMessage(user: User) {
        firebaseFirestore.collection("Message")
            .where(
                Filter.equalTo(
                    "usersEmail",
                    arrayListOf(auth.currentUser!!.email, user.userEmail)
                )
            )
            .addSnapshotListener { value, error ->
                if (value != null) {
                    if (value.toObjects(Messages::class.java).isNotEmpty()) {
                        val messagesList = value.toObjects(Messages::class.java)[0].messages
                        if (messagesList != null) {
                            messagesAdapter.setMessageList(messagesList)
                            successSendMessage(messagesList)
                        }
                    }
                }

            }
    }

    private fun initRv() {
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(this@MessageActivity)
            setHasFixedSize(true)
            adapter = messagesAdapter
        }
    }

    private fun sendMessage(user: User, userMessage: String) {
        val message = Message(userMessage, false, auth.currentUser!!.email)
        val messageArray = arrayListOf<Message>()


        firebaseFirestore.collection("Message")
            .where(
                Filter.equalTo(
                    "usersEmail",
                    arrayListOf(user.userEmail, auth.currentUser!!.email)
                )
            )
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.toObjects(Messages::class.java).isNotEmpty()) {
                        val messagesList = it.result.toObjects(Messages::class.java)[0].messages
                        it.result.documents.forEach {
                            val docId = it.id
                            val usersEmail = it.data?.get("usersEmail")
                            val messagesUser = it.data?.get("messages")

                            val array = usersEmail as List<String>
                            val mailArray = arrayListOf<String>()

                            if (array.isNotEmpty()) {
                                mailArray.add(array[0])
                                mailArray.add(array[1])
                            } else {
                                mailArray.add(user.userEmail!!)
                                mailArray.add(auth.currentUser!!.email!!)
                            }

                            messagesList?.forEach {
                                messageArray.add(it)
                            }

                            messageArray.add(message)
                            val messages = Messages(messages = messageArray, mailArray)

                            firebaseFirestore.collection("Message").document(docId).set(messages)
                        }
                    } else {
                        sendMyMessage(user,messageArray,message)
                    }
                }
            }


    }

    private fun sendMyMessage(user: User, messageArray: ArrayList<Message>, message: Message) {
        firebaseFirestore.collection("Message")
            .where(
                Filter.equalTo(
                    "usersEmail",
                    arrayListOf(auth.currentUser!!.email, user.userEmail)
                )
            )
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.toObjects(Messages::class.java).isNotEmpty()) {
                        val messagesList =
                            it.result.toObjects(Messages::class.java)[0].messages

                        it.result.documents.forEach {
                            val docId = it.id
                            val usersEmail = it.data?.get("usersEmail")
                            val messagesUser = it.data?.get("messages")

                            val array = usersEmail as List<String>
                            val mailArray = arrayListOf<String>()

                            if (array.isNotEmpty()) {
                                mailArray.add(array[0])
                                mailArray.add(array[1])
                            } else {
                                mailArray.add(user.userEmail!!)
                                mailArray.add(auth.currentUser!!.email!!)
                            }

                            messagesList?.forEach {
                                messageArray.add(it)
                            }

                            messageArray.add(message)
                            val messages =
                                Messages(messages = messageArray, mailArray)

                            firebaseFirestore.collection("Message").document(docId)
                                .set(messages)
                        }


                    } else {
                        sendMyFirstMessage(user)
                    }


                }

            }
    }

    private fun sendMyFirstMessage(user: User) {

        val message = Message(binding.editTextMessage.text.toString().trim(), false, auth.currentUser!!.email)

        val messageArray = arrayListOf<Message>()

        val mailArray = arrayListOf<String>()
        mailArray.add(user.userEmail!!)
        mailArray.add(auth.currentUser!!.email!!)

        messageArray.add(message)
        val messages = Messages(messages = messageArray, mailArray)

        firebaseFirestore.collection("Message")
            .add(messages)
    }

    private fun successSendMessage(messagesList: List<Message>) {
        binding.recyclerViewMessages.scrollToPosition(messagesList.size - 1)
        binding.editTextMessage.setText("")
    }

    private fun getUserData(user: User) {
        Glide.with(this).load(user.userImage).into(binding.imageViewProfilePhoto)
        binding.textViewUserName.text = user.userName
        binding.textViewUserUserName.text = user.userUsername
    }
}