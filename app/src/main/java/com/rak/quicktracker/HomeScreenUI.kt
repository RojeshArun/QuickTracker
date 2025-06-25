package com.rak.quicktracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.WindowInsets // Import WindowInsets
import androidx.compose.foundation.layout.statusBars // Import statusBars
import androidx.compose.foundation.layout.asPaddingValues // Import asPaddingValues


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
@Composable
fun HomeScreenUI(navController: NavController) {
    // The main container box filling the entire screen with a light background.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
    ) {
        // Application Title Header at the very top
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter) // Align the header to the top
                // Add padding based on status bar height and then some extra margin
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF007BFF), shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 16.dp), // Adjust vertical padding for the title
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Autonomous Fleet Management System",
                    fontSize = 18.sp, // Slightly smaller font size
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp)) // Space between header and main content
        }


        // Column to arrange major UI elements vertically, centered horizontally.
        // This is your original main content column, now vertically centered.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Apply padding around the entire content column
                .align(Alignment.Center), // Keep the main content centered
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Header Card: "View Real-Time Fleet Map"
            // This card acts as a prominent button to navigate to the map screen.
            Card(
                shape = RoundedCornerShape(12.dp), // Rounded corners for a modern look
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // Shadow for depth
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp) // Vertical spacing from other elements
                    .clickable { navController.navigate("map") } // Navigate to "map" route on click
            ) {
                // Inner Box for the button's background color and content alignment
                Box(
                    modifier = Modifier
                        .background(Color(0xFF007BFF)) // Blue background color
                        .fillMaxWidth()
                        .padding(20.dp), // Internal padding for the text
                    contentAlignment = Alignment.Center // Center the text within the box
                ) {
                    Text(
                        text = "View Real-Time Fleet Map",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White // White text color
                    )
                }
            }

            // Fleet Summary Card
            // Displays an overview of fleet statistics like total vehicles, average speed, etc.
            Card(
                shape = RoundedCornerShape(12.dp), // Rounded corners for the card
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // Shadow for depth
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp) // Horizontal and vertical spacing
            ) {
                // Column for the card's internal content, including title and grid layout
                Column(modifier = Modifier.padding(16.dp)) {
                    // Title for the Fleet Summary section
                    Text(
                        text = "My Fleet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333), // Dark gray text color
                        modifier = Modifier.padding(bottom = 12.dp) // Spacing below the title
                    )

                    // Table-like Grid Layout: Arranges fleet information in two rows and two columns.
                    Column {
                        // First row of fleet information cells
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Total Vehicles cell
                            FleetInfoCell(
                                iconRes = android.R.drawable.ic_menu_myplaces, // Android built-in icon
                                label = "Total: 5",
                                tint = Color(0xFF007BFF), // Blue tint for the icon
                                modifier = Modifier.weight(1f) // Distributes space equally
                            )
                            // Average Speed cell
                            FleetInfoCell(
                                iconRes = android.R.drawable.ic_menu_compass, // Android built-in icon
                                label = "Avg Speed: 22.5",
                                tint = Color(0xFF17A2B8), // Teal tint for the icon
                                modifier = Modifier.weight(1f) // Distributes space equally
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp)) // Vertical space between rows

                        // Second row of fleet information cells
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Battery status cell
                            FleetInfoCell(
                                iconRes = android.R.drawable.ic_lock_idle_charging, // Android built-in icon
                                label = "Battery: 76%",
                                tint = Color(0xFF28A745), // Green tint for the icon
                                modifier = Modifier.weight(1f) // Distributes space equally
                            )
                            // Alerts count cell
                            FleetInfoCell(
                                iconRes = android.R.drawable.ic_dialog_alert, // Android built-in icon
                                label = "Alerts: 0",
                                tint = Color(0xFFDC3545), // Red tint for the icon
                                modifier = Modifier.weight(1f) // Distributes space equally
                            )
                        }
                    }
                }
            }

            // Control Buttons Row: Start, Stop, Optimize
            // Provides actions to control the fleet.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp), // Spacing around the row
                horizontalArrangement = Arrangement.SpaceBetween // Distribute buttons evenly with space between them
            ) {
                // Start Button
                Button(
                    onClick = { navController.navigate("map") }, // Navigate to map on Start click
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745)), // Green background
                    modifier = Modifier
                        .weight(1f) // Equal weight for button width
                        .padding(end = 6.dp) // Spacing between buttons
                ) {
                    Text("Start", color = Color.White) // White text
                }

                // Stop Button
                Button(
                    onClick = { /* TODO: Implement Stop functionality */ }, // Placeholder for Stop action
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC3545)), // Red background
                    modifier = Modifier
                        .weight(1f) // Equal weight for button width
                        .padding(end = 6.dp) // Spacing between buttons
                ) {
                    Text("Stop", color = Color.White) // White text
                }

                // Optimize Button
                Button(
                    onClick = { /* TODO: Implement Optimize functionality */ }, // Placeholder for Optimize action
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)), // Yellow background
                    modifier = Modifier.weight(1f) // Equal weight for button width
                ) {
                    Text("Optimize", color = Color.Black) // Black text
                }
            }

            // Register New Car Button
            // Button to navigate to the car registration screen.
            Button(
                onClick = { navController.navigate("car_screen") }, // Navigate to "car_screen" route on click
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F42C1)), // Purple background
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp) // Spacing around the button
            ) {
                Text("Add a Vehicle", color = Color.White) // White text
            }
        }
    }
}

/**
 * Composable function to display a single information cell within the fleet summary grid.
 * It combines an icon and a label.
 *
 * @param iconRes The resource ID of the icon to display (e.g., `android.R.drawable.ic_menu_myplaces`).
 * @param label The text label to display alongside the icon (e.g., "Total: 5").
 * @param tint The color to apply to the icon.
 * @param modifier The [Modifier] to be applied to this composable, allowing for custom layout
 * and padding.
 * @author Mulasa Rojesh Arun kumar
 */
@Composable
fun FleetInfoCell(iconRes: Int, label: String, tint: Color, modifier: Modifier = Modifier) {
    // Row to arrange the icon and text horizontally
    Row(
        verticalAlignment = Alignment.CenterVertically, // Align items vertically in the center
        modifier = modifier.padding(8.dp) // Padding around the cell content
    ) {
        // Icon element
        Icon(
            painter = painterResource(id = iconRes), // Load icon from resource ID
            contentDescription = label, // Content description for accessibility
            tint = tint, // Apply specified tint color
            modifier = Modifier
                .size(28.dp) // Set icon size
                .padding(end = 8.dp) // Spacing between icon and text
        )
        // Text label
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.DarkGray // Dark gray text color
        )
    }
}