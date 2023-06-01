package com.example.mosisprojekat.ui.screens.account.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInScreenViewModel @Inject constructor(

): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val usernameTextState = mutableStateOf("")

    val passwordTextState = mutableStateOf("")

    fun onUsernameTextChanged(username: String) {
        usernameTextState.value = username
    }

    fun onPasswordTextChanged(password: String) {
        passwordTextState.value = password
    }

    fun onLogInClick() = viewModelScope.launch {

        if(usernameTextState.value == "admin" && passwordTextState.value == "admin")
            navigateToHomeScreen()
        else
            events.emit(Events.MakeToast)

        //TODO HANDLE FIREBASE INTEGRATION


    }

    private fun navigateToHomeScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToHomeScreen)
    }

    fun navigateToSignUp() = viewModelScope.launch {
        events.emit(Events.NavigateToSignUp)
    }

    sealed class Events {
        object NavigateToHomeScreen: Events()
        object NavigateToSignUp: Events()
        object MakeToast: Events()
    }
}