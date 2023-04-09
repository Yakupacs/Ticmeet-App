package com.mtaner.android_ticmeet.data

import com.mtaner.android_ticmeet.data.model.Comment
import com.mtaner.android_ticmeet.data.model.EventItem

data class EventStateList(
    val data: List<EventItem>? = null,
    val error: String = "",
    val isLoading: Boolean = false
)

data class EventState(
    val data: EventItem? = null,
    val error: String = "",
    val isLoading: Boolean = false
)

data class CommentState(
    val data: List<Comment>? = null,
    val error: String = "",
    val isLoading: Boolean = false
)