import Versions.android_material_version
import Versions.compose_accompanist_version
import Versions.compose_activity_version
import Versions.compose_constraint_layout_version
import Versions.compose_hilt_navigation_version
import Versions.compose_navigation_version
import Versions.compose_version
import Versions.coroutines_android_version
import Versions.coroutines_core_version
import Versions.dagger_hilt_version
import Versions.datastore_version
import Versions.firebase_version
import Versions.glide_landscapist_version
import Versions.gson_converter_version
import Versions.gson_version
import Versions.hilt_compiler_version
import Versions.hilt_navigation_version
import Versions.hilt_work_version
import Versions.kotlin_serialization_version
import Versions.kotlin_work_version
import Versions.ktx_core_version
import Versions.lifecycle_version
import Versions.okhttp3_version
import Versions.retrofit_version
import Versions.room_version
import Versions.test_arch_core_version
import Versions.test_core_version
import Versions.test_coroutines_version
import Versions.test_espresso_core_version
import Versions.test_ext_junit_version
import Versions.test_google_truth_version
import Versions.test_junit_version
import Versions.test_ktx_core_version
import Versions.test_mock_webserver_version
import Versions.test_mockk_version
import Versions.test_runner_version
import Versions.test_turbine_version
import Versions.timber_version

object Dependencies {

    //PROJECT FOUNDATION
    const val ktx_core = "androidx.core:core-ktx:$ktx_core_version"
    const val android_material = "com.google.android.material:material:$android_material_version"
    const val compose_ui = "androidx.compose.ui:ui:$compose_version"
    const val compose_material = "androidx.compose.material:material:$compose_version"
    const val compose_ui_preview = "androidx.compose.ui:ui-tooling-preview:$compose_version"
    const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    const val compose_activity = "androidx.activity:activity-compose:$compose_activity_version"
    const val kotlin_work = "androidx.work:work-runtime-ktx:$kotlin_work_version"

    //COMPOSE DEPENDENCIES
    const val compose_viewmodel_lifecycle = "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version"
    const val compose_navigation = "androidx.navigation:navigation-compose:$compose_navigation_version"
    const val compose_icons_extended = "androidx.compose.material:material-icons-extended:$compose_version"
    const val compose_hilt_navigation = "androidx.hilt:hilt-navigation-compose:$compose_hilt_navigation_version"
    const val compose_animated_navigation = "com.google.accompanist:accompanist-navigation-animation:$compose_accompanist_version"
    const val compose_system_ui_controller = "com.google.accompanist:accompanist-systemuicontroller:$compose_accompanist_version"
    const val compose_constraint_layout = "androidx.constraintlayout:constraintlayout-compose:$compose_constraint_layout_version"

    //COROUTINES
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_core_version"
    const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_android_version"

    //LIVEDATA
    const val livedata_lifecycle = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    const val livedata_compose_runtime = "androidx.compose.runtime:runtime-livedata:$compose_version"

    //FIREBASE
    const val firebase_platform = "com.google.firebase:firebase-bom:$firebase_version"
    const val firebase_authentication = "com.google.firebase:firebase-auth-ktx"

    //DAGGER HILT
    const val dagger_hilt = "com.google.dagger:hilt-android:$dagger_hilt_version"
    const val dagger_hilt_compiler = "com.google.dagger:hilt-android-compiler:$dagger_hilt_version"
    const val hilt_navigation = "androidx.hilt:hilt-navigation-fragment:$hilt_navigation_version"
    const val hilt_work = "androidx.hilt:hilt-work:$hilt_work_version"
    const val hilt_compiler = "androidx.hilt:hilt-compiler:$hilt_compiler_version"

    //NETWORK & SERIALIZATION
    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
    const val kotlin_serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlin_serialization_version"
    const val gson_converter = "com.squareup.retrofit2:converter-gson:$gson_converter_version"
    const val gson = "com.google.code.gson:gson:$gson_version"
    const val okhttp3_okhttp = "com.squareup.okhttp3:okhttp:$okhttp3_version"
    const val okhttp3_logging_interceptor = "com.squareup.okhttp3:logging-interceptor:$okhttp3_version"

    //DATASTORE
    const val datastore_protobuf = "androidx.datastore:datastore:$datastore_version"
    const val datastore_preferences = "androidx.datastore:datastore-preferences:$datastore_version"

    //ROOM
    const val room_runtime = "androidx.room:room-runtime:$room_version"
    const val room_ktx = "androidx.room:room-ktx:$room_version"
    const val room_compiler = "androidx.room:room-compiler:$room_version"

    //GLIDE
    const val glide_landscapist = "com.github.skydoves:landscapist-glide:$glide_landscapist_version"

    //TIMBER
    const val timber = "com.jakewharton.timber:timber:$timber_version"

    //TESTING
    const val test_core = "androidx.test:core:$test_core_version"
    const val test_junit = "junit:junit:$test_junit_version"
    const val test_arch_core = "androidx.arch.core:core-testing:$test_arch_core_version"
    const val test_coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$test_coroutines_version"
    const val test_google_truth = "com.google.truth:truth:$test_google_truth_version"
    const val test_mock_webserver = "com.squareup.okhttp3:mockwebserver:$test_mock_webserver_version"
    const val test_mockk = "io.mockk:mockk:$test_mockk_version"
    const val test_flow_turbine = "app.cash.turbine:turbine:$test_turbine_version"
    const val test_compose_ui_manifest = "androidx.compose.ui:ui-test-manifest:$compose_version"
    const val test_dagger_hilt = "com.google.dagger:hilt-android-testing:$dagger_hilt_version"
    const val test_ext_junit = "androidx.test.ext:junit:$test_ext_junit_version"
    const val test_ktx_core = "androidx.test:core-ktx:$test_ktx_core_version"
    const val test_runner = "androidx.test:runner:$test_runner_version"
    const val test_espresso_core = "androidx.test.espresso:espresso-core:$test_espresso_core_version"
    const val test_compose_ui_junit = "androidx.compose.ui:ui-test-junit4:$compose_version"
    const val test_compose_ui_tooling = "androidx.compose.ui:ui-tooling:$compose_version"
}