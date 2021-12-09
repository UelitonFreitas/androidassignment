package com.adyen.android.assignment.ui.placesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlacesListViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _loadPlaces = MutableStateFlow(Unit)

    init {
        _loadPlaces.mapLatest {
            viewModelScope.launch(Dispatchers.IO) {
                val query = VenueRecommendationsQueryBuilder()
                    .setLatitudeLongitude(52.376510, 4.905890)
                    .build()
                placesRepository.getVenueRecommendations(query)
            }
        }.launchIn(viewModelScope)
    }

    fun loadPlaces() {
        _loadPlaces.value = Unit
    }

    val places: LiveData<List<Place>> = _loadPlaces.flatMapLatest { _ ->
        placesRepository.getPlaceListFlow()
    }.asLiveData()

}