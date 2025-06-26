// Package declaration for the maps feature of the QuickTracker application.
package com.rak.quicktracker.maps

import android.content.Context
import com.rak.quicktracker.R

/**
 * Reads vehicle data from a raw resource file named `automobile.txt`.
 * Each line in the file is expected to represent a vehicle's data, with fields
 * separated by commas.
 *
 * The expected format for each line is:
 * `name,gpsDeviceId,latitude,longitude,regNumber,make,model,vehicleType,timestamp`
 *
 * Malformed lines or lines with an incorrect number of parts will be skipped,
 * and an error message will be printed to the console.
 *
 * @param context The [Context] used to access application resources (e.g., `R.raw.automobile`).
 * @return A [List] of [VehicleData] objects parsed from the raw resource file.
 *
 * @author Mulasa Rojesh Arun kumar
 */
fun readVehiclesFromRaw(context: Context): List<VehicleData> {
    // Open the raw resource file 'automobile.txt' as an InputStream.
    val inputStream = context.resources.openRawResource(R.raw.automobile)
    // Use `bufferedReader()` to efficiently read lines and `useLines` to ensure the reader is closed.
    return inputStream.bufferedReader().useLines { lines ->
        // Process each line from the input stream.
        lines.mapNotNull { line ->
            // Split the line by comma to get individual data parts.
            val parts = line.split(",")
            // Check if the line has the expected number of parts (9).
            if (parts.size == 12) { // Expecting 12 parts: name, gpsDeviceId, latitude, longitude, regNumber, make, model, vehicleType, timestamp, speed, battery, alerts
                try {
                    // Create a VehicleData object from the parsed parts.
                    VehicleData(
                        name = parts[0].trim(), // Trim whitespace from the name.
                        gpsDeviceId = parts[1].trim(), // GPS Device ID (maps to 'id' in previous versions).
                        latitude = parts[2].trim().toDouble(), // Convert latitude string to Double.
                        longitude = parts[3].trim().toDouble(), // Convert longitude string to Double.
                        regNumber = parts[4].trim(), // Registration number.
                        make = parts[5].trim(), // Vehicle make.
                        model = parts[6].trim(), // Vehicle model.
                        vehicleType = parts[7].trim(), // Vehicle type.
                        timestamp = parts[8].trim(), // Timestamp of the data.
                        speed = parts[9].trim().toDouble(),// Speed
                        battery = parts[10].trim().toInt(),
                        alerts= 1
                    )
                } catch (e: Exception) {
                    // Catch any parsing errors (e.g., NumberFormatException for latitude/longitude).
                    // Log the error message for debugging purposes.
                    println("Error parsing line: $line - ${e.message}")
                    null // Return null to exclude this malformed line from the list.
                }
            } else {
                // If the line does not have 9 parts, it's considered malformed.
                println("Skipping malformed line (expected 9 parts): $line")
                null // Return null to exclude this malformed line from the list.
            }
        }.toList() // Convert the sequence of VehicleData (and nulls) to a List, filtering out nulls.
    }
}