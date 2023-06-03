package com.example.mosisprojekat.ui.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    fun onLogOut() = viewModelScope.launch {
        authRepository.logOutUser()
        events.emit(Events.NavigateToSplashScreen)
    }

    sealed class Events {
        object NavigateToSplashScreen: Events()
    }

}