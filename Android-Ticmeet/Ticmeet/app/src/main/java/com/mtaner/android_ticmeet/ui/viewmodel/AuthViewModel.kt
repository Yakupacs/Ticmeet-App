package com.mtaner.android_ticmeet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mtaner.android_ticmeet.data.*
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _user = MutableStateFlow(AuthState())
    val user : StateFlow<AuthState> = _user

    private val _userData = MutableStateFlow(UserState())
    val userData: StateFlow<UserState> = _userData

    private val _userFollow = MutableStateFlow(UserStateList())
    val userFollow: StateFlow<UserStateList> = _userFollow

    private val _userFollowers = MutableStateFlow(UserStateList())
    val userFollowers: StateFlow<UserStateList> = _userFollowers

    private val _update = MutableStateFlow(AuthState())
    val update: StateFlow<AuthState> = _update

    private val _userMessageList = MutableStateFlow(UserMessageStateList())
    val userMessageList: StateFlow<UserMessageStateList> = _userMessageList

    fun login(email: String, password: String) {
        authRepository.login(email,password).onEach {
            when(it) {
                is Resource.Error -> _user.value = AuthState(error = it.message ?: "")
                is Resource.Loading -> _user.value = AuthState(isLoading = true)
                is Resource.Success -> _user.value = AuthState(data = it.data)
            }
        }.launchIn(viewModelScope)
    }

    fun register(email: String?,password: String?,user: User? = User(),saveInfo: Boolean) {
        authRepository.register(email, password, user,saveInfo).onEach {
            when(it) {
                is Resource.Error -> _user.value = AuthState(error = it.message ?: "")
                is Resource.Loading -> _user.value = AuthState(isLoading = true)
                is Resource.Success -> _user.value = AuthState(data = it.data)
            }
        }.launchIn(viewModelScope)
    }

    fun getMessageUser() {
        authRepository.getMessagesUser().onEach {
            when(it) {
                is Resource.Error -> _userMessageList.value = UserMessageStateList(error = it.message ?: "")
                is Resource.Loading -> _userMessageList.value = UserMessageStateList(isLoading = true)
                is Resource.Success -> {
                    _userMessageList.value = UserMessageStateList(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUser(userEmail: String) {
        authRepository.getUser(userEmail).onEach {
            when(it) {
                is Resource.Error -> _userData.value = UserState(error = it.message ?: "")
                is Resource.Loading -> _userData.value = UserState(isLoading = true)
                is Resource.Success -> {
                    _userData.value = UserState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUserFollowing(userEmail: String) {
        authRepository.getUserFollowing(userEmail).onEach {
            when(it) {
                is Resource.Error -> _userFollow.value = UserStateList(error = it.message ?: "")
                is Resource.Loading -> _userFollow.value = UserStateList(isLoading = true)
                is Resource.Success -> {
                    _userFollow.value = UserStateList(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUserFollowers(userEmail: String) {
        authRepository.getUserFollowers(userEmail).onEach {
            when(it) {
                is Resource.Error -> _userFollowers.value = UserStateList(error = it.message ?: "")
                is Resource.Loading -> _userFollowers.value = UserStateList(isLoading = true)
                is Resource.Success -> {
                    _userFollowers.value = UserStateList(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }


    fun loggedUser() {

        authRepository.getLoggedUser().onEach {
            when (it) {
                is Resource.Loading -> {
                    _user.value = AuthState(isLoading = true)
                }
                is Resource.Error -> {
                    _user.value = AuthState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _user.value = AuthState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun update(userMap: HashMap<String,Any>) {

        authRepository.update().onEach {
            when (it) {
                is Resource.Loading -> {
                    _update.value = AuthState(isLoading = true)
                }
                is Resource.Error -> {
                    _update.value = AuthState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    it.data?.update(userMap)?.addOnSuccessListener {
                        _update.value = AuthState(isLoading = false)
                    }?.await()
                }
            }
        }.launchIn(viewModelScope)

    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.logOut()
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            authRepository.resetPassword(email)
        }
    }


}