package com.example.mosisprojekat.ui.screens.main.gymdetails

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.example.mosisprojekat.ui.theme.GreenValid
import com.example.mosisprojekat.ui.theme.RedError
import com.example.mosisprojekat.ui.theme.spacing
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryButton
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryOutlinedTextField
import com.example.mosisprojekat.util.validateNotEmpty
import com.example.mosisprojekat.ui.screens.main.gymdetails.GymDetailsScreenViewModel.Events

@ExperimentalAnimationApi
@Composable
fun GymDetailsScreen(
    navController: NavHostController,
    selectedGymId: String
) {
    val viewModel = hiltViewModel<GymDetailsScreenViewModel>()

    viewModel.getGymData(selectedGymId)

    EventsHandler(navController, viewModel)
    GymDetailsScreenView(viewModel, selectedGymId)
}

@Composable
private fun GymDetailsScreenView(
    viewModel: GymDetailsScreenViewModel,
    selectedGymId: String
) {
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val gym by remember { viewModel.gym }

    val nameFocusRequester = remember { FocusRequester() }
    val nameTextState by rememberSaveable { viewModel.nameTextState }
    val isErrorMessagePairName = validateNotEmpty(nameTextState, context)

    val isEditButtonEnabled by remember { viewModel.isEditButtonEnabled }

    val isDeleteButtonEnabled by remember { viewModel.isDeleteButtonEnabled }

    val isEditing by remember { viewModel.isEditing }

    val scrollState = rememberScrollState()

    if (gym != null) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                    .background(MaterialTheme.colors.background)
                    .verticalScroll(scrollState)
                    .padding(top = MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = MaterialTheme.spacing.large)
                ) {

                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = R.string.gym_details_screen_title),
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.onBackground
                    )

                    if (isDeleteButtonEnabled)
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(
                                    end = MaterialTheme.spacing.medium
                                )
                                .scale(1.5f),
                            onClick = { viewModel.deleteGym(selectedGymId) },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete icon",
                                tint = RedError
                            )
                        }
                }

                Column {

                    PrimaryOutlinedTextField(
                        modifier = Modifier
                            .focusRequester(nameFocusRequester)
                            .padding(top = MaterialTheme.spacing.medium),
                        textStateValue = nameTextState,
                        isError = isErrorMessagePairName.first,
                        onValueChange = { viewModel.onNameTextChanged(it) },
                        label = stringResource(id = R.string.gym_details_screen_name_label),
                        trailingIconVector =
                        if (isEditButtonEnabled)
                            if (isEditing)
                                Icons.Filled.EditOff
                            else
                                Icons.Filled.Edit
                        else null,
                        onTrailingIconClick = { viewModel.onEnableEditButtonClick() },
                        imeAction = ImeAction.Done,
                        onDone = { focusManager.clearFocus() },
                        enabled = isEditing
                    )

                    Text(
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.spacing.extraSmall),
                        text = isErrorMessagePairName.second,
                        color = if (isErrorMessagePairName.first) RedError else GreenValid
                    )
                }

                PrimaryOutlinedTextField(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
                    textStateValue = gym?.lat.toString(),
                    onValueChange = { },
                    label = stringResource(id = R.string.gym_details_screen_latitude_label),
                    trailingIconVector = null,
                    enabled = false
                )

                PrimaryOutlinedTextField(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
                    textStateValue = gym?.lng.toString(),
                    onValueChange = { },
                    label = stringResource(id = R.string.gym_details_screen_longitude_label),
                    trailingIconVector = null,
                    enabled = false
                )

                /*PrimaryOutlinedTextField(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
                    singleLine = false,
                    textStateValue = gym?.comment ?: "",
                    onValueChange = { },
                    label = stringResource(id = R.string.gym_details_screen_comment_label),
                    trailingIconVector = null,
                    enabled = false
                )*/

                PrimaryButton(
                    modifier = Modifier
                        .padding(top = MaterialTheme.spacing.large)
                        .height(57.dp)
                        .width(150.dp),
                    text =
                    if (isEditing)
                        stringResource(id = R.string.gym_details_screen_save_button_text)
                    else
                        stringResource(id = R.string.gym_details_screen_ok_button_text),
                    onClick = { viewModel.onEditGymButtonClicked(selectedGymId) }
                )

                PrimaryButton(
                    modifier = Modifier
                        .padding(top = MaterialTheme.spacing.large)
                        .height(57.dp)
                        .width(150.dp),
                    text = stringResource(id = R.string.gym_details_screen_add_review_button_text),
                    onClick = { viewModel.onAddReviewButtonClicked(selectedGymId) }
                )
            }

        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: GymDetailsScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.NavigateToHomeScreen -> {
                navController.popBackStack()
                navController.navigate(Routes.HOME_SCREEN)
            }

            Events.MakeGenericErrorToast -> {
                Toast.makeText(
                    context,
                    context.getText(R.string.error_generic),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearEventChannel()
            }

            is Events.NavigateToAddRatingScreen -> {
                val selectedGymId = (event.value as Events.NavigateToAddRatingScreen).selectedGymId
                navController.navigate(Routes.ADD_REVIEW_SCREEN + "/"+ selectedGymId)
            }

            else -> {}
        }
    }

}