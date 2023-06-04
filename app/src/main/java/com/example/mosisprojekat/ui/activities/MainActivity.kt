package com.example.mosisprojekat.ui.activities

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.example.mosisprojekat.ui.navigation.MainActivityLayoutAndNavigation
import com.example.mosisprojekat.ui.theme.MosisProjekatTheme
import com.example.mosisprojekat.util.location.DefaultLocationClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Timber.i("Logging: Permission granted after request")
            }
        }

    private fun askPermissions() = when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) -> {
            Timber.i("Logging: Permission already granted")
        }
        else -> {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    //private val viewModel: MainViewModel by viewModels()

    @SuppressLint("FlowOperatorInvokedInComposition", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleNavigationBugOnXiaomiDevices()

        setContent {

            val viewModel = hiltViewModel<MainViewModel>()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            askPermissions()

            val locationClient = DefaultLocationClient(
                applicationContext,
                LocationServices.getFusedLocationProviderClient(applicationContext)
            )

            locationClient
                .getLocationUpdates(10000L)
                .catch {e ->
                    viewModel.makeLocationErrorToast()
                    e.printStackTrace()
                }
                .onEach { location ->
                    viewModel.updateLocation(location)
                }
                .launchIn(lifecycleScope)

            MosisProjekatTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    MainActivityLayoutAndNavigation(viewModel)
                }
            }
        }
    }

    private fun handleNavigationBugOnXiaomiDevices() {
        window.decorView.post {
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}