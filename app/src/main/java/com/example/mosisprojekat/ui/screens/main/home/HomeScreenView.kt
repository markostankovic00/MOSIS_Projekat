package com.example.mosisprojekat.ui.screens.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.ui.screens.main.home.HomeScreenViewModel.Events

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()

    EventsHandler(navController, viewModel)
    HomeScreenView(viewModel)
}

@Composable
private fun HomeScreenView(
    viewModel: HomeScreenViewModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "HomeScreen")
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: HomeScreenViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            else -> {}
        }
    }

}