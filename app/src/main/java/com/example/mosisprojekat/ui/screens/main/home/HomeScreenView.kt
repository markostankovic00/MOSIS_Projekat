package com.example.mosisprojekat.ui.screens.main.home

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.ui.activities.AccountActivity
import com.example.mosisprojekat.ui.screens.main.home.HomeScreenViewModel.Events
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryButton
import com.example.mosisprojekat.util.findActivity

@ExperimentalAnimationApi
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
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        PrimaryButton(
            text = "Log Out",
            onClick = viewModel::onLogOut
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: HomeScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.NavigateToSplashScreen -> {
                context.startActivity(Intent(context, AccountActivity::class.java))
                context.findActivity()?.finish()
            }
            else -> {}
        }
    }

}