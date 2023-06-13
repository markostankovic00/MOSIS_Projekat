package com.example.mosisprojekat.ui.screens.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.models.UserData
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import com.example.mosisprojekat.repository.interactors.UsersDataRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor,
    private val userDataRepository: UsersDataRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                if(authRepository.hasUser()) {
                    loadUserData(authRepository.getUserId())
                }
            } catch(e: Exception) {
                makeGenericErrorToast()
                e.printStackTrace()
            }
        }
    }

    private fun loadUserData(userId: String) = viewModelScope.launch {
        userDataRepository.getUserData(
            userId = userId,
            onError = {
                makeGenericErrorToast()
                it?.printStackTrace()
            },
            onSuccess = { userData ->
                _userData.value = userData
            }
        )
    }

    fun onLogOut() = viewModelScope.launch {
        try {
            authRepository.logOutUser()
            events.emit(Events.NavigateToSplashScreen)
        } catch (e: Exception) {
            makeLogOutErrorToast()
            e.printStackTrace()
        }
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    private fun makeLogOutErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeLogOutErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    sealed class Events {
        object NavigateToSplashScreen: Events()
        object MakeLogOutErrorToast: Events()
        object MakeGenericErrorToast: Events()
    }
}