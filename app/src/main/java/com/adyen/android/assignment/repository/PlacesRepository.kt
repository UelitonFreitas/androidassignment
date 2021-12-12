package com.adyen.android.assignment.repository

import com.adyen.android.assignment.model.Resource
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.Flow

abstract class PlacesRepository {

    abstract suspend fun getVenueRecommendations(location: Location)
    abstract fun getPlaceListFlow(): Flow<Resource<List<Place>>>
    abstract suspend fun getVenueRecommendationsFlow(location: Location): Flow<Resource<List<Place>>>

}