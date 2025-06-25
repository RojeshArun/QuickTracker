// Package declaration for the maps feature of the QuickTracker application.
package com.rak.quicktracker.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets // Import WindowInsets
import androidx.compose.foundation.layout.asPaddingValues // Import asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars // Import statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape // Import RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


/**
 * [MapScreen] is a Composable function that displays vehicle tracking information.
 * It provides a toggleable view between a Google Map showing vehicle locations and
 * a list of vehicles. When a vehicle is selected on the map or from the list,
 * its detailed information is displayed.
 *
 * This screen utilizes `com.google.maps.android:maps-compose` and `com.google.android.gms:play-services-maps`
 * for map functionalities.
 *
 * @author Mulasa Rojesh Arun kumar
 */
@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun MapScreen() {
    // Obtain the current context, used here to read vehicle data.
    val context = LocalContext.current
    // Remember the list of vehicles loaded from raw resources.
    val vehicles = remember { readVehiclesFromRaw(context) }

    // State to manage the view toggle: true for map view, false for list view.
    var isMapView by rememberSaveable { mutableStateOf(true) }

    // State to hold the currently selected vehicle. Defaults to the first vehicle if available.
    var selectedVehicle by remember { mutableStateOf<VehicleData?>(vehicles.firstOrNull()) } // Default to first vehicle for title

    // Remember the camera position state for the Google Map, initializing it to the selected vehicle's location.
    val cameraPositionState = rememberCameraPositionState {
        selectedVehicle?.let {
            position = CameraPosition.fromLatLngZoom(LatLng(it.latitude, it.longitude), 10f)
        }
    }

    // Calculate the height of the custom top bar
    val customTopBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp + 16.dp + 24.dp // Approximate, 24.sp converted to Dp

    // The main container box filling the entire screen with a light background.
    // It now hosts both the custom header and the Scaffold.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {
        // --- Custom Application Title Header ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color(0xFF007BFF), shape = RectangleShape) // No rounded corners
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
        // --- End Custom Application Title Header ---

        // Scaffold provides the basic visual structure for Material Design screens.
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                // Apply the calculated custom top bar height as top padding to the Scaffold
                .padding(top = customTopBarHeight),
            topBar = {
                // Top app bar displaying the screen title and the name of the selected vehicle.
                TopAppBar(
                    title = {
                        Text(
                            text = "Vehicle Tracking - ${selectedVehicle?.name ?: "No Vehicle Selected"}",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                    // Crucial: Set windowInsets to WindowInsets(0) to remove default top padding/insets
                    windowInsets = WindowInsets(0)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { isMapView = !isMapView },
                    modifier = Modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50)
                ) {
                    Icon(
                        imageVector = if (isMapView) Icons.Filled.List else Icons.Filled.Map,
                        contentDescription = if (isMapView) "Switch to List View" else "Switch to Map View"
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    if (isMapView) {
                        Box(modifier = Modifier.weight(1f)) {
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                onMapLoaded = {
                                    selectedVehicle?.let {
                                        cameraPositionState.move(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(it.latitude, it.longitude),
                                                12f
                                            )
                                        )
                                    }
                                }
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
                                        onClick = { marker ->
                                            selectedVehicle = vehicles.find { it.name == marker.title }
                                            false
                                        }
                                    )
                                }
                            }

                            selectedVehicle?.let { vehicle ->
                                Card(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .background(Color.White, RoundedCornerShape(12.dp))
                                        .wrapContentHeight(),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "${vehicle.name} (${vehicle.regNumber})",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        VehicleDetailRow(
                                            Icons.Filled.DirectionsCar,
                                            "${vehicle.make} ${vehicle.model} (${vehicle.vehicleType})"
                                        )
                                        VehicleDetailRow(
                                            Icons.Filled.LocationOn,
                                            "Lat: ${vehicle.latitude}, Lng: ${vehicle.longitude}"
                                        )
                                        VehicleDetailRow(
                                            Icons.Filled.Speed,
                                            "Speed: ${vehicle.speed} km/h"
                                        )
                                        VehicleDetailRow(
                                            Icons.Filled.BatteryChargingFull,
                                            "Battery: ${vehicle.battery}%"
                                        )
                                        if (vehicle.alerts > 0) {
                                            VehicleDetailRow(
                                                Icons.Filled.Warning,
                                                "Alerts: ${vehicle.alerts}",
                                                textColor = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 0.dp)
                        ) {
                            items(vehicles) { vehicle ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp, horizontal = 8.dp)
                                        .clickable {
                                            selectedVehicle = vehicle
                                            isMapView = true
                                        },
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            "${vehicle.name} - ${vehicle.regNumber}",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text("${vehicle.make} ${vehicle.model} (${vehicle.vehicleType})")
                                        Text("📍 ${vehicle.latitude}, ${vehicle.longitude}")
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = "Note: Rotate screen for a better view.",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        )
    }
}

/**
 * [VehicleDetailRow] is a helper Composable function to display a single row of vehicle details
 * with an icon and text.
 *
 * @param icon The [ImageVector] icon to display.
 * @param text The text content for the detail row.
 * @param textColor The color of the text (defaults to [Color.Black]).
 * @author Mulasa Rojesh Arun kumar
 */
@Composable
fun VehicleDetailRow(
    icon: ImageVector,
    text: String,
    textColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 16.sp, color = textColor)
    }
}