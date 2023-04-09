package com.mtaner.android_ticmeet.data.model

data class Messages(
    val messages: List<Message>? = emptyList(),
    val usersEmail: List<String>? = emptyList()
) {
}