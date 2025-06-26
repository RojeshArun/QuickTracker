package com.rak.quicktracker.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.*
import kotlin.random.Random

// Imports for the custom top bar functionality
import androidx.compose.foundation.layout.WindowInsets // Import WindowInsets
import androidx.compose.foundation.layout.statusBars // Import statusBars
import androidx.compose.foundation.layout.asPaddingValues // Import asPaddingValues
import androidx.compose.ui.graphics.RectangleShape // Import RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Helper to convert sp to dp within Composable scope.
 * This should be a top-level or static function within a Composable scope
 * to correctly access LocalDensity.current.
 */
@Composable
fun TextUnit.toDp(): Dp {
    val density = LocalDensity.current // Get density directly here
    return with(density) {
        this@toDp.toDp()
    }
}

/**
 * [CarScreen] is a Composable function that provides a UI for registering a new vehicle.
 * It includes input fields for VIN, Make, Model, Plate, Location, and Name, along with validation.
 *
 * @param navController The [NavController] used for navigating back to the previous screen.
 * @param onCarRegistered A callback function invoked when a car is successfully registered,
 * passing the [Car] object.
 *
 * @author Mulasa Rojesh Arun kumar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarScreen(
    navController: NavController,
    onCarRegistered: (Car) -> Unit = {}
) {
    // Obtain the current context, typically used for showing Toast messages.
    val context = LocalContext.current

    // State variables for VIN input field and its error message.
    var vin by remember { mutableStateOf("") }
    var vinError by remember { mutableStateOf<String?>(null) }

    // State variables for Make input field and its error message.
    var make by remember { mutableStateOf("") }
    var makeError by remember { mutableStateOf<String?>(null) }

    // State variables for Model input field and its error message.
    var model by remember { mutableStateOf("") }
    var modelError by remember { mutableStateOf<String?>(null) }

    // State variables for Plate input field and its error message.
    var plate by remember { mutableStateOf("") }
    var plateError by remember { mutableStateOf<String?>(null) }

    // State variables for Location input field and its error message.
    var location by remember { mutableStateOf("") }
    var locationError by remember { mutableStateOf<String?>(null) }

    // State variables for Name input field and its error message.
    var name by remember { mutableStateOf("") } // New state variable for Name
    var nameError by remember { mutableStateOf<String?>(null) } // New error state for Name


    // Define a background color, matching the home screen's background.
    val backgroundColor = Color(0xFFF4F6F8) // Match HomeScreen bg

    /**
     * Validates all input fields based on predefined rules.
     * Updates error messages for invalid fields.
     * @return True if all fields are valid, false otherwise.
     */
    fun validate(): Boolean {
        // Validate VIN: must not be blank, 17 characters long, and match the specified regex.
        vinError = when {
            vin.isBlank() -> "VIN is required"
            vin.length != 17 -> "VIN must be exactly 17 characters"
            !vin.matches(Regex("^[A-HJ-NPR-Z0-9]{17}\$")) -> "VIN invalid (no I,O,Q allowed)"
            else -> null
        }
        // Validate Make: must not be blank and contain only alphabetic characters.
        makeError = when {
            make.isBlank() -> "Make is required"
            !make.matches(Regex("^[A-Za-z]{2,}\$")) -> "Make must be alphabetic"
            else -> null
        }
        // Validate Model: must not be blank and match the specified regex.
        modelError = when {
            model.isBlank() -> "Model is required"
            !model.matches(Regex("^[A-Za-z0-9\\- ]+\$")) -> "Model invalid"
            else -> null
        }
        // Validate Plate: must not be blank and match one of the two specified plate formats.
        plateError = when {
            plate.isBlank() -> "Plate is required"
            !plate.matches(Regex("^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}\$")) &&
                    !plate.matches(Regex("^[A-Z]{3}-[0-9]{4}\$")) -> "Plate format invalid"

            else -> null
        }
        // Validate Location: must not be blank and match the specified regex.
        locationError = when {
            location.isBlank() -> "Location is required"
            !location.matches(Regex("^[A-Za-z ]{2,50}\$")) -> "Location invalid"
            else -> null
        }
        // Validate Name: must not be blank and contain only alphabetic characters and spaces.
        nameError = when {
            name.isBlank() -> "Name is required"
            !name.matches(Regex("^[A-Za-z ]{2,50}\$")) -> "Name invalid (only alphabets and spaces)"
            else -> null
        }
        // Return true if all error messages are null (i.e., no errors).
        return listOf(
            vinError,
            makeError,
            modelError,
            plateError,
            locationError,
            nameError
        ).all { it == null }
    }

    // Calculate the height of the custom top bar for content positioning
    // This now correctly calls the top-level toDp() extension function
    val customTopBarHeight = WindowInsets.statusBars.asPaddingValues()
        .calculateTopPadding() + 16.dp + 16.dp + 18.sp.toDp() // 18.sp is fontSize of custom title

    // The main container box filling the entire screen with a light background.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // Set the main Box background
    ) {
        // --- Custom Application Title Header at the very top ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Color(0xFF007BFF),
                    shape = RectangleShape
                ) // No rounded corners, full width
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp,
                    bottom = 16.dp // Keep vertical padding for text within the header
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
                .padding(top = customTopBarHeight), // Offset Scaffold below custom header
            containerColor = Color.Transparent, // Ensure Scaffold background is transparent so Box background shows
            topBar = {
                // Center aligned top app bar for the screen title.
                CenterAlignedTopAppBar(
                    title = { Text("Register New Vehicle", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent) // Make AppBar background transparent
                    // We specifically do NOT use `windowInsets` here, as the top-level Box handles status bar.
                )
            },
            content = { padding ->
                // Column to arrange the input fields and buttons vertically.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(padding), // Apply padding from the scaffold.
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between items.
                ) {
                    // Card to encapsulate the input fields, providing a material design look.
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Inner column within the card for input fields.
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {

                            // NEW: Name input field
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Name") },
                                isError = nameError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                                modifier = Modifier.fillMaxWidth()
                            )
                            // Display Name error message if present.
                            nameError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }
                            // VIN input field.
                            OutlinedTextField(
                                value = vin,
                                onValueChange = {
                                    vin = it.uppercase(Locale.getDefault())
                                }, // Convert to uppercase.
                                label = { Text("VIN") },
                                isError = vinError != null, // Show error if vinError is not null.
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Characters,
                                    keyboardType = KeyboardType.Ascii
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            // Display VIN error message if present.
                            vinError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }

                            // Make input field.
                            OutlinedTextField(
                                value = make,
                                onValueChange = { make = it },
                                label = { Text("Make") },
                                isError = makeError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                                modifier = Modifier.fillMaxWidth()
                            )
                            // Display Make error message if present.
                            makeError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }

                            // Model input field.
                            OutlinedTextField(
                                value = model,
                                onValueChange = { model = it },
                                label = { Text("Model") },
                                isError = modelError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                                modifier = Modifier.fillMaxWidth()
                            )
                            // Display Model error message if present.
                            modelError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }

                            // Plate input field.
                            OutlinedTextField(
                                value = plate,
                                onValueChange = {
                                    plate = it.uppercase(Locale.getDefault())
                                }, // Convert to uppercase.
                                label = { Text("Plate") },
                                isError = plateError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                                modifier = Modifier.fillMaxWidth()
                            )
                            // Display Plate error message if present.
                            plateError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }

                            // Location (Owner) input field.
                            OutlinedTextField(
                                value = location,
                                onValueChange = { location = it },
                                label = { Text("Location") },
                                isError = locationError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                                modifier = Modifier.fillMaxWidth()
                            )
                            // Display Location error message if present.
                            locationError?.let {
                                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }


                        }
                    }

                    // Row for action buttons: Register Vehicle and Random Fill.
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp), // Spacing between buttons.
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Register Vehicle button.
                        Button(
                            onClick = {
                                // Validate inputs before proceeding.
                                if (validate()) {
                                    // Create a Car object with the input data, including the new name field.
                                    val car = Car(vin, make, model, plate, location, name)
                                    // Invoke the onCarRegistered callback.
                                    onCarRegistered(car)
                                    // Show a success toast message.
                                    Toast.makeText(
                                        context,
                                        "Vehicle Registered",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Navigate back to the previous screen.
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier.weight(1f), // Make button fill available width.
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F42C1)) // Custom color.
                        ) {
                            Text("Register Vehicle", color = Color.White)
                        }

                        // Random Fill button.
                        Button(
                            onClick = {
                                // Generate random car data.
                                val randomCar = generateRandomCar()
                                // Populate input fields with random data, including the new name field.
                                vin = randomCar.vin
                                make = randomCar.make
                                model = randomCar.model
                                plate = randomCar.plate
                                location = randomCar.owner
                                name = randomCar.name // Populate the new name field
                            },
                            modifier = Modifier.weight(1f), // Make button fill available width.
                            colors = ButtonDefaults.outlinedButtonColors() // Outlined button style.
                        ) {
                            // Text("Random Fill")
                        }
                    }
                }
            }
        )
    }
}

/**
 * Generates a [Car] object with random data for demonstration or testing purposes.
 * @return A [Car] object with randomly generated VIN, make, model, plate, owner (location), and name.
 * @author Mulasa Rojesh Arun kumar
 */
fun generateRandomCar(): Car {
    // Predefined lists of car makes, models, cities, and names for random selection.
    val makes = listOf("Toyota", "Honda", "Ford", "BMW", "Hyundai", "Suzuki", "Kia", "Tesla")
    val models =
        listOf("Corolla", "Civic", "Mustang", "320i", "i20", "Swift", "Seltos", "Model 3")
    val cities = listOf(
        "Mumbai",
        "Delhi",
        "Bangalore",
        "Chennai",
        "Hyderabad",
        "Pune",
        "Vizag",
        "Nagoya"
    )
    val names = listOf(
        "Alice Smith", "Bob Johnson", "Charlie Brown", "Diana Prince", "Eva Green",
        "Frank White", "Grace Hopper", "Harry Potter"
    ) // New list of names

    // Characters allowed in a VIN.
    val vinChars = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789"
    // Generate a random 17-character VIN.
    val vin = (1..17).map { vinChars.random() }.joinToString("")
    // Select a random make and model.
    val make = makes.random()
    val model = models.random()
    // Generate a random license plate.
    val plate = generateRandomPlate()
    // Select a random city for the owner's location.
    val location = cities.random()
    // Select a random name.
    val name = names.random() // Select a random name
    // Return a new Car object with the generated data, including the new name.
    return Car(vin, make, model, plate, location, name)
}

/**
 * Generates a random license plate string in a specific format (e.g., "MH12AB1234").
 * @return A randomly generated license plate string.
 * @author Mulasa Rojesh Arun kumar
 */
fun generateRandomPlate(): String {
    val letters = ('A'..'Z').toList()
    // Generate two random letters for the state code.
    val state = "${letters.random()}${letters.random()}"
    // Generate a random two-digit code.
    val code = Random.nextInt(10, 99)
    // Generate two random letters for the series.
    val series = "${letters.random()}${letters.random()}"
    // Generate a random four-digit number.
    val number = Random.nextInt(1000, 9999)
    // Combine to form the full plate number.
    return "$state$code$series$number"
}

/**
 * Data class representing a Car with its details.
 *
 * @property vin The Vehicle Identification Number.
 * @property make The manufacturer of the car.
 * @property model The model of the car.
 * @property plate The license plate number of the car.
 * @property owner The name or location associated with the car's owner.
 * @property name The name of the person associated with the car. // NEW property
 * @author Mulasa Rojesh Arun kumar
 */
data class Car(
    val vin: String,
    val make: String,
    val model: String,
    val plate: String,
    val owner: String,
    val name: String, // NEW: Added name property
)
