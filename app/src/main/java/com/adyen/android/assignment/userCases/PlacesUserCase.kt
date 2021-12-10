package com.adyen.android.assignment.userCases

import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepository
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PlacesUserCase @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val geolocationRepository: GeolocationRepository,
    private val localScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    val shouldLoadPlaces = MutableStateFlow(Unit)

    val shouldShowLoading = MutableStateFlow(false)

    val errorMessage = MutableStateFlow<String?>(null)

    private val currentLocation = MutableStateFlow(Location(-1.0, -1.0))

    val placesFlow: Flow<List<Place>>
        get() = placesRepository.getPlaceListFlow()

    init {

        loadDataFor(shouldLoadPlaces) {
            placesRepository.getVenueRecommendations(currentLocation.value)
        }

        geolocationRepository.fetchLocations().mapLatest {
            currentLocation.value = it
        }.launchIn(localScope)

        loadDataFor(currentLocation) {
            placesRepository.getVenueRecommendations(it)
        }
    }

    private fun <T> loadDataFor(source: StateFlow<T>, block: suspend (T) -> Unit) {
        source.mapLatest { data ->
            shouldShowLoading.value = true
            block(data)
        }.onEach {
            shouldShowLoading.value = false
        }.catch { throwable ->
            errorMessage.value = throwable.message
            shouldShowLoading.value = false
        }.launchIn(localScope)
    }
}