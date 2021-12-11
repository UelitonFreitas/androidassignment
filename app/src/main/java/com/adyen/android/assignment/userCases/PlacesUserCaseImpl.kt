package com.adyen.android.assignment.userCases

import com.adyen.android.assignment.dispatchers.DispatcherProvider
import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepository
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PlacesUserCaseImpl @Inject constructor(
    private val placesRepository: PlacesRepository,
    geolocationRepository: GeolocationRepository,
    dispatcherProvider: DispatcherProvider
) : PlacesUserCase() {

    private val shouldLoadPlaces = MutableStateFlow(false)

    private val scope = CoroutineScope(dispatcherProvider.io())

    init {

        loadDataFor(shouldLoadPlaces) {
            if (currentLocation.value != Location.none())
                placesRepository.getVenueRecommendations(currentLocation.value)
        }

        geolocationRepository.fetchLocations().mapLatest {
            currentLocation.value = it
        }.launchIn(scope)

        loadDataFor(currentLocation) {
            if (currentLocation.value != Location.none())
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
        }.launchIn(scope)
    }

    override fun getPlacesFlow(): Flow<List<Place>> = placesRepository.getPlaceListFlow()

    override suspend fun loadPlaces() {
        with(shouldLoadPlaces) {
            value = value.not()
        }
    }
}