package com.adyen.android.assignment.ui.placesList

import androidx.lifecycle.*
import com.adyen.android.assignment.repository.model.Place
import com.adyen.android.assignment.userCases.PlacesUserCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PlacesListViewModel @Inject constructor(
    private val placesUserCase: PlacesUserCase
) : ViewModel() {

    private val _loadPlaces = MutableStateFlow(Unit)

    val shouldShowSpinner = MutableLiveData(false)

    private val _snackbar = placesUserCase.errorMessage

    val snackbar: LiveData<String?>
        get() = _snackbar.asLiveData()

    init {

        _loadPlaces.mapLatest {
            placesUserCase.shouldLoadPlaces.value = Unit
        }.launchIn(viewModelScope)

        setupErrorMessage()

        placesUserCase.shouldShowLoading.onEach {
            shouldShowSpinner.value = it
        }.launchIn(viewModelScope)

    }

    private fun setupErrorMessage() {
        placesUserCase.errorMessage.mapLatest {
            _snackbar.value = it
        }.launchIn(viewModelScope)

        _snackbar.mapLatest {
            placesUserCase.errorMessage.value = it
        }.launchIn(viewModelScope)
    }

    fun loadPlaces() {
        _loadPlaces.value = Unit
    }

    val places: LiveData<List<Place>> = placesUserCase.placesFlow.asLiveData()

    fun onSnackbarShown() {
        _snackbar.value = null
    }
}