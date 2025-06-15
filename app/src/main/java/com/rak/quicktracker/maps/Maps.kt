// Package declaration for the maps feature of the QuickTracker application.
package com.rak.quicktracker.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

    // Scaffold provides the basic visual structure for Material Design screens.
    Scaffold(
        topBar = {
            // Top app bar displaying the screen title and the name of the selected vehicle.
            TopAppBar(
                title = {
                    Text(
                        text = "Vehicle Tracking - ${selectedVehicle?.name ?: "No Vehicle Selected"}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White // Text color for the title.
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary) // Background color of the top app bar.
            )
        },
        floatingActionButton = {
            // Floating Action Button to toggle between map and list views.
            FloatingActionButton(
                onClick = { isMapView = !isMapView }, // Toggles the `isMapView` state.
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.secondary, // Background color of the FAB.
                contentColor = Color.White, // Icon color of the FAB.
                shape = RoundedCornerShape(50) // Circular FAB shape.
            ) {
                // Icon changes based on the current view (List for Map view, Map for List view).
                Icon(
                    imageVector = if (isMapView) Icons.Filled.List else Icons.Filled.Map,
                    contentDescription = if (isMapView) "Switch to List View" else "Switch to Map View"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End, // Position the FAB at the bottom end of the screen.
        content = { paddingValues ->
            // Column to hold either the map view or the list view.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Apply padding from the scaffold.
            ) {
                if (isMapView) {
                    // Google Map View
                    Box(modifier = Modifier.weight(1f)) { // Box takes available vertical space.
                        // Composable for rendering the Google Map.
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            onMapLoaded = {
                                // Once the map is loaded, move the camera to the selected vehicle's location.
                                selectedVehicle?.let {
                                    cameraPositionState.move(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(it.latitude, it.longitude),
                                            12f // Slightly higher zoom level for better detail.
                                        )
                                    )
                                }
                            }
                        ) {
                            // Iterate through all vehicles and add a Marker for each on the map.
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
                                        // When a marker is clicked, find the corresponding vehicle and set it as selected.
                                        selectedVehicle = vehicles.find { it.name == marker.title }
                                        false // Return false to allow the default info window to be shown.
                                    }
                                )
                            }
                        }

                        // Vehicle Detail Card (appears on top of the map when a vehicle is selected).
                        selectedVehicle?.let { vehicle ->
                            Card(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter) // Position the card at the bottom center of the Box.
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .background(Color.White, RoundedCornerShape(12.dp)) // White background with rounded corners.
                                    .wrapContentHeight(), // Height adjusts to content.
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // Card elevation for shadow.
                                shape = RoundedCornerShape(12.dp) // Rounded corners for the card.
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                ) {
                                    // Vehicle name and registration number.
                                    Text(
                                        text = "${vehicle.name} (${vehicle.regNumber})",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    // Display vehicle details using a helper composable.
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
                                    // Display alerts if any.
                                    if (vehicle.alerts > 0) {
                                        VehicleDetailRow(
                                            Icons.Filled.Warning,
                                            "Alerts: ${vehicle.alerts}",
                                            textColor = Color.Red // Red text for alerts.
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // List View
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(vehicles) { vehicle ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        // When a list item is clicked, set it as the selected vehicle and switch to map view.
                                        selectedVehicle = vehicle
                                        isMapView = true // Switch to map view when item is clicked
                                    },
                                elevation = CardDefaults.cardElevation(4.dp), // Card elevation.
                                shape = RoundedCornerShape(12.dp) // Rounded corners for the card.
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    // Display vehicle name and registration number in the list.
                                    Text(
                                        "${vehicle.name} - ${vehicle.regNumber}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    // Display make, model, and vehicle type.
                                    Text("${vehicle.make} ${vehicle.model} (${vehicle.vehicleType})")
                                    // Display latitude and longitude.
                                    Text("📍 ${vehicle.latitude}, ${vehicle.longitude}")
                                }
                            }
                        }
                    }
                }

                // Red note at the bottom for screen rotation.
                Text(
                    text = "Note: Rotate screen for a better view.",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp)
                        .wrapContentSize(Alignment.Center) // Center the text horizontally.
                )
            }
        }
    )
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
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    textColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp), // Vertical padding for each row.
        verticalAlignment = Alignment.CenterVertically // Vertically align items in the center.
    ) {
        // Icon for the detail row.
        Icon(
            imageVector = icon,
            contentDescription = null, // No content description needed for decorative icon.
            modifier = Modifier.size(24.dp), // Fixed size for the icon.
            tint = MaterialTheme.colorScheme.onSurfaceVariant // Use a suitable tint color from the theme.
        )
        Spacer(modifier = Modifier.width(8.dp)) // Spacer for horizontal spacing between icon and text.
        // Text content for the detail row.
        Text(text = text, fontSize = 16.sp, color = textColor)
    }
}
