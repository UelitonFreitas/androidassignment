package com.adyen.android.assignment.ui.placesList

import androidx.lifecycle.*
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlacesListViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _loadPlaces = MutableStateFlow(Unit)

    private val _shouldShowSpinner = MutableLiveData(false)

    val shouldShowSpinner: LiveData<Boolean>
        get() = _shouldShowSpinner

    private val _snackbar = MutableLiveData<String?>()

    val snackbar: LiveData<String?>
        get() = _snackbar

    init {
        _loadPlaces.mapLatest {

            _shouldShowSpinner.value = true
            
                val query = VenueRecommendationsQueryBuilder()
                    .setLatitudeLongitude(52.376510, 4.905890)
                    .build()
                placesRepository.getVenueRecommendations(query)
        }.onEach {
            _shouldShowSpinner.value = false
        }.catch { throwable ->
            _snackbar.value = throwable.message
        }.launchIn(viewModelScope)
    }

    fun loadPlaces() {
        _loadPlaces.value = Unit
    }

    val places: LiveData<List<Place>> = _loadPlaces.flatMapLatest { _ ->
        _shouldShowSpinner.value = true
        placesRepository.getPlaceListFlow()
    }.onEach {
        _shouldShowSpinner.value = false
    }.asLiveData()

    fun onSnackbarShown() {
        _snackbar.value = null
    }
}