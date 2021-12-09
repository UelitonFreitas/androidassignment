package com.adyen.android.assignment.repository

import com.adyen.android.assignment.api.PlacesServicesApi
import com.adyen.android.assignment.repository.dataBase.PlaceDao
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesServicesApi: PlacesServicesApi,
    private val placeDao: PlaceDao
) :
    PlacesRepository() {

    private val places: Flow<List<Place>> = placeDao.getPlaces()
        .map { placesList ->
            placesList.map { Place(it.name) }
        }


    override suspend fun getVenueRecommendations(query: Map<String, String>) {
        val places = placesServicesApi.getVenueRecommendations(query)
        placeDao.insertPlaces(places.map {
            com.adyen.android.assignment.repository.dataBase.model.Place(
                it.name
            )
        })
    }

    override fun getPlaceListFlow() = places
}