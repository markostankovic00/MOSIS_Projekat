package com.example.mosisprojekat.ui.screens.account.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.ui.navigation.Routes
import com.example.mosisprojekat.ui.screens.account.signup.SignUpScreenViewModel.Events

@Composable
fun SignUpScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<SignUpScreenViewModel>()

    EventsHandler(navController, viewModel)
    LogInScreenView(viewModel)
}

@Composable
private fun LogInScreenView(
    viewModel: SignUpScreenViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "SignUpScreen")
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: SignUpScreenViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.NavigateToHomeScreen -> {
                navController.navigate(Routes.HOME_SCREEN)
            }
            Events.NavigateToLogIn -> {
                navController.navigate(Routes.LOG_IN_SCREEN)
            }
            else -> {}
        }
    }

}