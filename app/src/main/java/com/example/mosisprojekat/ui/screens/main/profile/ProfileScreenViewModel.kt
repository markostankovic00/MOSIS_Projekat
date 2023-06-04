package com.example.mosisprojekat.ui.screens.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    private val _name = MutableStateFlow("")
    var name = _name.asStateFlow()

    private val _surname = MutableStateFlow("")
    var surname = _surname.asStateFlow()

    private val _email = MutableStateFlow("")
    var email = _email.asStateFlow()

    init {
        try {
            //TODO set name and surname with firebase data
            _name.value = "Marko"
            _surname.value = "Stankovic"
            _email.value = authRepository.currentUser?.email ?: ""
        } catch(e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
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