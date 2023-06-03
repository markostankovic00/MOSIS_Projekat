package com.example.mosisprojekat.ui.screens.account.signup

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.R
import com.example.mosisprojekat.ui.activities.MainActivity
import com.example.mosisprojekat.ui.navigation.Routes
import com.example.mosisprojekat.ui.screens.account.signup.SignUpScreenViewModel.Events
import com.example.mosisprojekat.ui.theme.GreenValid
import com.example.mosisprojekat.ui.theme.LinkColor
import com.example.mosisprojekat.ui.theme.RedError
import com.example.mosisprojekat.ui.theme.spacing
import com.example.mosisprojekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryButton
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryOutlinedTextField
import com.example.mosisprojekat.util.*

@ExperimentalAnimationApi
@Composable
fun SignUpScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<SignUpScreenViewModel>()

    EventsHandler(navController, viewModel)
    SignUpScreenView(viewModel)
}

@Composable
private fun SignUpScreenView(
    viewModel: SignUpScreenViewModel
) {
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    val nameFocusRequester = remember { FocusRequester() }
    val nameTextState by rememberSaveable { viewModel.nameTextState}
    val isErrorMessagePairName = validateNotEmpty(nameTextState, context)

    val surnameFocusRequester = remember { FocusRequester() }
    val surnameTextState by rememberSaveable { viewModel.surnameTextState}
    val isErrorMessagePairSurname = validateNotEmpty(surnameTextState, context)

    val emailFocusRequester = remember { FocusRequester() }
    val emailTextState by rememberSaveable { viewModel.emailTextState}
    val isErrorMessagePairEmail = validateSignUpEmailTextField(emailTextState, context)

    val passwordFocusRequester = remember { FocusRequester() }
    var passwordVisibility by remember { mutableStateOf(false) }
    val passwordTextState by rememberSaveable { viewModel.passwordTextState }
    val isErrorMessagePairPassword = validateSignUpPasswordTextField(passwordTextState, context)

    val repeatPasswordFocusRequester = remember { FocusRequester() }
    var repeatPasswordVisibility by remember { mutableStateOf(false) }
    val repeatPasswordTextState by rememberSaveable { viewModel.repeatPasswordTextState }
    val isErrorMessagePairRepeatPassword =
        validateRepeatPasswordTextField(repeatPasswordTextState, passwordTextState, context)

    val signUpButtonEnabled = !isErrorMessagePairName.first &&
            !isErrorMessagePairSurname.first &&
            !isErrorMessagePairEmail.first &&
            !isErrorMessagePairPassword.first &&
            !isErrorMessagePairRepeatPassword.first

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
                .padding(top = MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium),
                text = stringResource(id = R.string.sign_up_screen_title),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )

            //name text field
            Column {
                PrimaryOutlinedTextField(
                    modifier = Modifier
                        .focusRequester(nameFocusRequester)
                        .padding(top = MaterialTheme.spacing.medium),
                    textStateValue = nameTextState,
                    onValueChange = viewModel::onNameTextChanged,
                    label = stringResource(id = R.string.sign_up_screen_name_label),
                    isError = isErrorMessagePairName.first,
                    onNext = {
                        focusManager.clearFocus()
                        surnameFocusRequester.requestFocus()
                    },
                    onTrailingIconClick = { viewModel.onNameTextChanged("") }
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    text = isErrorMessagePairName.second,
                    color = if (isErrorMessagePairName.first) RedError else GreenValid
                )
            }

            //surname text field
            Column {
                PrimaryOutlinedTextField(
                    modifier = Modifier
                        .focusRequester(surnameFocusRequester)
                        .padding(top = MaterialTheme.spacing.medium),
                    textStateValue = surnameTextState,
                    onValueChange = viewModel::onSurnameTextChanged,
                    label = stringResource(id = R.string.sign_up_screen_surname_label),
                    isError = isErrorMessagePairSurname.first,
                    onNext = {
                        focusManager.clearFocus()
                        emailFocusRequester.requestFocus()
                    },
                    onTrailingIconClick = { viewModel.onSurnameTextChanged("") }
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    text = isErrorMessagePairSurname.second,
                    color = if (isErrorMessagePairSurname.first) RedError else GreenValid
                )
            }

            //email text field
            Column {
                PrimaryOutlinedTextField(
                    modifier = Modifier
                        .focusRequester(emailFocusRequester)
                        .padding(top = MaterialTheme.spacing.medium),
                    textStateValue = emailTextState,
                    onValueChange = viewModel::onEmailTextChanged,
                    label = stringResource(id = R.string.sign_up_screen_email_label),
                    isError = isErrorMessagePairEmail.first,
                    onNext = {
                        focusManager.clearFocus()
                        passwordFocusRequester.requestFocus()
                    },
                    onTrailingIconClick = { viewModel.onEmailTextChanged("") }
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    text = isErrorMessagePairEmail.second,
                    color = if (isErrorMessagePairEmail.first) RedError else GreenValid
                )
            }

            //password text field
            Column {
                PrimaryOutlinedTextField(
                    modifier = Modifier
                        .focusRequester(passwordFocusRequester)
                        .padding(top = MaterialTheme.spacing.medium),
                    textStateValue = passwordTextState,
                    onValueChange = viewModel::onPasswordTextChanged,
                    label = stringResource(id = R.string.sign_up_screen_password_label),
                    isError = isErrorMessagePairPassword.first,
                    imeAction = ImeAction.Next,
                    onNext = {
                        focusManager.clearFocus()
                        repeatPasswordFocusRequester.requestFocus()
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

            //repeat password text field
            Column {
                PrimaryOutlinedTextField(
                    modifier = Modifier
                        .focusRequester(repeatPasswordFocusRequester)
                        .padding(top = MaterialTheme.spacing.medium),
                    textStateValue = repeatPasswordTextState,
                    onValueChange = viewModel::onRepeatPasswordTextChanged,
                    label = stringResource(id = R.string.sign_up_screen_repeat_password_label),
                    isError = isErrorMessagePairRepeatPassword.first,
                    imeAction = ImeAction.Done,
                    onDone = {
                        focusManager.clearFocus()
                    },
                    visualTransformation =
                    if (repeatPasswordVisibility)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIconVector =
                    if (!repeatPasswordVisibility)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff,
                    onTrailingIconClick = { repeatPasswordVisibility = !repeatPasswordVisibility }
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    text = isErrorMessagePairRepeatPassword.second,
                    color = if (isErrorMessagePairRepeatPassword.first) RedError else GreenValid
                )
            }



            //button sector
            PrimaryButton(
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.large)
                    .height(57.dp)
                    .width(150.dp),
                enabled = signUpButtonEnabled,
                text = stringResource(id = R.string.sign_up_screen_sign_up_button),
                onClick = viewModel::onSignUpClick
            )

            Row(
                Modifier
                    .padding(vertical = MaterialTheme.spacing.large),
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    style = MaterialTheme.typography.body1,
                    text = stringResource(id = R.string.sign_up_screen_already_have_an_account),
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    modifier = Modifier
                        .clickable { viewModel.navigateToLogIn() }
                        .padding(horizontal = MaterialTheme.spacing.extraSmall),
                    style = MaterialTheme.typography.body1,
                    text = stringResource(id = R.string.sign_up_screen_already_have_an_account_press_here),
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
    viewModel: SignUpScreenViewModel
) {
    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.NavigateToHomeScreen -> {
                context.startActivity(Intent(context, MainActivity::class.java))
                context.findActivity()?.finish()
            }
            Events.NavigateToLogIn -> {
                navController.popBackStack()
                navController.navigate(Routes.LOG_IN_SCREEN)
            }
            Events.MakeSignupErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_invalid_signup), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            else -> {}
        }
    }
}