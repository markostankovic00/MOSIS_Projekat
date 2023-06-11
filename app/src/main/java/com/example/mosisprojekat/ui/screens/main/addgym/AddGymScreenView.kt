package com.example.mosisprojekat.ui.screens.main.addgym

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.mosisprojekat.ui.screens.main.addgym.AddGymScreenViewModel.Events
import com.example.mosisprojekat.ui.theme.GreenValid
import com.example.mosisprojekat.ui.theme.RedError
import com.example.mosisprojekat.ui.theme.spacing
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryButton
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryOutlinedTextField
import com.example.mosisprojekat.util.validateNotEmpty

@ExperimentalAnimationApi
@Composable
fun AddGymScreen(
    navController: NavHostController,
    lat: Double,
    lng: Double
) {
    val viewModel = hiltViewModel<AddGymScreenViewModel>()

    EventsHandler(navController, viewModel)
    AddGymScreenView(viewModel, lat, lng)
}

@Composable
private fun AddGymScreenView(
    viewModel: AddGymScreenViewModel,
    lat: Double,
    lng: Double
) {

    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val nameFocusRequester = remember { FocusRequester() }
    val nameTextState by rememberSaveable { viewModel.nameTextState}
    val isErrorMessagePairName = validateNotEmpty(nameTextState, context)

    val commentFocusRequester = remember { FocusRequester() }
    val commentTextState by rememberSaveable { viewModel.commentTextState}

    val rating by rememberSaveable{ viewModel.rating }

    val addGymButtonEnabled = !isErrorMessagePairName.first

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .background(MaterialTheme.colors.background)
            .verticalScroll(scrollState)
            .padding(top = MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .padding(MaterialTheme.spacing.large),
            text = stringResource(id = R.string.add_gym_screen_title),
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground
        )

        Column {

            PrimaryOutlinedTextField(
                modifier = Modifier
                    .focusRequester(nameFocusRequester)
                    .padding(top = MaterialTheme.spacing.medium),
                textStateValue = nameTextState,
                isError = isErrorMessagePairName.first,
                onValueChange = { viewModel.onNameTextChanged(it) },
                label = stringResource(id = R.string.add_gym_screen_name_label),
                onTrailingIconClick = { viewModel.onNameTextChanged("") },
                onNext = {
                    focusManager.clearFocus()
                    commentFocusRequester.requestFocus()
                }
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
            textStateValue = lat.toString(),
            onValueChange = { },
            label = stringResource(id = R.string.add_gym_screen_latitude_label),
            trailingIconVector = null,
            enabled = false
        )

        PrimaryOutlinedTextField(
            modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
            textStateValue = lng.toString(),
            onValueChange = { },
            label = stringResource(id = R.string.add_gym_screen_longitude_label),
            trailingIconVector = null,
            enabled = false
        )

        PrimaryOutlinedTextField(
            modifier = Modifier
                .focusRequester(commentFocusRequester)
                .padding(top = MaterialTheme.spacing.medium),
            singleLine = false,
            textStateValue = commentTextState,
            onValueChange = { viewModel.onCommentTextChanged(it) },
            label = stringResource(id = R.string.add_gym_screen_comment_label),
            onTrailingIconClick = { viewModel.onCommentTextChanged("") },
            imeAction = ImeAction.Done,
            onDone = {
                focusManager.clearFocus()
            }
        )

        PrimaryButton(
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.large)
                .height(57.dp)
                .width(150.dp),
            enabled = addGymButtonEnabled,
            text = stringResource(id = R.string.add_gym_screen_add_gym_button),
            onClick = { viewModel.addGym(lat, lng) }
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: AddGymScreenViewModel
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
            else -> {}
        }
    }

}