package com.adyen.android.assignment.api

import com.adyen.android.assignment.model.Resource
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.Flow

interface PlacesServicesApi {
    suspend fun getVenueRecommendations(location: Location): List<Place>
    fun getVenueRecommendationsFlow(location: Location): Flow<Resource<List<Place>>>
}