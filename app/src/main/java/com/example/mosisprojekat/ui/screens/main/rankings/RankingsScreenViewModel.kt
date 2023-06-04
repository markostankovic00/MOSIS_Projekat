package com.example.mosisprojekat.ui.screens.main.rankings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mosisprojekat.models.UserData
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RankingsScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val currentUserID = mutableStateOf("")

    private val _listOfUsers = MutableStateFlow<List<UserData>>(emptyList())
    var listOfUsers = _listOfUsers.asStateFlow()

    init {
        try {
            currentUserID.value = authRepository.getUserId()
            Timber.i("TAGA: ${currentUserID.value}")
            //TODO DATA SHOULD BE FETCHED FROM FIREBASE
            _listOfUsers.value = getHardcodedListOfUsers()
        } catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    private fun getHardcodedListOfUsers() = listOf(
        UserData(
            authID = "",
            name = "Name1",
            surname = "Surname1",
            email = "Email1",
            points = 1000
        ),
        UserData(
            authID = "",
            name = "Name2",
            surname = "Surname2",
            email = "Email2",
            points = 100
        ),
        UserData(
            authID = "",
            name = "Name3",
            surname = "Surname3",
            email = "Email3",
            points = 10
        ),
        UserData(
            authID = "",
            name = "Name4",
            surname = "Surname4",
            email = "Email4",
            points = 120
        ),
        UserData(
            authID = "",
            name = "Name5",
            surname = "Surname5",
            email = "Email5",
            points = 200
        ),
        UserData(
            authID = "",
            name = "Name6",
            surname = "Surname6",
            email = "Email6",
            points = 350
        ),
        UserData(
            authID = "",
            name = "Name7",
            surname = "Surname7",
            email = "Email7",
            points = 10
        ),
        UserData(
            authID = "HMBIQCe4HcRx0ehlfGo0SRABysO2",
            name = "Name8",
            surname = "Surname8",
            email = "Email8",
            points = 700
        ),
        UserData(
            authID = "",
            name = "Name9",
            surname = "Surname9",
            email = "Email9",
            points = 800
        ),
        UserData(
            authID = "",
            name = "Name10",
            surname = "Surname10",
            email = "Email10",
            points = 700
        )
    )

    sealed class Events {
        object MakeGenericErrorToast: Events()
    }
}