package com.example.mosisprojekat.ui.screens.account.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.ui.navigation.Routes
import com.example.mosisprojekat.ui.screens.account.splash.SplashScreenViewModel.Events
import com.example.mosisprojekat.ui.uiutil.composables.BackgroundPatternBox
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryButton

@Composable
fun SplashScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<SplashScreenViewModel>()

    EventsHandler(navController, viewModel)
    LaunchScreenView(viewModel)
}

@Composable
private fun LaunchScreenView(
    viewModel: SplashScreenViewModel
) {

    val interactionSource = remember { MutableInteractionSource() }
    val logoTransparency = remember { Animatable(0.0f) }

    LaunchedEffect(key1 = true) {
        logoTransparency.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(
                durationMillis = 3000
            )
        )

        //viewModel.navigateToOnBoarding()
    }

    BackgroundPatternBox(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                viewModel.navigateToOnBoarding()
            }
    ) {

        /*Image(
            modifier = Modifier
                .scale(MaterialTheme.componentSizes.launchScreenLogoScale)
                .align(Alignment.Center)
                .alpha(logoTransparency.value),
            contentScale = ContentScale.Fit,
            painter = painterResource(id = R.drawable.nistruct_logo_white),
            contentDescription = "Nistruct Logo"
        )*/
    }
}



@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: SplashScreenViewModel
) {
    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.NavigateToOnBoarding -> {
                navController.popBackStack()
                navController.navigate(Routes.ON_BOARDING_SCREEN)
            }
            else -> {}
        }
    }
}