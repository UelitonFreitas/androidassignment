package com.adyen.android.assignment.ui

import app.cash.turbine.test
import com.adyen.android.assignment.CoroutinesTestRule
import com.adyen.android.assignment.model.Resource
import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepository
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import com.adyen.android.assignment.userCases.PlacesUserCaseImpl
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
    fun `should not load places without location`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {

            val locationFlow = flow<Location> { }
            whenever(geolocationRepository.fetchLocations()).thenReturn(locationFlow)

            val placeFlow = flow<Resource<List<Place>>> { }
            whenever(placesRepository.getPlaceListFlow()).thenReturn(placeFlow)

            val userCase = PlacesUserCaseImpl(
                placesRepository,
                geolocationRepository
            )

            verify(placesRepository, never()).getVenueRecommendations(any())

            userCase.getPlacesFlow().test { awaitComplete() }
        }

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
            awaitComplete()
        }
    }

    private suspend fun mockPlacesRepositoryFlow(places: List<Place> = createPlaces(3)) {
        val placeFlow = flow { emit(Resource.success(places)) }
        whenever(placesRepository.getPlaceListFlow()).thenReturn(placeFlow)
    }

    private suspend fun mockPlacesRepositoryFlowWithLocation(
        location: Location,
        places: List<Place> = createPlaces(3)
    ) {
        val placeFlow = flow {
            emit(Resource.success(places))
        }
        whenever(placesRepository.getVenueRecommendationsFlow(eq(location))).thenReturn(placeFlow)
    }


    private fun mockLocationRepositoryFlow(location: Location) {
        val locationFlow = flow { emit(location) }
        whenever(geolocationRepository.fetchLocations()).thenReturn(locationFlow)
    }

    private fun createPlace(name: String) = Place(name)

    private fun createPlaces(amount: Int) =
        (0 until amount).map { index -> createPlace("Place-$index") }
}