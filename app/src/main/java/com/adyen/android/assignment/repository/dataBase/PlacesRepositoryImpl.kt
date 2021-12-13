package com.adyen.android.assignment.repository.dataBase

import com.adyen.api.PlacesServicesApi
import com.adyen.dispatchers.DispatcherProvider
import com.adyen.model.Location
import com.adyen.model.Place
import com.adyen.model.Resource
import com.adyen.model.Status
import com.adyen.repositories.places.PlacesRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesServicesApi: PlacesServicesApi,
    private val placeDao: PlaceDao,
    private val dispatcherProvider: DispatcherProvider
) :
    PlacesRepository() {

    override fun getPlaceListFlow() = places

    val places = placeDao.getPlaces()
        .map { placesList -> Resource.success(placesList.map { Place(it.id, it.name) }) }

    override fun getPlacesByLocationFlow(location: Location): Flow<Resource<List<Place>>> =
        places.combine(placesServicesApi.getPlacesByLocationFlow(location)) { placesFromDb, placesFromApi ->
            when (placesFromApi.status) {
                Status.LOADING ->
                    Resource.loading(placesFromDb.data)
                Status.SUCCESS -> {
                    placesFromApi.data?.let { places ->
                        placeDao.insertPlaces(places.map {
                            com.adyen.android.assignment.repository.dataBase.model.Place(
                                it.id,
                                it.name
                            )
                        })
                    }
                    placesFromApi
                }
                Status.ERROR ->
                    Resource.error(placesFromDb.data)
            }
        }.flowOn(dispatcherProvider.io()).conflate()


    override fun getPlacesByQueryFlow(
        query: String,
        location: Location
    ): Flow<Resource<List<Place>>> =
        placesServicesApi.getPlacesByQueryFlow(query, location).onEach { queriedPlacesFromApi ->
            if (queriedPlacesFromApi.status == Status.SUCCESS) {
                queriedPlacesFromApi.data?.let { places ->
                    placeDao.insertPlaces(places.map {
                        com.adyen.android.assignment.repository.dataBase.model.Place(it.id, it.name)
                    })
                }
            }
        }.flowOn(dispatcherProvider.io())
}