package com.mtaner.android_ticmeet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mtaner.android_ticmeet.data.*
import com.mtaner.android_ticmeet.data.model.Comment
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.data.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository
): ViewModel() {

    private val _event = MutableStateFlow(EventStateList())
    val event : StateFlow<EventStateList> = _event

    private val _eventEventId = MutableStateFlow(EventState())
    val eventEventId : StateFlow<EventState> = _eventEventId

    private val _userEvent = MutableStateFlow(EventStateList())
    val userEvent : StateFlow<EventStateList> = _userEvent

    private val _attendeesUser = MutableStateFlow(UserState())
    val attendeesUser : StateFlow<UserState> = _attendeesUser

    private val _comment = MutableStateFlow(CommentState())
    val comment : StateFlow<CommentState> = _comment

    private val _sendComment = MutableStateFlow(CommentState())
    val sendComment : StateFlow<CommentState> = _sendComment

    fun getEvent() {
        eventsRepository.getEvent().onEach {
            when(it) {
                is Resource.Error -> _event.value = EventStateList(error = it.message ?: "")
                is Resource.Loading -> _event.value = EventStateList(isLoading = true)
                is Resource.Success -> {
                    _event.value = EventStateList(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getEventEventId(eventId: String) {
        eventsRepository.getEventEventId(eventId).onEach {
            when(it) {
                is Resource.Error -> _eventEventId.value = EventState(error = it.message ?: "")
                is Resource.Loading -> _eventEventId.value = EventState(isLoading = true)
                is Resource.Success -> {
                    _eventEventId.value = EventState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUserEvent(userEmail: String) {
        eventsRepository.getUserEvent(userEmail).onEach {
            when(it) {
                is Resource.Error -> _userEvent.value = EventStateList(error = it.message ?: "")
                is Resource.Loading -> _userEvent.value = EventStateList(isLoading = true)
                is Resource.Success -> {
                    _userEvent.value = EventStateList(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getAttendeesUser(event: EventItem) {
        eventsRepository.getAttendeesUser(event = event).onEach {
            when(it) {
                is Resource.Error -> _attendeesUser.value = UserState(error = it.message ?: "")
                is Resource.Loading -> _attendeesUser.value = UserState(isLoading = true)
                is Resource.Success -> {
                    _attendeesUser.value = UserState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUserComment(event: EventItem) {
        eventsRepository.getUserComment(event = event).onEach {
            when(it) {
                is Resource.Error -> _comment.value = CommentState(error = it.message ?: "")
                is Resource.Loading -> _comment.value = CommentState(isLoading = true)
                is Resource.Success -> {
                    _comment.value = CommentState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun sendEventComment(comment: Comment) {
        eventsRepository.sendComment().onEach {
            when(it) {
                is Resource.Error -> _sendComment.value = CommentState(error = it.message ?: "")
                is Resource.Loading -> _sendComment.value = CommentState(isLoading = true)
                is Resource.Success -> {
                    it.data?.add(comment)
                        ?.addOnCompleteListener {
                            _sendComment.value = CommentState(isLoading = false)
                        }?.await()
                }
            }
        }.launchIn(viewModelScope)
    }

}