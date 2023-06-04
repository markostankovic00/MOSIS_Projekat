package com.example.mosisprojekat.ui.screens.main.rankings

import androidx.compose.animation.ExperimentalAnimationApi
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
fun RankingsScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<RankingsScreenViewModel>()

    EventsHandler(navController, viewModel)
    RankingsScreenView(viewModel)
}

@Composable
private fun RankingsScreenView(
    viewModel: RankingsScreenViewModel
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Rankings Screen",
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: RankingsScreenViewModel
) {
    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            else -> {}
        }
    }

}