package com.adyen.android.assignment.userCases

import com.adyen.android.assignment.model.Resource
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

abstract class PlacesUserCase {
    abstract fun getPlacesFlow(): Flow<Resource<List<Place>>>
}