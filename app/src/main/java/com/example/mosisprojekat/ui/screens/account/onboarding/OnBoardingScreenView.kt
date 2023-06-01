package com.example.mosisprojekat.ui.screens.account.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.ui.navigation.Routes
import com.example.mosisprojekat.ui.screens.account.onboarding.OnBoardingScreenViewModel.Events

@Composable
fun OnBoardingScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<OnBoardingScreenViewModel>()

    EventsHandler(navController, viewModel)
    OnBoardingScreenView(viewModel)
}

@Composable
private fun OnBoardingScreenView(
    viewModel: OnBoardingScreenViewModel
) {
    Box(Modifier.fillMaxSize()) {
        Text(text = "OnBoarding", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: OnBoardingScreenViewModel
) {
    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.NavigateToLogIn -> {
                navController.navigate(Routes.LOG_IN_SCREEN)
            }
            Events.NavigateToSignUp -> {
                navController.navigate(Routes.SIGN_UP_SCREEN)
            }
            else -> {}
        }
    }
}