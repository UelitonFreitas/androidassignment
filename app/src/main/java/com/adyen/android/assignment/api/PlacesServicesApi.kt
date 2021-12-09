package com.adyen.android.assignment.api

import com.adyen.android.assignment.repository.model.Place

interface PlacesServicesApi {
    suspend fun getVenueRecommendations(query: Map<String, String>): List<Place>
}