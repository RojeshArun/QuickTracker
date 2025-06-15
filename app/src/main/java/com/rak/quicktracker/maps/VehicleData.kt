// Package declaration for the maps feature of the QuickTracker application.
package com.rak.quicktracker.maps

// Data class to represent vehicle information.
/**
 * [VehicleData] is a data class that holds comprehensive information about a single vehicle.
 * It is used to parse and store vehicle details, typically loaded from a data source
 * like a raw resource file or API response.
 *
 * @property name The common name or identifier for the vehicle (e.g., "Vehicle 1").
 * @property gpsDeviceId The unique identifier for the GPS device installed in the vehicle.
 * @property latitude The current latitude coordinate of the vehicle's location.
 * @property longitude The current longitude coordinate of the vehicle's location.
 * @property regNumber The registration number (license plate) of the vehicle.
 * @property make The manufacturer of the vehicle (e.g., "Toyota", "Ford").
 * @property model The specific model of the vehicle (e.g., "Camry", "F-150").
 * @property vehicleType The type of vehicle (e.g., "Car", "Truck", "Motorcycle").
 * @property timestamp The timestamp indicating when this vehicle data was recorded, in String format.
 * @property battery The current battery percentage of the vehicle's tracking device, defaults to 0 if not provided.
 * @property speed The current speed of the vehicle in km/h, defaults to 0.0 if not provided.
 * @property alerts The number of active alerts associated with the vehicle, defaults to 0 if not provided.
 *
 * @author Mulasa Rojesh Arun kumar
 */
data class VehicleData(
    val name: String,
    val gpsDeviceId: String, // Renamed from 'id' to match new raw data for clarity and consistency.
    val latitude: Double,
    val longitude: Double,
    val regNumber: String,
    val make: String,
    val model: String,
    val vehicleType: String,
    val timestamp: String, // Added timestamp property to store when the data was recorded.
    val battery: Int = 0,    // Retained with a default value of 0, as this field might not always be present in raw data.
    val speed: Double = 0.0, // Retained with a default value of 0.0, as this field might not always be present in raw data.
    val alerts: Int = 0      // Retained with a default value of 0, as this field might not always be present in raw data.
)