package com.mtaner.android_ticmeet.data.repository

import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mtaner.android_ticmeet.data.Resource
import com.mtaner.android_ticmeet.data.model.Messages
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.data.model.UsersMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        if (firebaseAuth.currentUser != null) {
            loggedOutLiveData.postValue(false)
        }
    }

    fun register(
        email: String?,
        password: String?,
        user: User?,
        saveInfo: Boolean = false
    ): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading())
        try {
            if (saveInfo) {
                if (user != null) {
                    firebaseFirestore.collection("User")
                        .add(user).await()
                }
            } else {
                if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                    val result =
                        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                    emit(result.user?.let {
                        Resource.Success(data = it)
                    }!!)
                }
            }
            loggedOutLiveData.postValue(false)
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

    fun update(): Flow<Resource<DocumentReference>> = flow {
        emit(Resource.Loading())
        try {
            val task =
                firebaseFirestore.collection("User")
                    .whereEqualTo("userEmail", firebaseAuth.currentUser!!.email)
                    .get()
            val documentId = task.await().documents[0].id
            val result = firebaseFirestore.collection("User").document(documentId)

            emit(Resource.Success(result))

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


    fun login(email: String, password: String): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading())
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(result.user?.let {
                Resource.Success(data = it)
            }!!)
            loggedOutLiveData.postValue(false)
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

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

    suspend fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    fun getLoggedUser(): Flow<Resource<FirebaseUser>> = flow {

        emit(Resource.Loading())

        if (firebaseAuth.currentUser != null) {
            loggedOutLiveData.postValue(false)
            emit(Resource.Success(data = firebaseAuth.currentUser!!))
        } else {
            emit(Resource.Error("hata"))
        }

    }

    fun getUserFollowing(userEmail: String): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading())

        try {
            val snapshot = firebaseFirestore.collection("User")
                .whereEqualTo("userEmail", userEmail)
                .get().await()
            if (snapshot != null) {
                val userFollowing = snapshot.toObjects(User::class.java)[0].userFollowing
                val followingSnapshot = firebaseFirestore.collection("User")
                    .get().await()

                userFollowing?.forEach { userF ->
                    val user = followingSnapshot.toObjects(User::class.java).filter {
                        it.userEmail == userF
                    }
                    emit(Resource.Success(data = user))
                }

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


    fun getUserFollowers(userEmail: String): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading())

        try {
            val snapshot = firebaseFirestore.collection("User")
                .whereEqualTo("userEmail", userEmail)
                .get().await()
            if (snapshot != null) {
                val userFollowers = snapshot.toObjects(User::class.java)[0].userFollowers
                val followersSnapshot = firebaseFirestore.collection("User")
                    .get().await()

                userFollowers?.forEach { userF ->
                    val user = followersSnapshot.toObjects(User::class.java).filter {
                        it.userEmail == userF
                    }
                    emit(Resource.Success(data = user))
                }

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

    fun getMessagesUser(): Flow<Resource<UsersMessage>> = flow {
        emit(Resource.Loading())
        try {
            val messageSnapshot = firebaseFirestore.collection("Message")
                .whereArrayContains("usersEmail", firebaseAuth.currentUser!!.email!!).get().await()
            messageSnapshot.documents.forEach {
                val usersEmail = it.data?.get("usersEmail") as List<String>
                val emailArray = usersEmail.find { it != firebaseAuth.currentUser!!.email }


                val userSnapshot =
                    firebaseFirestore.collection("User").whereEqualTo("userEmail", emailArray)
                        .get().await()
                val user = userSnapshot.toObjects(User::class.java)[0]
                val lastMessage = it.toObject(Messages::class.java)?.messages?.last()

                val usersMessage = UsersMessage(user, lastMessage!!)

                emit(Resource.Success(usersMessage))

            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: "Bilinmeyen hata"))
        }
    }

    fun getUser(userEmail: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        if (firebaseAuth.currentUser != null) {
            try {
                val snapshot = firebaseFirestore.collection("User")
                    .whereEqualTo("userEmail", userEmail)
                    .get().await()
                if (snapshot != null) {
                    val user = snapshot.toObjects(User::class.java)[0]
                    emit(Resource.Success(data = user!!))
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

    }

    fun followUser(
        user: User,
        firebaseFirestore: FirebaseFirestore,
        auth: FirebaseAuth,
        buttonFollow: AppCompatButton
    ) {
        val myUserTask =
            firebaseFirestore.collection("User").whereEqualTo("userEmail", auth.currentUser!!.email)
                .get()

        val yourUserTask =
            firebaseFirestore.collection("User").whereEqualTo("userEmail", user.userEmail)
                .get()

        val userMapFollowing = hashMapOf<String, Any>()
        val userMapFollower = hashMapOf<String, Any>()

        val userFollowingArray = arrayListOf<String>()
        val userFollowerArray = arrayListOf<String>()


        yourUserTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val u = yourUserTask.result.toObjects(User::class.java)[0]
                val userFollower = u.userFollowers

                if (userFollower!!.isNotEmpty()) {
                    userFollower.forEach {
                        userFollowerArray.add(it)
                    }
                }

                userFollowerArray.add(auth.currentUser!!.email!!)

                userMapFollower["userFollowers"] = userFollowerArray

                firebaseFirestore.collection("User").document(it.result.documents[0].id)
                    .update(userMapFollower)


            }
        }

        myUserTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val u = myUserTask.result.toObjects(User::class.java)[0]

                val userFollowing = u.userFollowing


                if (userFollowing!!.isNotEmpty()) {
                    userFollowing.forEach {
                        userFollowingArray.add(it)
                    }
                }

                userFollowingArray.add(user.userEmail!!)

                userMapFollowing["userFollowing"] = userFollowingArray

                firebaseFirestore.collection("User").document(it.result.documents[0].id)
                    .update(userMapFollowing)

                buttonFollow.text = "Takiptesin"

            }
        }


    }

    fun unfollowUser(
        user: User,
        firebaseFirestore: FirebaseFirestore,
        auth: FirebaseAuth,
        buttonFollow: AppCompatButton
    ) {

        val myUserTask =
            firebaseFirestore.collection("User").whereEqualTo("userEmail", auth.currentUser!!.email)
                .get()

        val yourUserTask =
            firebaseFirestore.collection("User").whereEqualTo("userEmail", user.userEmail)
                .get()

        val userMapFollowing = hashMapOf<String, Any>()
        val userMapFollower = hashMapOf<String, Any>()

        val userFollowingArray = arrayListOf<String>()
        val userFollowerArray = arrayListOf<String>()


        yourUserTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val u = yourUserTask.result.toObjects(User::class.java)[0]
                val userFollower = u.userFollowers

                if (userFollower!!.isNotEmpty()) {
                    userFollower.forEach {
                        userFollowerArray.add(it)
                    }
                }

                userFollowerArray.remove(auth.currentUser!!.email!!)

                userMapFollower["userFollowers"] = userFollowerArray

                println("userFollowingArray" + userFollowerArray)


                firebaseFirestore.collection("User").document(it.result.documents[0].id)
                    .update(userMapFollower)


            }
        }

        myUserTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val u = myUserTask.result.toObjects(User::class.java)[0]

                val userFollowing = u.userFollowing


                if (userFollowing!!.isNotEmpty()) {
                    userFollowing.forEach {
                        userFollowingArray.add(it)
                    }
                }

                userFollowingArray.remove(user.userEmail!!)
                println("userFollowingArray" + userFollowingArray)

                userMapFollowing["userFollowing"] = userFollowingArray
                buttonFollow.text = "Takip et"


                firebaseFirestore.collection("User").document(it.result.documents[0].id)
                    .update(userMapFollowing)


            }
        }


    }

    fun checkFollow(
        user: User,
        auth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        buttonFollow: AppCompatButton
    ) {

        val myUserTask =
            firebaseFirestore.collection("User").whereEqualTo("userEmail", auth.currentUser!!.email)
                .get()

        myUserTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val u = myUserTask.result.toObjects(User::class.java)[0]

                val userFollowing = u.userFollowing

                val isFollow = userFollowing?.find {
                    it == user.userEmail
                }.isNullOrEmpty()

                if (isFollow) {
                    buttonFollow.text = "Takip et"
                } else {
                    buttonFollow.text = "Takiptesin"
                }


            }
        }

    }


}