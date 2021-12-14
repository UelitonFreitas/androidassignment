package com.adyen.android.assignment.userCases

import com.adyen.repositories.places.PlacesRepository
import com.adyen.repositories.geolocation.GeolocationRepository
import com.adyen.model.Location
import com.adyen.model.Place
import com.adyen.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import java.util.*
import javax.inject.Inject

class PlacesUserCaseImpl @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val geolocationRepository: GeolocationRepository
) : PlacesUserCase() {


    private var query = MutableStateFlow("")

    override fun getQueryFLow(): Flow<String> = query

    private val searchParametersFlow: Flow<SearchEntry> =
        this.query.combine(geolocationRepository.fetchLocations()) { query, location ->
            Pair(query, location)
        }

    override fun getPlacesFlow(): Flow<Resource<List<Place>>> =
        searchParametersFlow.flatMapLatest { searchEntry ->
            val location = searchEntry.second
            val query = searchEntry.first
            when {
                query.isEmpty() -> {
                    placesRepository.getPlacesByLocationFlow(location)
                }
                else -> placesRepository.getPlacesByQueryFlow(query, location)
            }
        }

    override fun setQuery(originalInput: String) {
        query.value = originalInput.lowercase(Locale.getDefault()).trim()
    }
}

typealias SearchEntry = Pair<String, Location>