package com.example.mosisprojekat.ui.screens.account.splash

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.ui.activities.MainActivity
import com.example.mosisprojekat.ui.navigation.Routes
import com.example.mosisprojekat.ui.screens.account.splash.SplashScreenViewModel.Events
import com.example.mosisprojekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.example.mosisprojekat.ui.uiutil.composables.LogoImage
import com.example.mosisprojekat.util.findActivity

@ExperimentalAnimationApi
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

        viewModel.onEndOfAnimation()
    }

    BoxWithBackgroundPattern(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                viewModel.onEndOfAnimation()
            }
    ) {

        LogoImage(
            modifier = Modifier
                .alpha(logoTransparency.value)
                .align(Alignment.Center)
        )
    }
}



@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: SplashScreenViewModel
) {
    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.NavigateToOnBoarding -> {
                navController.popBackStack()
                navController.navigate(Routes.ON_BOARDING_SCREEN)
            }

            Events.NavigateToHome -> {
                context.startActivity(Intent(context, MainActivity::class.java))
                context.findActivity()?.finish()
            }

            else -> {}
        }
    }
}