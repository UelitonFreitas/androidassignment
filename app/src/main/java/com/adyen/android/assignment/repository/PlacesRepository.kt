package com.adyen.android.assignment.repository

import com.adyen.android.assignment.repository.model.Resource
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.Flow

abstract class PlacesRepository {

    abstract fun getPlaceListFlow(): Flow<Resource<List<Place>>>
    abstract fun getPlacesByLocationFlow(location: Location): Flow<Resource<List<Place>>>
    abstract fun getPlacesByQueryFlow(
        query: String,
        location: Location
    ): Flow<Resource<List<Place>>>

}