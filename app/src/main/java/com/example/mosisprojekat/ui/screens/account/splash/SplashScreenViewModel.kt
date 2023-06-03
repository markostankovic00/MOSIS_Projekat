package com.example.mosisprojekat.ui.screens.account.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    fun onEndOfAnimation() = viewModelScope.launch {
        try {
            if(authRepository.hasUser())
                navigateToHome()
            else
                navigateToOnBoarding()
        } catch (e:Exception) {
            navigateToOnBoarding()
            e.printStackTrace()
        }
    }

    private fun navigateToHome() = viewModelScope.launch {
        events.emit(Events.NavigateToHome)
    }

    private fun navigateToOnBoarding() = viewModelScope.launch {
        events.emit(Events.NavigateToOnBoarding)
    }

    sealed class Events {
        object NavigateToOnBoarding: Events()
        object NavigateToHome: Events()
    }
}