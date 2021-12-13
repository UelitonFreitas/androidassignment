package com.adyen.android.assignment.userCases

import com.adyen.android.assignment.repository.model.Resource
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

abstract class PlacesUserCase {
    abstract fun getQueryFLow(): Flow<String>
    abstract fun getPlacesFlow(): Flow<Resource<List<Place>>>
    abstract fun setQuery(query: String)
}