// Package declaration for the QuickTracker application.
package com.rak.quicktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rak.quicktracker.maps.MapScreen
import com.rak.quicktracker.register.CarScreen
import com.rak.quicktracker.ui.theme.QuickTrackerTheme

/**
 * [HomeScreen] is the main entry point for the QuickTracker application.
 * It extends [ComponentActivity] and sets up the Compose UI.
 *
 * @author Mulasa Rojesh Arun kumar
 */
class HomeScreen : ComponentActivity() {
    /**
     * Called when the activity is first created. This is where we initialize the UI.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in [onSaveInstanceState]. Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content of the activity using Jetpack Compose.
        setContent {
            // Apply the QuickTracker custom theme to the application.
            QuickTrackerTheme {
                // Call the main composable function for the QuickTracker application.
                QuickTrackerApp()
            }
        }
    }
}

/**
 * [QuickTrackerApp] is the root composable function for the QuickTracker application's navigation.
 * It sets up the navigation graph using [NavHost].
 *
 * @author Mulasa Rojesh Arun kumar
 */
@Composable
fun QuickTrackerApp() {
    // Remember a NavController to navigate between different screens in the app.
    val navController = rememberNavController()

    // Use a Surface to apply the background color from the current MaterialTheme.
    Surface(color = MaterialTheme.colorScheme.background) {
        // Define the navigation host, starting at the "home" destination.
        NavHost(navController = navController, startDestination = "home") {
            // Define the "home" composable route.
            composable("home") {
                // Display the HomeScreenUI and pass the navController for navigation actions.
                HomeScreenUI(navController)
            }
            // Define the "map" composable route.
            composable("map") {
                // Display the MapScreen.
                MapScreen()
            }
            // Define the "car_screen" composable route.
            composable("car_screen") {
                // Display the CarScreen and pass the navController for navigation actions.
                CarScreen(navController = navController)
            }
        }
    }
}