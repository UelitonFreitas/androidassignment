package com.adyen.android.assignment.ui.placesList

import androidx.lifecycle.*
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepository
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PlacesListViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val geolocationRepository: GeolocationRepository
) : ViewModel() {

    private val _loadPlaces = MutableStateFlow(Unit)

    private val _shouldShowSpinner = MutableLiveData(false)

    val shouldShowSpinner: LiveData<Boolean>
        get() = _shouldShowSpinner

    private val _snackbar = MutableLiveData<String?>()

    val snackbar: LiveData<String?>
        get() = _snackbar

    private val _location = MutableStateFlow(Location(-1.0, -1.0))

    init {

        loadDataFor(_loadPlaces) {
            placesRepository.getVenueRecommendations(_location.value)
        }

        geolocationRepository.fetchLocations().mapLatest {
            _location.value = it
        }.launchIn(viewModelScope)

        loadDataFor(_location) {
            placesRepository.getVenueRecommendations(it)
        }
    }

    fun loadPlaces() {
        _loadPlaces.value = Unit
    }

    val places: LiveData<List<Place>> = placesRepository.getPlaceListFlow().asLiveData()

    fun onSnackbarShown() {
        _snackbar.value = null
    }

    private fun <T> loadDataFor(source: StateFlow<T>, block: suspend (T) -> Unit) {
        source.mapLatest { data ->
            _shouldShowSpinner.value = true
            block(data)
        }.onEach { _shouldShowSpinner.value = false }
            .catch { throwable -> _snackbar.value = throwable.message }
            .launchIn(viewModelScope)
    }
}