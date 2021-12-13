package com.adyen.repositories.places

import com.adyen.model.Location
import com.adyen.model.Place
import com.adyen.model.Resource
import kotlinx.coroutines.flow.Flow

abstract class PlacesRepository {

    abstract fun getPlaceListFlow(): Flow<Resource<List<Place>>>
    abstract fun getPlacesByLocationFlow(location: Location): Flow<Resource<List<Place>>>
    abstract fun getPlacesByQueryFlow(
        query: String,
        location: Location
    ): Flow<Resource<List<Place>>>

}