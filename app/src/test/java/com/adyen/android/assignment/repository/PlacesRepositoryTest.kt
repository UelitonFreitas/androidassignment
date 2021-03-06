package com.adyen.android.assignment.repository

import app.cash.turbine.test
import com.adyen.android.assignment.CoroutinesTestRule
import com.adyen.android.assignment.TestUtil.createDbPlace
import com.adyen.android.assignment.TestUtil.createDbPlaces
import com.adyen.android.assignment.TestUtil.createPlaces
import com.adyen.api.PlacesServicesApi
import com.adyen.android.assignment.repository.dataBase.PlaceDao
import com.adyen.model.Location
import com.adyen.model.Place
import com.adyen.model.Resource
import com.adyen.android.assignment.repository.dataBase.PlacesRepositoryImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*


@ExperimentalCoroutinesApi
class PlacesRepositoryTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val placesServicesApi: PlacesServicesApi = mock()
    private val placeDao: PlaceDao = mock()

    @Test
    fun `should load places from API and save on DB`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {

            val location = Location(1.0, 1.0)
            val expectedPlaces = createPlaces(4)

            val successApiResponse = Resource.success(expectedPlaces)
            mockServiceApi(successApiResponse, location)

            val placesRepo = PlacesRepositoryImpl(
                placesServicesApi,
                placeDao,
                coroutinesTestRule.testDispatcherProvider
            )

            placesRepo.getPlacesByLocationFlow(location).test {
                assertEquals(Resource.success(expectedPlaces), awaitItem())
                awaitComplete()
            }

            verify(placeDao).insertPlaces(eq(expectedPlaces.map { createDbPlace(it.id, it.name) }))
        }

    @Test
    fun `should NOT save places on DB when resource places loaded from api is NOT success`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {

            val location = Location(1.0, 1.0)
            val query = "Coffee"
            val expectedPlaces = createPlaces(4)

            val successApiResponse = Resource.error(null)
            mockQueriedServiceApi(query, successApiResponse, location)

            val placesRepo = PlacesRepositoryImpl(
                placesServicesApi,
                placeDao,
                coroutinesTestRule.testDispatcherProvider
            )

            placesRepo.getPlacesByQueryFlow(query, location).test {
                assertEquals(Resource.error(null), awaitItem())
                awaitComplete()
            }

            verify(placeDao, never()).insertPlaces(eq(expectedPlaces.map {
                createDbPlace(
                    it.id,
                    it.name
                )
            }))
        }

    private fun mockServiceApi(
        loadingApiResponse: Resource<List<Place>>,
        location: Location
    ) {
        val apiFlow = flow {
            emit(loadingApiResponse)
        }
        whenever(placesServicesApi.getPlacesByLocationFlow(eq(location))).thenReturn(
            apiFlow
        )
    }

    private fun mockQueriedServiceApi(
        query: String,
        loadingApiResponse: Resource<List<Place>>,
        location: Location
    ) {
        val apiFlow = flow {
            emit(loadingApiResponse)
        }
        whenever(placesServicesApi.getPlacesByQueryFlow(eq(query), eq(location))).thenReturn(
            apiFlow
        )
    }

    private fun mockPlacesDao(placesFromDb: List<com.adyen.android.assignment.repository.dataBase.model.Place>) {
        val placeFlow = flow { emit(placesFromDb) }
        whenever(placeDao.getPlaces()).thenReturn(
            placeFlow
        )
    }
}