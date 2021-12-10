package com.adyen.android.assignment.repository.geolocalization

import com.adyen.android.assignment.repository.geolocalization.model.Location
import kotlinx.coroutines.flow.Flow

interface GeolocationRepository {
    fun fetchLocations(): Flow<Location>
}