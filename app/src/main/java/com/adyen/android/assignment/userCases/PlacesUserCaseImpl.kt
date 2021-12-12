package com.adyen.android.assignment.userCases

import com.adyen.android.assignment.model.Resource
import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepository
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class PlacesUserCaseImpl @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val geolocationRepository: GeolocationRepository
) : PlacesUserCase() {

    override fun getPlacesFlow(): Flow<Resource<List<Place>>> =
        geolocationRepository.fetchLocations().flatMapLatest { location ->
            if (location != Location.none())
                placesRepository.getVenueRecommendationsFlow(location)
            else
                placesRepository.getPlaceListFlow()
        }
}