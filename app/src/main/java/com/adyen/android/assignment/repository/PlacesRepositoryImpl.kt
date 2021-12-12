package com.adyen.android.assignment.repository

import com.adyen.android.assignment.api.PlacesServicesApi
import com.adyen.android.assignment.dispatchers.DispatcherProvider
import com.adyen.android.assignment.model.Resource
import com.adyen.android.assignment.model.Status
import com.adyen.android.assignment.repository.dataBase.PlaceDao
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesServicesApi: PlacesServicesApi,
    private val placeDao: PlaceDao,
    private val dispatcherProvider: DispatcherProvider
) :
    PlacesRepository() {

    override suspend fun getVenueRecommendations(location: Location) =
        withContext(dispatcherProvider.io()) {
            val places = placesServicesApi.getVenueRecommendations(location)
            placeDao.insertPlaces(places.map {
                com.adyen.android.assignment.repository.dataBase.model.Place(
                    it.name
                )
            })
        }

    override fun getPlaceListFlow() = places

    val places = placeDao.getPlaces()
        .map { placesList ->
            Resource.success(placesList.map { Place(it.name) })
        }

    override suspend fun getVenueRecommendationsFlow(location: Location): Flow<Resource<List<Place>>> =
        places.combine(placesServicesApi.getVenueRecommendationsFlow(location)) { placesFromDb, placesFromApi ->
            when (placesFromApi.status) {
                Status.LOADING ->
                    Resource.loading(placesFromDb.data)
                Status.SUCCESS -> {
                    placesFromApi.data?.let { places ->
                        placeDao.insertPlaces(places.map {
                            com.adyen.android.assignment.repository.dataBase.model.Place(it.name)
                        })
                    }
                    placesFromDb
                }
                Status.ERROR ->
                    Resource.error(placesFromDb.data)
            }
        }.flowOn(dispatcherProvider.io()).conflate()
}