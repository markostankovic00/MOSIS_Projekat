package com.example.mosisprojekat.ui.screens.account.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(

): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    fun navigateToOnBoarding() = viewModelScope.launch {
        events.emit(Events.NavigateToOnBoarding)
    }

    sealed class Events {
        object NavigateToOnBoarding: Events()
    }
}