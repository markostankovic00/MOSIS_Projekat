package com.example.mosisprojekat.ui.screens.main.gymdetails

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@ExperimentalAnimationApi
@Composable
fun GymDetailsScreen(
    navController: NavHostController,
    selectedGymId: String
) {
    val viewModel = hiltViewModel<GymDetailsScreenViewModel>()

    EventsHandler(navController, viewModel)
    GymDetailsScreenView(viewModel, selectedGymId)
}

@Composable
private fun GymDetailsScreenView(
    viewModel: GymDetailsScreenViewModel,
    selectedGymId: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = selectedGymId,
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.primary
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: GymDetailsScreenViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            else -> {}
        }
    }

}