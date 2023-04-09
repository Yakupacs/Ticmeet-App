package com.mtaner.android_ticmeet.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventItem(
    val eventAttented: Int? = 0,
    val eventCategory: String? = "",
    val eventComments: List<String>? = emptyList(),
    val eventDetail: String? = "",
    val eventDescription: String? = "",
    val eventID: String? = "",
    val eventImage: String? = "",
    val eventLocation: String? = "",
    val eventName: String? = "",
    val eventUsersEmail: List<String>? = emptyList()

) : Parcelable{

}