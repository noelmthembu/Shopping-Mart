package com.example.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.data.model.Store
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlin.math.roundToInt

data class UserLocation(
    val latitude: Double,
    val longitude: Double,
    val isSimulated: Boolean = false,
    val accuracyMeters: Float = 10f,
    val areaName: String = "",
    val isConfigured: Boolean = false
)

data class SouthAfricanAreaPreset(
    val name: String,
    val cityOrRegion: String,
    val latitude: Double,
    val longitude: Double
)

data class StoreWithDistance(
    val store: Store,
    val distanceKm: Double,
    val distanceFormatted: String,
    val isNearby: Boolean
)

class LocationHelper(private val context: Context) {
    private val fusedClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    companion object {
        val SA_POPULAR_PRESETS = listOf(
            SouthAfricanAreaPreset("Sandton City", "Johannesburg, Gauteng", -26.1076, 28.0567),
            SouthAfricanAreaPreset("Rosebank Mall", "Johannesburg, Gauteng", -26.1465, 28.0416),
            SouthAfricanAreaPreset("V&A Waterfront", "Cape Town, Western Cape", -33.9048, 18.4215),
            SouthAfricanAreaPreset("Menlyn Park", "Pretoria, Gauteng", -25.7824, 28.2758),
            SouthAfricanAreaPreset("Canal Walk / Century City", "Cape Town, Western Cape", -33.8924, 18.5118),
            SouthAfricanAreaPreset("Gateway Theatre of Shopping", "Umhlanga, Durban", -29.7262, 31.0667),
            SouthAfricanAreaPreset("Mall of Africa", "Midrand, Gauteng", -26.0154, 28.1065),
            SouthAfricanAreaPreset("Maponya Mall", "Soweto, Gauteng", -26.2736, 27.8922),
            SouthAfricanAreaPreset("Hyde Park Corner", "Johannesburg, Gauteng", -26.1260, 28.0380),
            SouthAfricanAreaPreset("Fourways Mall", "Johannesburg, Gauteng", -26.0245, 28.0089),
            SouthAfricanAreaPreset("Cresta Shopping Centre", "Randburg, Gauteng", -26.1444, 27.9719),
            SouthAfricanAreaPreset("Brooklyn Mall", "Pretoria, Gauteng", -25.7681, 28.2372),
            SouthAfricanAreaPreset("Sea Point Promenade", "Cape Town, Western Cape", -33.9213, 18.3847),
            SouthAfricanAreaPreset("Umhlanga Ridge", "Durban, KwaZulu-Natal", -29.7285, 31.0792),
            SouthAfricanAreaPreset("Claremont", "Cape Town, Western Cape", -33.9823, 18.4687),
            SouthAfricanAreaPreset("Centurion Mall", "Centurion, Gauteng", -25.8521, 28.1856),
            SouthAfricanAreaPreset("Melville", "Johannesburg, Gauteng", -26.1741, 28.0094),
            SouthAfricanAreaPreset("Bedfordcentre", "Bedfordview, Gauteng", -26.1834, 28.1345)
        )
    }

    private val _currentLocation = MutableStateFlow(
        UserLocation(
            latitude = -26.1076,
            longitude = 28.0567,
            isSimulated = true,
            areaName = "",
            isConfigured = false // Starts NOT configured as requested
        )
    )
    val currentLocation: StateFlow<UserLocation> = _currentLocation.asStateFlow()

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    suspend fun fetchCurrentLocation(): UserLocation {
        if (!hasLocationPermission()) {
            return _currentLocation.value
        }

        return try {
            val cts = CancellationTokenSource()
            val location: Location? = fusedClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cts.token
            ).await()

            if (location != null) {
                val loc = UserLocation(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    isSimulated = false,
                    accuracyMeters = location.accuracy,
                    areaName = "Current GPS Location",
                    isConfigured = true
                )
                _currentLocation.value = loc
                loc
            } else {
                _currentLocation.value
            }
        } catch (e: Exception) {
            _currentLocation.value
        }
    }

    fun setLocation(lat: Double, lng: Double, area: String) {
        _currentLocation.value = UserLocation(
            latitude = lat,
            longitude = lng,
            isSimulated = true,
            areaName = area,
            isConfigured = true
        )
    }

    fun calculateStoreDistances(stores: List<Store>, userLoc: UserLocation): List<StoreWithDistance> {
        val results = FloatArray(1)
        return stores.map { store ->
            Location.distanceBetween(
                userLoc.latitude,
                userLoc.longitude,
                store.latitude,
                store.longitude,
                results
            )
            val meters = results[0].toDouble()
            val km = meters / 1000.0
            val formatted = if (meters < 1000) {
                "${meters.roundToInt()} m away"
            } else {
                String.format("%.1f km away", km)
            }
            StoreWithDistance(
                store = store,
                distanceKm = km,
                distanceFormatted = formatted,
                isNearby = km <= 3.0
            )
        }.sortedBy { it.distanceKm }
    }
}
