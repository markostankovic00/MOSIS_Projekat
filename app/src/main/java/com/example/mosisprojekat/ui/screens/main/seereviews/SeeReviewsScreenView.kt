package com.example.mosisprojekat.ui.screens.main.seereviews

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.R
import com.example.mosisprojekat.ui.screens.main.seereviews.SeeReviewsScreenViewModel.Events
import com.example.mosisprojekat.ui.theme.GreyLight
import com.example.mosisprojekat.ui.theme.RankingRowHighlightColor
import com.example.mosisprojekat.ui.theme.RedError
import com.example.mosisprojekat.ui.theme.spacing

@ExperimentalAnimationApi
@Composable
fun SeeReviewsScreen(
    navController: NavHostController,
    selectedGymId: String
) {
    val viewModel = hiltViewModel<SeeReviewsScreenViewModel>()

    EventsHandler(navController, viewModel)
    SeeReviewsScreenView(viewModel, selectedGymId)
}

@Composable
private fun SeeReviewsScreenView(
    viewModel: SeeReviewsScreenViewModel,
    selectedGymId: String
) {

    viewModel.loadReviews(selectedGymId)
    viewModel.loadCurrentUser()

    val reviews by viewModel.reviews.collectAsState()

    val currentUserId by remember {viewModel.currentUserId}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium),
            text = stringResource(id = R.string.see_reviews_screen_title),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground
        )

        LazyColumn(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.large
                )
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            items(reviews) {review ->

                val isOwner = review.userId == currentUserId

                Box {
                    Column(
                        modifier = Modifier
                            .padding(
                                vertical = MaterialTheme.spacing.medium,
                                horizontal = MaterialTheme.spacing.medium
                            )
                            .background(
                                color = if (isOwner)
                                    RankingRowHighlightColor
                                else
                                    MaterialTheme.colors.surface,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(MaterialTheme.spacing.small)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = MaterialTheme.spacing.small),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(end = MaterialTheme.spacing.small),
                                text = "${review.mark}",
                                style = MaterialTheme.typography.overline
                            )
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "star",
                                tint = Color.Yellow
                            )
                        }

                        Text(
                            text = review.comment,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface
                        )
                    }


                    IconButton(
                        modifier = Modifier
                            .padding(
                                top = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium
                            )
                            .align(Alignment.TopEnd)
                            .scale(1.3f),
                        onClick = { viewModel.onDeleteReviewClicked(review, selectedGymId) },
                        enabled = isOwner
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete comment",
                            tint = if (isOwner) RedError else GreyLight
                        )
                    }
                }
            }
        }

    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: SeeReviewsScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {

            Events.MakeGenericErrorToast -> {
                Toast.makeText(
                    context,
                    context.getText(R.string.error_generic),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearEventChannel()
            }

            Events.MakeSuccessfulDeletionToast -> {
                Toast.makeText(
                    context,
                    context.getText(R.string.successful_deletion_toast),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearEventChannel()
            }

            else -> {}
        }
    }

}