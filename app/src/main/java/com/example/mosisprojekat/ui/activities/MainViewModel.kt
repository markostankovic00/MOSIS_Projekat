package com.example.mosisprojekat.ui.activities

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val bottomNavBarVisibilityState = (mutableStateOf(false))

    fun setBottomNavBarVisibilityState(currentRoute: String?) = viewModelScope.launch {
        bottomNavBarVisibilityState.value = currentRoute in listOf(
            Routes.HOME_SCREEN,
            Routes.PROFILE_SCREEN,
            Routes.RANKINGS_SCREEN
        )
    }

    fun onBottomNavItemClicked(itemRoute: String) = viewModelScope.launch {
        when (itemRoute) {
            Routes.HOME_SCREEN -> events.emit(Events.NavigateToHome)
            Routes.PROFILE_SCREEN -> events.emit(Events.NavigateToProfile)
            Routes.RANKINGS_SCREEN -> events.emit(Events.NavigateToRankings)
        }
    }

    sealed class Events {

        object NavigateToHome: Events()
        object NavigateToProfile: Events()
        object NavigateToRankings: Events()
    }
}