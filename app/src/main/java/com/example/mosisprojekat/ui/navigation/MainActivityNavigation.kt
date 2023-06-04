package com.example.mosisprojekat.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mosisprojekat.ui.activities.MainViewModel
import com.example.mosisprojekat.ui.screens.main.home.HomeScreen
import com.example.mosisprojekat.ui.activities.MainViewModel.Events
import com.example.mosisprojekat.ui.uiutil.composables.bottomnav.BottomNavItem
import com.example.mosisprojekat.R
import com.example.mosisprojekat.ui.screens.main.profile.ProfileScreen
import com.example.mosisprojekat.ui.screens.main.rankings.RankingsScreen
import com.example.mosisprojekat.ui.uiutil.composables.bottomnav.BottomNavBar
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalAnimationApi
@Composable
fun MainActivityLayoutAndNavigation() {

    val navController = rememberAnimatedNavController()
    val viewModel = hiltViewModel<MainViewModel>()

    NavHostAndBottomNavigation(navController, viewModel)
    EventsHandler(navController, viewModel)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
private fun NavHostAndBottomNavigation(
    navController: NavHostController,
    viewModel: MainViewModel
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavBarVisibilityState = rememberSaveable { viewModel.bottomNavBarVisibilityState }

    viewModel.setBottomNavBarVisibilityState(currentRoute)

    val bottomNavBarItems = listOf(
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_bar_rankings),
            route = Routes.RANKINGS_SCREEN,
            icon = Icons.Default.EmojiEvents
        ),
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_bar_home),
            route = Routes.HOME_SCREEN,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_bar_profile),
            route = Routes.PROFILE_SCREEN,
            icon = Icons.Default.Person
        )
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                items = bottomNavBarItems,
                bottomBarState = bottomNavBarVisibilityState.value,
                onItemClick = { viewModel.onBottomNavItemClicked(it.route) }
            )
        }
    ) {
        AnimatedNavigation(navController)
    }

}

@ExperimentalAnimationApi
@Composable
private fun AnimatedNavigation(
    navController: NavHostController
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Routes.HOME_SCREEN,
        enterTransition = {
            fadeIn(
                initialAlpha = 0.1f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        }
    ) {
        composable(
            route = Routes.HOME_SCREEN
        ) {
            HomeScreen(navController)
        }

        composable(
            route = Routes.PROFILE_SCREEN
        ) {
            ProfileScreen(navController)
        }

        composable(
            route = Routes.RANKINGS_SCREEN
        ) {
            RankingsScreen(navController)
        }
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: MainViewModel
) {

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.NavigateToHome -> {
                navController.popBackStack()
                navController.navigate(Routes.HOME_SCREEN)
            }
            Events.NavigateToProfile -> {
                navController.popBackStack()
                navController.navigate(Routes.PROFILE_SCREEN)
            }
            Events.NavigateToRankings -> {
                navController.popBackStack()
                navController.navigate(Routes.RANKINGS_SCREEN)
            }
            else -> {}
        }
    }
}

/*
@ExperimentalAnimationApi
@Composable
fun MainActivityNavigation() {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = Routes.HOME_SCREEN,
        enterTransition = {
            fadeIn(
                initialAlpha = 0.1f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutLinearInEasing
                )
            )
        }
    ) {
        composable(
            route = Routes.HOME_SCREEN
        ) {
            HomeScreen(navController)
        }
    }
}*/
