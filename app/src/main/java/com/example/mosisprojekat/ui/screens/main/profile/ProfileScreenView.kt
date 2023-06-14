package com.example.mosisprojekat.ui.screens.main.profile

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mosisprojekat.R
import com.example.mosisprojekat.ui.activities.AccountActivity
import com.example.mosisprojekat.ui.theme.spacing
import com.example.mosisprojekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.example.mosisprojekat.ui.screens.main.profile.ProfileScreenViewModel.Events
import com.example.mosisprojekat.ui.uiutil.composables.AutoResizedText
import com.example.mosisprojekat.ui.uiutil.composables.PrimaryButton
import com.example.mosisprojekat.util.ComponentSizes
import com.example.mosisprojekat.util.findActivity
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<ProfileScreenViewModel>()

    EventsHandler(navController, viewModel)
    ProfileScreenView(viewModel)
}

@ExperimentalCoroutinesApi
@Composable
private fun ProfileScreenView(
    viewModel: ProfileScreenViewModel
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp

    val scrollState = rememberScrollState()

    val userData by viewModel.userData.collectAsState()

    val isUploading by viewModel.isUploading.collectAsState()

    val selectImageLauncher = createSelectImageLauncher(viewModel)

    BoxWithBackgroundPattern {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isUploading) {
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .size(screenWidth.times(0.5f).dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary
                    )
                }
            }
            else {
                GlideImage(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .size(screenWidth.times(0.5f).dp)
                        .clickable { selectImageLauncher.launch("image/*") },
                    imageModel = { userData?.photoUrl ?: "" },
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colors.primary
                        )
                    },
                    failure = {
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small)
                                .size(screenWidth.times(0.5f).dp)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.scale(2f),
                                imageVector = Icons.Outlined.ErrorOutline,
                                contentDescription = "Icon representing image fetch failure",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                )
            }

            ProfileDataRow(
                label = stringResource(id = R.string.profile_screen_name_label),
                data = userData?.name ?: ""
            )

            ProfileDataRow(
                label = stringResource(id = R.string.profile_screen_surname_label),
                data = userData?.surname ?: ""
            )

            ProfileDataRow(
                label = stringResource(id = R.string.profile_screen_email_label),
                data = userData?.email ?: ""
            )

            ProfileDataRow(
                label = stringResource(id = R.string.profile_screen_points_label),
                data = userData?.points.toString()
            )

            PrimaryButton(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.extraLarge,
                        bottom = ComponentSizes.bottomNavBarHeight.dp
                    )
                    .height(57.dp)
                    .width(150.dp),
                text = stringResource(id = R.string.profile_screen_log_out_button),
                onClick = viewModel::onLogOut
            )
        }

    }

}

@Composable
private fun ProfileDataRow(
    modifier: Modifier = Modifier,
    label: String,
    data: String
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp

    Row(
        modifier = modifier
            .padding(vertical = MaterialTheme.spacing.medium)
            .height(57.dp)
            .width(screenWidth.times(0.7f).dp)
            .shadow(
                elevation = 15.dp,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        AutoResizedText(
            modifier = Modifier
                .padding(
                    start = MaterialTheme.spacing.small,
                    end = MaterialTheme.spacing.medium
                )
                .weight(2f),
            text = label,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onSurface
        )

        AutoResizedText(
            modifier = Modifier
                .padding(end = MaterialTheme.spacing.small)
                .weight(3f),
            text = data,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@ExperimentalCoroutinesApi
@Composable
fun createSelectImageLauncher(
    viewModel: ProfileScreenViewModel
) = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onProfilePhotoClicked(uri)
    }

@ExperimentalAnimationApi
@Composable
private fun EventsHandler(
    navController: NavHostController,
    viewModel: ProfileScreenViewModel
) {
    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {
        when (event.value) {
            Events.NavigateToSplashScreen -> {
                context.startActivity(Intent(context, AccountActivity::class.java))
                context.findActivity()?.finish()
            }
            Events.MakeSuccessfullyChangedProfilePictureToast -> {
                Toast.makeText(context, context.getText(R.string.successful_changed_profile_picture_toast), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            Events.MakeLogOutErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_invalid_logout), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            Events.MakeGenericErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_generic), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            else -> {}
        }
    }

}