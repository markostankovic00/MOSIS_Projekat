package com.example.mosisprojekat.ui.screens.main.rankings

import androidx.compose.runtime.mutableStateOf
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
class RankingsScreenViewModel @Inject constructor(
    private val authRepository: AuthRepositoryInteractor,
    private val userDataRepository: UsersDataRepositoryInteractor
): ViewModel() {

    val events = MutableSharedFlow<Events?>(replay = 0)

    val currentUserID = mutableStateOf("")

    private val _listOfUsers = MutableStateFlow<List<UserData>>(emptyList())
    var listOfUsers = _listOfUsers.asStateFlow()

    init {
        try {

            currentUserID.value = authRepository.getUserId()
            loadUsersData()

        } catch (e: Exception) {
            makeGenericErrorToast()
            e.printStackTrace()
        }
    }

    private fun loadUsersData() = viewModelScope.launch {
        userDataRepository.getAllUsersData().collect {
            _listOfUsers.value = it.data ?: emptyList()
        }
    }

    private fun makeGenericErrorToast() = viewModelScope.launch {
        events.emit(Events.MakeGenericErrorToast)
    }

    fun clearEventChannel() = viewModelScope.launch {
        events.emit(null)
    }

    sealed class Events {
        object MakeGenericErrorToast: Events()
    }
}