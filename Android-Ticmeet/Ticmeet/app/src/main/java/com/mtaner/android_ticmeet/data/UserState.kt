package com.mtaner.android_ticmeet.data

import com.google.firebase.auth.FirebaseUser
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.data.model.UsersMessage

data class UserState(
    val data: User? = null,
    val error: String = "",
    val isLoading: Boolean = false
)

data class UserStateList(
    val data: List<User>? = null,
    val error: String = "",
    val isLoading: Boolean = false
)

data class UserMessageStateList(
    val data: UsersMessage? = null,
    val error: String = "",
    val isLoading: Boolean = false
)