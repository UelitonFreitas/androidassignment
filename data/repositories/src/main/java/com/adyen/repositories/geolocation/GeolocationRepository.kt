package com.adyen.repositories.geolocation

import com.adyen.model.Location
import kotlinx.coroutines.flow.Flow

interface GeolocationRepository {
    fun fetchLocations(): Flow<Location>
}