package com.adyen.android.assignment.repository.geolocalization

import com.adyen.android.assignment.repository.geolocalization.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeolocationRepositoryImpl @Inject constructor() : GeolocationRepository {

    override fun fetchLocations(): Flow<Location> = flow {
        emit(Location(52.376510, 4.905890))
    }
}