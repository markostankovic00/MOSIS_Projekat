package com.example.mosisprojekat.ui.navigation

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.mosisprojekat.ui.activities.MainViewModel
import com.example.mosisprojekat.ui.screens.main.home.HomeScreen
import com.example.mosisprojekat.ui.activities.MainViewModel.Events
import com.example.mosisprojekat.ui.uiutil.composables.bottomnav.BottomNavItem
import com.example.mosisprojekat.R
import com.example.mosisprojekat.ui.screens.main.addgym.AddGymScreen
import com.example.mosisprojekat.ui.screens.main.addreview.AddReviewScreen
import com.example.mosisprojekat.ui.screens.main.gymdetails.GymDetailsScreen
import com.example.mosisprojekat.ui.screens.main.profile.ProfileScreen
import com.example.mosisprojekat.ui.screens.main.rankings.RankingsScreen
import com.example.mosisprojekat.ui.screens.main.seereviews.SeeReviewsScreen
import com.example.mosisprojekat.ui.uiutil.composables.bottomnav.BottomNavBar
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalAnimationApi
@Composable
fun MainActivityLayoutAndNavigation(
    viewModel: MainViewModel
) {

    val navController = rememberAnimatedNavController()

    NavHostAndBottomNavigation(navController, viewModel)
    EventsHandler(navController, viewModel)
}

@FlowPreview
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
        AnimatedNavigation(viewModel, navController)
    }

}

@FlowPreview
@ExperimentalAnimationApi
@Composable
private fun AnimatedNavigation(
    mainViewModel: MainViewModel,
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
            HomeScreen(mainViewModel, navController)
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

        composable(
            route = Routes.GYM_DETAILS_SCREEN  + "/{selectedGymId}",
            arguments = listOf(
                navArgument("selectedGymId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            GymDetailsScreen(
                navController,
                it.arguments?.getString("selectedGymId") ?: ""
            )
        }

        composable(
            route = Routes.ADD_GYM_SCREEN  + "/{lat}/{lng}",
            arguments = listOf(
                navArgument("lat") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("lng") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            AddGymScreen(
                navController = navController,
                lat = (it.arguments?.getString("lat") ?: "").toDouble(),
                lng = (it.arguments?.getString("lng") ?: "").toDouble()
            )
        }

        composable(
            route = Routes.ADD_REVIEW_SCREEN  + "/{selectedGymId}",
            arguments = listOf(
                navArgument("selectedGymId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            AddReviewScreen(
                navController,
                it.arguments?.getString("selectedGymId") ?: ""
            )
        }

        composable(
            route = Routes.SEE_REVIEWS_SCREEN  + "/{selectedGymId}",
            arguments = listOf(
                navArgument("selectedGymId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            SeeReviewsScreen(
                navController,
                it.arguments?.getString("selectedGymId") ?: ""
            )
        }
    }
}

@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: MainViewModel
) {

    val context = LocalContext.current

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
            Events.MakeLocationErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_location), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            else -> {}
        }
    }
}
