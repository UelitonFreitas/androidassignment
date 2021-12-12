package com.adyen.android.assignment.api

import com.adyen.android.assignment.repository.model.Resource
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.Flow

interface PlacesServicesApi {
    fun getPlacesByLocationFlow(location: Location): Flow<Resource<List<Place>>>
    fun getPlacesByQueryFlow(query: String, location: Location): Flow<Resource<List<Place>>>
}