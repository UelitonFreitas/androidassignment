package com.adyen.android.assignment.userCases

import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

abstract class PlacesUserCase {

    val shouldShowLoading = MutableStateFlow(false)

    val errorMessage = MutableStateFlow<String?>(null)

    val currentLocation = MutableStateFlow(Location.none())

    abstract fun getPlacesFlow(): Flow<List<Place>>

    abstract suspend fun loadPlaces()
}