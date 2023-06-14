package com.example.mosisprojekat.ui.activities

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.mosisprojekat.ui.navigation.MainActivityLayoutAndNavigation
import com.example.mosisprojekat.ui.theme.MosisProjekatTheme
import com.example.mosisprojekat.util.location.DefaultLocationClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                initializeLocationClient(viewModel)
                isLocationPermissionGranted = true
            }
        }

    private fun askPermissions() = when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) -> {
            Timber.i("Logging: Permission is already granted")
            isLocationPermissionGranted = true
        }
        else -> {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    private val viewModel by viewModels<MainViewModel>()

    private var isLocationPermissionGranted = false

    @SuppressLint("FlowOperatorInvokedInComposition", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleNavigationBugOnXiaomiDevices()

        setContent {

            askPermissions()

            MosisProjekatTheme {

                val context = LocalContext.current

                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                val gpsEnabled by remember { viewModel.gpsEnabled }

                viewModel.checkIfGPSEnabled(locationManager)

                if(isLocationPermissionGranted)
                    LaunchedEffect(key1 = gpsEnabled) {
                        if (gpsEnabled)
                            initializeLocationClient(viewModel)
                    }

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

    private fun initializeLocationClient(
        viewModel: MainViewModel
    ) {
        val locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        locationClient
            .getLocationUpdates(5000L)
            .catch {e ->
                viewModel.makeLocationErrorToast()
                e.printStackTrace()
            }
            .onEach { location ->
                Timber.i("TAGA: lat: ${location.latitude}, long: ${location.longitude}")
                viewModel.updateLocation(location)
            }
            .launchIn(lifecycleScope)
    }

    private fun handleNavigationBugOnXiaomiDevices() {
        window.decorView.post {
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}