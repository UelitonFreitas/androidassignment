package com.adyen.android.assignment.ui.placesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.adyen.android.assignment.model.Status
import com.adyen.android.assignment.repository.model.Place
import com.adyen.android.assignment.userCases.PlacesUserCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class PlacesListViewModel @Inject constructor(
    private val placesUserCase: PlacesUserCase
) : ViewModel() {

    private val _loadPlaces = MutableStateFlow(Unit)

    val shouldShowSpinner = MutableLiveData(false)

    private val _snackbar = MutableStateFlow<String?>(null)

    val snackbar: LiveData<String?>
        get() = _snackbar.asLiveData()

    fun loadPlaces() {
        _loadPlaces.value = Unit
    }

    val places: LiveData<List<Place>> = placesUserCase.getPlacesFlow().mapLatest { resource ->
        when (resource.status) {
            Status.LOADING -> {
                shouldShowSpinner.value = true
            }
            Status.SUCCESS -> {
                shouldShowSpinner.value = false
            }
            else -> {
                shouldShowSpinner.value = false
                _snackbar.value = "Error on load Places from API"
            }
        }
        resource.data ?: emptyList()
    }.asLiveData()

    fun onSnackbarShown() {
        _snackbar.value = null
    }
}