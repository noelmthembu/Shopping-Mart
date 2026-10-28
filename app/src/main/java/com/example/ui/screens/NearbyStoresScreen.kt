package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.ShoppingViewModel
import com.example.ui.components.LocationSetupCard
import com.example.ui.components.StoreCard
import com.example.location.LocationHelper

@Composable
fun NearbyStoresScreen(
    viewModel: ShoppingViewModel,
    modifier: Modifier = Modifier
) {
    val storesWithDistance by viewModel.storesWithDistance.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()
    val downloadingIds by viewModel.downloadingStoreIds.collectAsState()
    val nearbyStores = storesWithDistance.filter { it.isNearby }

    // Location Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                      permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.detectDeviceLocation()
        }
    }

    // Notification Permission launcher for Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Step 1: If location is not configured, show LocationSetupCard
        if (!userLocation.isConfigured) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                LocationSetupCard(
                    onLocationSelected = { lat, lng, area ->
                        viewModel.setUserLocation(lat, lng, area)
                    },
                    onDetectGps = {
                        viewModel.detectDeviceLocation()
                    }
                )
            }
        } else {
            // Location & GPS Status Card for South Africa
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "GPS Location",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Nearby Stores (South Africa)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = if (userLocation.isSimulated) {
                                        "${userLocation.areaName} • area estimate"
                                    } else {
                                        "${userLocation.areaName} • GPS accuracy ${userLocation.accuracyMeters.toInt()} m"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                if (!viewModel.locationHelper.hasLocationPermission()) {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                } else {
                                    viewModel.detectDeviceLocation()
                                }
                            },
                            modifier = Modifier.testTag("refresh_location_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh GPS",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // South African Location Simulation Presets
                    Text(
                        text = "Switch Location Area:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.setUserLocation(-26.1076, 28.0567, "Sandton City, JHB")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("simulate_sandton_button"),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                        ) {
                            Text("Sandton", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.setUserLocation(-26.1465, 28.0416, "Rosebank Mall, JHB")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("simulate_rosebank_button"),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                        ) {
                            Text("Rosebank", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.setUserLocation(-33.9048, 18.4215, "V&A Waterfront, CT")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("simulate_capetown_button"),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                        ) {
                            Text("Cape Town", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("request_gps_button"),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                        ) {
                            Icon(Icons.Default.GpsFixed, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("My GPS", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

        // Notification Prompt Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF3E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = Color(0xFFE65100),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Sample store alerts",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Demo notifications only. Store and offer data is sample content; confirm details with the retailer.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        !viewModel.notificationHelper.hasNotificationPermission()
                    ) {
                        Button(
                            onClick = {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            },
                            modifier = Modifier.testTag("enable_notifications_button")
                        ) {
                            Text("Enable", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Stores sorted by distance (kilometers)
        item {
            Text(
                text = "Stores within ${LocationHelper.NEARBY_RADIUS_KM.toInt()} km",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (userLocation.isConfigured && nearbyStores.isEmpty()) {
            item {
                Text(
                    text = "No sample stores are listed within ${LocationHelper.NEARBY_RADIUS_KM.toInt()} km of this location.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        items(nearbyStores, key = { it.store.id }) { item ->
            StoreCard(
                store = item.store,
                distanceFormatted = if (userLocation.isConfigured) item.distanceFormatted else null,
                isDownloading = downloadingIds.contains(item.store.id),
                onDownload = { viewModel.downloadStore(item.store.id, openAfterDownload = true) },
                onRemoveDownload = { viewModel.removeStoreDownload(item.store.id) },
                onOpenCatalogue = {
                    viewModel.selectStoreForCatalogue(item.store.id)
                    viewModel.navigateTo(AppScreen.STORE_CATALOGUES)
                },
                onTriggerDealAlert = {
                    viewModel.triggerNearbyPromotionNotification(item.store)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
