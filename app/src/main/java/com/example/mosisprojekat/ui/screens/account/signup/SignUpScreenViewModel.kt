package com.example.mosisprojekat.ui.screens.account.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import com.example.mosisprojekat.repository.interactors.UsersDataRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor,
    private val usersDataRepository: UsersDataRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val nameTextState = mutableStateOf("")

    val surnameTextState = mutableStateOf("")

    val emailTextState = mutableStateOf("")

    val passwordTextState = mutableStateOf("")

    val repeatPasswordTextState = mutableStateOf("")

    fun onNameTextChanged(name: String) {
        nameTextState.value = name
    }

    fun onSurnameTextChanged(surname: String) {
        surnameTextState.value = surname
    }

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

        try {
            authRepository.signUpUser(
                email = emailTextState.value.trim(),
                password = passwordTextState.value.trim()
            ) { isSuccessful ->
                if (isSuccessful)
                    //navigateToHomeScreen()

                    usersDataRepository.addUserData(
                        userId = authRepository.getUserId(),
                        name = nameTextState.value.trim(),
                        surname = surnameTextState.value.trim(),
                        email = emailTextState.value.trim(),
                        points = 0
                    ) { isSuccessfulUserData ->
                        if (isSuccessfulUserData)
                            navigateToHomeScreen()
                        else
                            makeSignUpErrorToast()
                    }

                else
                    makeSignUpErrorToast()
            }
        } catch (e:Exception) {
            makeSignUpErrorToast()
            e.printStackTrace()
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