package com.example.mosisprojekat.ui.screens.main.addreview

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.R
import com.example.mosisprojekat.ui.navigation.Routes
import com.example.mosisprojekat.ui.screens.main.addreview.AddReviewScreenViewModel.Events
import com.example.mosisprojekat.ui.theme.GreenValid
import com.example.mosisprojekat.ui.theme.RedError
import com.example.mosisprojekat.ui.theme.spacing
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryButton
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryOutlinedTextField
import com.example.mosisprojekat.util.validateNotEmpty

@ExperimentalAnimationApi
@Composable
fun AddReviewScreen(
    navController: NavHostController,
    selectedGymId: String
) {
    val viewModel = hiltViewModel<AddReviewScreenViewModel>()

    EventsHandler(navController, viewModel)
    AddReviewScreenView(viewModel, selectedGymId)
}

@Composable
private fun AddReviewScreenView(
    viewModel: AddReviewScreenViewModel,
    selectedGymId: String
) {

    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val commentTextState by rememberSaveable { viewModel.commentTextState}
    val isErrorMessagePairComment = validateNotEmpty(commentTextState, context)

    val markState by rememberSaveable { viewModel.markState }

    val saveReviewButtonEnabled = !isErrorMessagePairComment.first && markState!=0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .background(MaterialTheme.colors.background)
            .padding(top = MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .padding(MaterialTheme.spacing.extraLarge),
            text = stringResource(id = R.string.add_review_screen_title),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground
        )

        Column {
            PrimaryOutlinedTextField(
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.large,
                    start = MaterialTheme.spacing.large,
                    end = MaterialTheme.spacing.large
                ),
                textStateValue = commentTextState,
                isError = isErrorMessagePairComment.first,
                onValueChange = { viewModel.onCommentTextChanged(it) },
                label = stringResource(id = R.string.add_review_screen_comment_label),
                onTrailingIconClick = { viewModel.onCommentTextChanged("") },
                imeAction = ImeAction.Done,
                onDone = { focusManager.clearFocus() },
                singleLine = false
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.extraSmall + MaterialTheme.spacing.large),
                text = isErrorMessagePairComment.second,
                color = if (isErrorMessagePairComment.first) RedError else GreenValid
            )
        }

        Row(
            modifier = Modifier
                .padding(
                    vertical = MaterialTheme.spacing.extraLarge,
                    horizontal = MaterialTheme.spacing.large
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            for (index in 1..5) {
                Icon(
                    modifier = Modifier
                        .scale(2f)
                        .clickable { viewModel.onStarClick(index) },
                    imageVector = Icons.Filled.Star,
                    contentDescription = "One star",
                    tint = if (index <= markState) Color.Yellow else Color.LightGray
                )
            }

        }

        PrimaryButton(
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.extraLarge)
                .height(57.dp)
                .width(150.dp),
            enabled = saveReviewButtonEnabled,
            text = stringResource(id = R.string.add_review_screen_save_review_button),
            onClick = { viewModel.onSaveReviewClick(selectedGymId) }
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: AddReviewScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {

            is Events.NavigateToGymDetailsScreen -> {
                val selectedGymId = (event.value as Events.NavigateToGymDetailsScreen).selectedGymId
                navController.navigate(Routes.GYM_DETAILS_SCREEN + "/"+ selectedGymId)
            }

            Events.MakeGenericErrorToast -> {
                Toast.makeText(
                    context,
                    context.getText(R.string.error_generic),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearEventChannel()
            }

            else -> {}
        }
    }

}