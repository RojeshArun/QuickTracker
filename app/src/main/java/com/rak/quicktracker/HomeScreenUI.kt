package com.rak.quicktracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

// Google Maps imports
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// Import VehicleData and readVehiclesFromRaw from the maps package
import com.rak.quicktracker.maps.VehicleData
import com.rak.quicktracker.maps.readVehiclesFromRaw


/**
 * Author: Mulasa Rojesh Arun kumar
 *
 * This file defines the Home Screen UI using Jetpack Compose for the fleet tracking application.
 * It provides an overview of the fleet, navigation buttons to other sections like
 * the real-time fleet map and vehicle registration, and fleet control actions.
 */

/**
 * Composable function for the Home screen UI.
 * This screen serves as the main dashboard for fleet management.
 *
 * @param navController The [NavController] instance used for navigating between different screens
 * within the application.
 * @author Mulasa Rojesh Arun kumar
 */
@OptIn(MapsComposeExperimentalApi::class) // Add this for GoogleMap composable
@Composable
fun HomeScreenUI(navController: NavController) {
    val context = LocalContext.current
    val vehicles = remember { readVehiclesFromRaw(context) }

    // Camera position for the world map view - changed zoom to 0f for whole world
    val worldCameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 0f) // Zoom set to 0f
    }

    // The main container box filling the entire screen with a light background.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {
        // --- Application Title Header at the very top ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color(0xFF007BFF), shape = RectangleShape)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
                    bottom = 16.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Autonomous Fleet Management System",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        // --- End Application Title Header ---

        // Calculate the height of the custom top bar for content positioning
        val customTopBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp + 16.dp + 24.sp.toDp()

        // Column to arrange major UI elements vertically
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = customTopBarHeight + 16.dp, // Space below the top header
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Header Card: "View Real-Time Fleet Map" - Reduced height and font size
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { navController.navigate("map") }
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF007BFF))
                        .fillMaxWidth()
                        .padding(vertical = 16.dp), // Reduced vertical padding
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "View Real-Time Fleet Map",
                        fontSize = 16.sp, // Reduced font size
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Space between button and map

            // --- Embedded Google Map --- Increased height and enabled gestures
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp) // Increased height
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = worldCameraPositionState,
                        uiSettings = com.google.maps.android.compose.MapUiSettings(
                            zoomControlsEnabled = true, // Enabled zoom controls
                            scrollGesturesEnabled = true, // Enabled scrolling/panning
                            zoomGesturesEnabled = true, // Enabled pinch-to-zoom
                            rotationGesturesEnabled = false,
                            tiltGesturesEnabled = false,
                            mapToolbarEnabled = false
                        )
                    ) {
                        vehicles.forEach { vehicle ->
                            Marker(
                                state = MarkerState(
                                    position = LatLng(
                                        vehicle.latitude,
                                        vehicle.longitude
                                    )
                                ),
                                title = vehicle.name,
                                snippet = "${vehicle.make} ${vehicle.model}",
                                onClick = { _ -> false }
                            )
                        }
                    }
                }
            }
            // --- End Embedded Google Map ---

            Spacer(modifier = Modifier.height(16.dp)) // Space between map and "My Fleet" card

            // Fleet Summary Card
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "My Fleet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Column {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            FleetInfoCell(
                                iconRes = android.R.drawable.ic_menu_myplaces,
                                label = "Total: 5",
                                tint = Color(0xFF007BFF),
                                modifier = Modifier.weight(1f)
                            )
                            FleetInfoCell(
                                iconRes = android.R.drawable.ic_menu_compass,
                                label = "Avg Speed: 22.5",
                                tint = Color(0xFF17A2B8),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            FleetInfoCell(
                                iconRes = android.R.drawable.ic_lock_idle_charging,
                                label = "Battery: 76%",
                                tint = Color(0xFF28A745),
                                modifier = Modifier.weight(1f)
                            )
                            FleetInfoCell(
                                iconRes = android.R.drawable.ic_dialog_alert,
                                label = "Alerts: 5",
                                tint = Color(0xFFDC3545),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Control Buttons Row: Start, Stop, Optimize
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("map") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                ) {
                    Text("Start", color = Color.White)
                }

                Button(
                    onClick = { /* TODO: Implement Stop functionality */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC3545)),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                ) {
                    Text("Stop", color = Color.White)
                }

                Button(
                    onClick = { /* TODO: Implement Optimize functionality */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Optimize", color = Color.Black)
                }
            }

            // Register New Car Button
            Button(
                onClick = { navController.navigate("car_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F42C1)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Text("Add a Vehicle", color = Color.White)
            }
        }
    }
}

/**
 * Composable function to display a single information cell within the fleet summary grid.
 * It combines an icon and a label.
 */
@Composable
fun FleetInfoCell(iconRes: Int, label: String, tint: Color, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = tint,
            modifier = Modifier
                .size(28.dp)
                .padding(end = 8.dp)
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}

/**
 * Extension function to convert a [TextUnit] (like `sp`) to [Dp] (density-independent pixels).
 * This is useful for accurately calculating dimensions based on text size.
 */
@Composable
fun TextUnit.toDp(): Dp {
    val density = LocalDensity.current
    return with(density) {
        this@toDp.toDp()
    }
}