package com.example.mosisprojekat.ui.screens.main.gymlist

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.R
import com.example.mosisprojekat.ui.navigation.Routes
import com.example.mosisprojekat.ui.theme.RankingRowHighlightColor
import com.example.mosisprojekat.ui.theme.spacing
import com.example.mosisprojekat.ui.uiutil.composables.AutoResizedText
import com.example.mosisprojekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.example.mosisprojekat.ui.screens.main.gymlist.GymListScreenViewModel.Events
import com.example.mosisprojekat.util.ComponentSizes

@ExperimentalAnimationApi
@Composable
fun GymListScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<GymListScreenViewModel>()

    EventsHandler(navController, viewModel)
    GymListScreenView(viewModel)
}

@Composable
private fun GymListScreenView(
    viewModel: GymListScreenViewModel
) {

    val listOfGyms by viewModel.listOfGyms.collectAsState()

    val currentUserID by remember { viewModel.currentUserID }

    BoxWithBackgroundPattern {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.medium
                    ),
                text = stringResource(id = R.string.gym_list_screen_title),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = MaterialTheme.spacing.large,
                        bottom = ComponentSizes.bottomNavBarHeight.dp,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium
                    )
            ) {
                itemsIndexed(listOfGyms) { index, gym ->

                    GymDataRow(
                        index = index + 1,
                        name = gym.name,
                        isCurrentUserCreator = gym.userId == currentUserID,
                        onClick = { viewModel.onGymCLicked(gym.documentId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GymDataRow(
    modifier: Modifier = Modifier,
    index: Int,
    name: String,
    isCurrentUserCreator: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(vertical = MaterialTheme.spacing.medium)
            .height(57.dp)
            .fillMaxWidth()
            .background(
                color = if (isCurrentUserCreator)
                    RankingRowHighlightColor
                else
                    MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium
                ),
            text = index.toString(),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.primary
        )

        AutoResizedText(
            modifier = Modifier
                .padding(end = MaterialTheme.spacing.small),
            text = name,
            style = MaterialTheme.typography.body1.copy(fontStyle = FontStyle.Italic),
            color = MaterialTheme.colors.onSurface
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: GymListScreenViewModel
) {
    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {

            is Events.NavigateToGymDetailsScreen -> {
                val selectedGymId = (event.value as Events.NavigateToGymDetailsScreen).selectedGymId
                navController.navigate(Routes.GYM_DETAILS_SCREEN + "/" + selectedGymId)
            }

            Events.MakeGenericErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_generic), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            else -> {}
        }
    }

}