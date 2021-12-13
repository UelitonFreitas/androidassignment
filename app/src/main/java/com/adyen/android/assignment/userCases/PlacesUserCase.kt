package com.adyen.android.assignment.userCases

import com.adyen.model.Resource
import com.adyen.model.Place
import kotlinx.coroutines.flow.Flow

abstract class PlacesUserCase {
    abstract fun getQueryFLow(): Flow<String>
    abstract fun getPlacesFlow(): Flow<Resource<List<Place>>>
    abstract fun setQuery(query: String)
}