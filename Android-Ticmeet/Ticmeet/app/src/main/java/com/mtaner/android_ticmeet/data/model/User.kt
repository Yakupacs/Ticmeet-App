package com.mtaner.android_ticmeet.data.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class User(
    var userUsername: String? = "",
    var userEmail: String? = "",
    @ServerTimestamp
    val userRegisterDate: Date = Date(),
    var userName: String? = "",
    var userLocation: String? = "",
    var userImage: String? = "",
    var userGender: String? = "",
    var userBio: String? = "",
    var userAge: Int? = 0,
    var userFollowers: List<String>? = emptyList(),
    var userFollowing: List<String>? = emptyList(),
    var userEventsID: List<String>? = emptyList(),
    val userTopImage: String? = ""
): Parcelable {

}