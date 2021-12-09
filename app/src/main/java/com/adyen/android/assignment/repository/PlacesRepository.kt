package com.adyen.android.assignment.repository

import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.Flow

abstract class PlacesRepository {

    abstract suspend fun getVenueRecommendations(query: Map<String, String>)
    abstract fun getPlaceListFlow(): Flow<List<Place>>

}