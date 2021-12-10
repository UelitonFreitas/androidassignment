package com.adyen.android.assignment.repository

import com.adyen.android.assignment.api.PlacesServicesApi
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.repository.dataBase.PlaceDao
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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


    override suspend fun getVenueRecommendations(location: Location) =
        withContext(Dispatchers.IO) {
            val places = placesServicesApi.getVenueRecommendations(location)
            placeDao.insertPlaces(places.map {
                com.adyen.android.assignment.repository.dataBase.model.Place(
                    it.name
                )
            })
        }

    override fun getPlaceListFlow() = places
}