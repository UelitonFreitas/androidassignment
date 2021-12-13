package com.adyen.android.assignment.userCases

import app.cash.turbine.test
import com.adyen.android.assignment.CoroutinesTestRule
import com.adyen.android.assignment.TestUtil.createPlaces
import com.adyen.model.Resource
import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepository
import com.adyen.model.Location
import com.adyen.model.Place
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class PlacesListUserCaseTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val placesRepository: PlacesRepository = mock()
    private val geolocationRepository: GeolocationRepository = mock()

    @Test
    fun `should load places for a location`() = coroutinesTestRule.testDispatcher.runBlockingTest {

        val location = Location(1.0, 1.0)
        val expectedPlaces = createPlaces(3)

        mockLocationRepositoryFlow(location)

        mockPlacesRepositoryFlowWithLocation(location, expectedPlaces)

        val userCase = PlacesUserCaseImpl(
            placesRepository,
            geolocationRepository
        )

        userCase.getPlacesFlow().test {
            assertEquals(Resource.success(expectedPlaces), awaitItem())
        }
    }

    @Test
    fun `should load queried places for a location`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {

            val query = "Coffee"
            val location = Location(1.0, 1.0)
            val expectedPlaces = createPlaces(3)

            mockLocationRepositoryFlow(location)

            mockPlacesRepositoryFlowQueryWithLocation(query.lowercase(), location, expectedPlaces)

            val userCase = PlacesUserCaseImpl(
                placesRepository,
                geolocationRepository
            )

            userCase.setQuery(query)

            userCase.getPlacesFlow().test {
                assertEquals(Resource.success(expectedPlaces), awaitItem())
            }
        }


    private suspend fun mockPlacesRepositoryFlow(places: List<Place> = createPlaces(3)) {
        val placeFlow = flow { emit(Resource.success(places)) }
        whenever(placesRepository.getPlaceListFlow()).thenReturn(placeFlow)
    }

    private fun mockPlacesRepositoryFlowWithLocation(
        location: Location,
        places: List<Place> = createPlaces(3)
    ) {
        val placeFlow = flow {
            emit(Resource.success(places))
        }
        whenever(placesRepository.getPlacesByLocationFlow(eq(location))).thenReturn(placeFlow)
    }

    private fun mockPlacesRepositoryFlowQueryWithLocation(
        query: String,
        location: Location,
        places: List<Place> = createPlaces(3)
    ) {
        val placeFlow = flow {
            emit(Resource.success(places))
        }

        whenever(placesRepository.getPlacesByQueryFlow(eq(query), eq(location))).thenReturn(
            placeFlow
        )
    }


    private fun mockLocationRepositoryFlow(location: Location) {
        val locationFlow = flow { emit(location) }
        whenever(geolocationRepository.fetchLocations()).thenReturn(locationFlow)
    }
}