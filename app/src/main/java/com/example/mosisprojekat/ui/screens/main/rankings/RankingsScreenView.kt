package com.example.mosisprojekat.ui.screens.main.rankings

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.R
import com.example.mosisprojekat.ui.screens.main.rankings.RankingsScreenViewModel.Events
import com.example.mosisprojekat.ui.theme.RankingRowHighlightColor
import com.example.mosisprojekat.ui.theme.spacing
import com.example.mosisprojekat.ui.uiutil.composables.AutoResizedText
import com.example.mosisprojekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.example.mosisprojekat.util.ComponentSizes

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

    val listOfUsers by viewModel.listOfUsers.collectAsState()

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
                text = stringResource(id = R.string.rankings_screen_title),
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
                itemsIndexed(
                    listOfUsers.sortedByDescending { it.points }
                ) { index, user ->

                    UserRankingDataRow(
                        ranking = index + 1,
                        name = user.name,
                        surname = user.surname,
                        points = user.points,
                        isCurrentUser = user.authID == currentUserID
                    )
                }
            }
        }
    }
}

@Composable
private fun UserRankingDataRow(
    modifier: Modifier = Modifier,
    ranking: Int,
    name: String,
    surname: String,
    points: Int,
    isCurrentUser: Boolean
) {
    Row(
        modifier = modifier
            .padding(vertical = MaterialTheme.spacing.medium)
            .height(57.dp)
            .fillMaxWidth()
            .background(
                color = if (isCurrentUser)
                    RankingRowHighlightColor
                else
                    MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium
                ),
            text = ranking.toString(),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.primary
        )

        AutoResizedText(
            modifier = Modifier
                .padding(end = MaterialTheme.spacing.small),
            text = "$name $surname - $points points",
            style = MaterialTheme.typography.body1.copy(fontStyle = FontStyle.Italic),
            color = MaterialTheme.colors.onSurface
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: RankingsScreenViewModel
) {
    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.MakeGenericErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_generic), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            else -> {}
        }
    }

}