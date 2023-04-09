package com.mtaner.android_ticmeet.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    val message: String? = "",
    val messageSeen: Boolean = false,
    val messageUserEmail: String? = "",
    @ServerTimestamp
    val timestamp: Date = Date(),
) {
}