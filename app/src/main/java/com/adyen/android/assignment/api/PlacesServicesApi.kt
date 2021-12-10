package com.adyen.android.assignment.api

import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place

interface PlacesServicesApi {
    suspend fun getVenueRecommendations(location: Location): List<Place>
}