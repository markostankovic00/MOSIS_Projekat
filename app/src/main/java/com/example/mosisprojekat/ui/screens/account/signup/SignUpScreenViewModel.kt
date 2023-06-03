package com.example.mosisprojekat.ui.screens.account.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val emailTextState = mutableStateOf("")

    val passwordTextState = mutableStateOf("")

    val repeatPasswordTextState = mutableStateOf("")

    fun onEmailTextChanged(email: String) {
        emailTextState.value = email
    }

    fun onPasswordTextChanged(password: String) {
        passwordTextState.value = password
    }

    fun onRepeatPasswordTextChanged(repeatPassword: String) {
        repeatPasswordTextState.value = repeatPassword
    }

    fun onSignUpClick() = viewModelScope.launch {

        navigateToHomeScreen()

        authRepository.signUpUser(
            email = emailTextState.value,
            password = passwordTextState.value
        ) { isSuccessful ->
            if (isSuccessful)
                navigateToHomeScreen()
            else
                makeSignUpErrorToast()
        }
    }

    private fun makeSignUpErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeSignupErrorToast)
    }

    private fun navigateToHomeScreen() = viewModelScope.launch {
        events.emit(Events.NavigateToHomeScreen)
    }

    fun navigateToLogIn() = viewModelScope.launch {
        events.emit(Events.NavigateToLogIn)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    sealed class Events {
        object NavigateToHomeScreen: Events()
        object NavigateToLogIn: Events()
        object MakeSignupErrorToast: Events()
    }
}