package com.adyen.repositories.geolocation

import com.adyen.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeolocationRepositoryImpl @Inject constructor() : GeolocationRepository {

    override fun fetchLocations(): Flow<Location> = flow {
        emit(Location(52.376510, 4.905890))
    }
}