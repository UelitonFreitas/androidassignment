package com.adyen.api

import com.adyen.model.Location
import com.adyen.model.Place
import com.adyen.model.Resource
import kotlinx.coroutines.flow.Flow

interface PlacesServicesApi {
    fun getPlacesByLocationFlow(location: Location): Flow<Resource<List<Place>>>
    fun getPlacesByQueryFlow(query: String, location: Location): Flow<Resource<List<Place>>>
}