package com.example.mosisprojekat.ui.screens.account.login

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.R
import com.example.mosisprojekat.ui.activities.MainActivity
import com.example.mosisprojekat.ui.navigation.Routes
import com.example.mosisprojekat.ui.screens.account.login.LogInScreenViewModel.Events
import com.example.mosisprojekat.ui.theme.GreenValid
import com.example.mosisprojekat.ui.theme.LinkColor
import com.example.mosisprojekat.ui.theme.RedError
import com.example.mosisprojekat.ui.theme.spacing
import com.example.mosisprojekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryButton
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryOutlinedTextField
import com.example.mosisprojekat.util.findActivity
import com.example.mosisprojekat.util.validatePasswordTextField
import com.example.mosisprojekat.util.validateUsernameTextField

@ExperimentalAnimationApi
@Composable
fun LogInScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<LogInScreenViewModel>()

    EventsHandler(navController, viewModel)
    LogInScreenView(viewModel)
}

@Composable
private fun LogInScreenView(
    viewModel: LogInScreenViewModel
) {
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    val usernameFocusRequester = remember { FocusRequester() }
    val usernameTextState by rememberSaveable { viewModel.usernameTextState}
    val isErrorMessagePairUsername = validateUsernameTextField(usernameTextState, context)

    val passwordFocusRequester = remember { FocusRequester() }
    var passwordVisibility by remember { mutableStateOf(false) }
    val passwordTextState by rememberSaveable { viewModel.passwordTextState }
    val isErrorMessagePairPassword = validatePasswordTextField(passwordTextState, context)

    val logInButtonEnabled = !isErrorMessagePairUsername.first && !isErrorMessagePairPassword.first

    BoxWithBackgroundPattern(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = MaterialTheme.spacing.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.large),
                text = stringResource(id = R.string.log_in_screen_title),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )

            //username text field
            Column {
                PrimaryOutlinedTextField(
                    modifier = Modifier
                        .focusRequester(usernameFocusRequester)
                        .padding(top = MaterialTheme.spacing.large),
                    textStateValue = usernameTextState,
                    onValueChange = viewModel::onUsernameTextChanged,
                    label = stringResource(id = R.string.log_in_screen_username_label),
                    isError = isErrorMessagePairUsername.first,
                    onNext = {
                        focusManager.clearFocus()
                        passwordFocusRequester.requestFocus()
                    },
                    onTrailingIconClick = { viewModel.onUsernameTextChanged("") }
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    text = isErrorMessagePairUsername.second,
                    color = if (isErrorMessagePairUsername.first) RedError else GreenValid
                )
            }

            //password text field
            Column {
                PrimaryOutlinedTextField(
                    modifier = Modifier
                        .focusRequester(passwordFocusRequester)
                        .padding(top = MaterialTheme.spacing.large),
                    textStateValue = passwordTextState,
                    onValueChange = viewModel::onPasswordTextChanged,
                    label = stringResource(id = R.string.log_in_screen_password_label),
                    isError = isErrorMessagePairPassword.first,
                    imeAction = ImeAction.Done,
                    onDone = {
                        focusManager.clearFocus()
                    },
                    visualTransformation =
                        if (passwordVisibility)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                    trailingIconVector =
                        if (!passwordVisibility)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                    onTrailingIconClick = { passwordVisibility = !passwordVisibility }
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    text = isErrorMessagePairPassword.second,
                    color = if (isErrorMessagePairPassword.first) RedError else GreenValid
                )
            }

            PrimaryButton(
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.extraLarge)
                    .height(57.dp)
                    .width(150.dp),
                enabled = logInButtonEnabled,
                text = stringResource(id = R.string.log_in_screen_log_in_button),
                onClick = viewModel::onLogInClick
            )

            Row(
                Modifier
                    .padding(top = MaterialTheme.spacing.large),
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    style = MaterialTheme.typography.body1,
                    text = stringResource(id = R.string.log_in_screen_dont_have_an_account),
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    modifier = Modifier
                        .clickable { viewModel.navigateToSignUp() }
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    style = MaterialTheme.typography.body1,
                    text = stringResource(id = R.string.log_in_screen_dont_have_an_account_press_here),
                    color = LinkColor
                )
            }

        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: LogInScreenViewModel
) {
    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.NavigateToHomeScreen -> {
                context.startActivity(Intent(context, MainActivity::class.java))
                context.findActivity()?.finish()
            }
            Events.NavigateToSignUp -> {
                navController.popBackStack()
                navController.navigate(Routes.SIGN_UP_SCREEN)
            }
            Events.MakeToast -> {
                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

}