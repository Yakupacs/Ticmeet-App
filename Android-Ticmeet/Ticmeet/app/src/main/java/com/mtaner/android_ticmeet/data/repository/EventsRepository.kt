package com.mtaner.android_ticmeet.data.repository

import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.mtaner.android_ticmeet.R
import com.mtaner.android_ticmeet.data.Resource
import com.mtaner.android_ticmeet.data.model.Comment
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.ui.adapter.EventItemAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class EventsRepository @Inject constructor() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getAttendeesUser(event: EventItem): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {

            val snapshot = firebaseFirestore.collection("User").get().await()
            val user = snapshot.toObjects(User::class.java)
            event.eventUsersEmail?.forEach { email ->
                val filterUser = user.find { it.userEmail == email }

                emit(Resource.Success(filterUser!!))

            }


        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Bilinmeyen hata"))
        }
    }

    fun getUserComment(event: EventItem): Flow<Resource<List<Comment>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firebaseFirestore.collection("Comment").get().await()
            val comment = snapshot.toObjects(Comment::class.java)

            val filterComment = comment.filter {
                it.commentEventID == event.eventID
            }
            emit(Resource.Success(filterComment))

        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Bilinmeyen hata"))
        }
    }

    fun getEvent(): Flow<Resource<List<EventItem>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firebaseFirestore.collection("Event").get().await()
            val event = snapshot.toObjects(EventItem::class.java)
            emit(Resource.Success(data = event))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Bilinmeyen hata"))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "İnternet bağlantınızı kontrol edin"
                )
            )
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }
    }

    fun getEventEventId(eventID: String): Flow<Resource<EventItem>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firebaseFirestore.collection("Event").whereEqualTo("eventID",eventID).get().await()
            val event = snapshot.toObjects(EventItem::class.java)[0]
            emit(Resource.Success(data = event))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Bilinmeyen hata"))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "İnternet bağlantınızı kontrol edin"
                )
            )
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }
    }

    fun getUserEvent(userEmail:String): Flow<Resource<List<EventItem>>> = flow {
        emit(Resource.Loading())
        try {
            val userSnapshot = firebaseFirestore.collection("User").whereEqualTo("userEmail", userEmail)
                .get().await()
            val userEvents = userSnapshot.toObjects(User::class.java)[0].userEventsID

            val snapshot = firebaseFirestore.collection("Event").get().await()
            val event = snapshot.toObjects(EventItem::class.java)

            userEvents?.forEach { e ->
                emit(Resource.Success(data = event.filter { it.eventID ==  e}))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Bilinmeyen hata"))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "İnternet bağlantınızı kontrol edin"
                )
            )
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }
    }

    fun sendComment() : Flow<Resource<CollectionReference>> = flow {
        emit(Resource.Loading())
        try {
            val add = firebaseFirestore.collection("Comment")

            emit(Resource.Success(add))

        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Bilinmeyen hata"))
        }
    }



    fun joinEvent(
        event: EventItem
    ) {
        var emailArray = arrayListOf<String>()
        var docId = ""
        var eventId = ""
        var eventAttendedCount = 0

        val task =
            firebaseFirestore.collection("User").whereEqualTo("userEmail", firebaseAuth.currentUser!!.email)
                .get()

        firebaseFirestore.collection("Event").whereEqualTo("eventID",event.eventID).get().addOnCompleteListener { snapshot->

            snapshot.result.documents.forEach { document ->
                val userEmail = document.data?.get("eventUsersEmail")
                val eventAttented  = document.data?.get("eventAttented")
                val eventID = document.data?.get("eventID")

                emailArray = userEmail as ArrayList<String>
                docId = document.id
                eventAttendedCount = eventAttented.toString().toInt()
                eventId = eventID.toString()
            }

            if (emailArray.find { it == firebaseAuth.currentUser!!.email.toString() }.isNullOrEmpty()) {
                emailArray.add(firebaseAuth.currentUser!!.email.toString())
            }

            val map = hashMapOf<String,Any>()
            map["eventUsersEmail"] = emailArray
            map["eventAttented"] = eventAttendedCount + 1


            firebaseFirestore.collection("Event").document(docId)
                .update(map)



            val userEventArray = arrayListOf<String>()
            userEventArray.add(eventId)
            val userMap = hashMapOf<String,Any>()
            userMap["userEventsID"] = userEventArray

            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    val userEventsId = it.result.toObjects(User::class.java)[0].userEventsID

                    if (userEventsId!!.isNotEmpty()) {
                        userEventsId.forEach {
                            userEventArray.add(it)
                        }
                    }

                    firebaseFirestore.collection("User").document(it.result.documents[0].id)
                        .update(userMap)
                }
            }

        }



    }

    fun removeJoinEvent(
        event: EventItem
    ) {
        var docId = ""
        var eventId = ""
        var emailArray = arrayListOf<String>()
        var eventAttendedCount = 0

        firebaseFirestore.collection("Event").whereEqualTo("eventID",event.eventID).get().addOnCompleteListener { snapshot ->

            snapshot.result.documents.forEach { document ->
                val userEmail = document.data?.get("eventUsersEmail")
                val eventAttented  = document.data?.get("eventAttented")
                val eventID = document.data?.get("eventID")

                emailArray = userEmail as ArrayList<String>
                docId = document.id
                eventAttendedCount = eventAttented.toString().toInt()
                eventId = eventID.toString()
            }

            if (!emailArray.find { it == firebaseAuth.currentUser!!.email.toString() }.isNullOrEmpty()) {
                emailArray.remove(firebaseAuth.currentUser!!.email.toString())
            }


            val map = hashMapOf<String,Any>()
            map["eventUsersEmail"] = emailArray
            map["eventAttented"] = eventAttendedCount - 1


            firebaseFirestore.collection("Event").document(docId)
                .update(map)


            val task =
                firebaseFirestore.collection("User").whereEqualTo("userEmail", firebaseAuth.currentUser!!.email)
                    .get()


            val userEventArray = arrayListOf<String>()
            val userMap = hashMapOf<String,Any>()
            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    val userEventsId = it.result.toObjects(User::class.java)[0].userEventsID

                    if (userEventsId!!.isNotEmpty()) {
                        userEventsId.forEach {
                            userEventArray.add(it)
                        }
                        userEventArray.remove(eventId)
                        userMap["userEventsID"] = userEventArray
                    }

                    firebaseFirestore.collection("User").document(it.result.documents[0].id)
                        .update(userMap)
                }
            }



        }


    }


}