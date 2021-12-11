package com.adyen.android.assignment.ui

import app.cash.turbine.test
import com.adyen.android.assignment.CoroutinesTestRule
import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepository
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import com.adyen.android.assignment.userCases.PlacesUserCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
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

            val placeFlow = flow<List<Place>> { }
            whenever(placesRepository.getPlaceListFlow()).thenReturn(placeFlow)

            val userCase = PlacesUserCaseImpl(
                placesRepository,
                geolocationRepository,
                coroutinesTestRule.testDispatcherProvider
            )

            verify(placesRepository, never()).getVenueRecommendations(any())

            userCase.getPlacesFlow().test { awaitComplete() }
        }

    @Test
    fun `should load places for a location`() = coroutinesTestRule.testDispatcher.runBlockingTest {

        val location = Location(1.0, 1.0)

        mockLocationRepositoryFlow(location)

        mockPlacesRepositoryFlow()

        val userCase = PlacesUserCaseImpl(
            placesRepository,
            geolocationRepository,
            coroutinesTestRule.testDispatcherProvider
        )

        userCase.getPlacesFlow().test {
            assertThat(
                awaitItem(), contains(
                    createPlace("Place-0"),
                    createPlace("Place-1"),
                    createPlace("Place-2")
                )
            )
            awaitComplete()
        }
    }

    @Test
    fun `should load places for a location on user interaction`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {

            val location = Location(1.0, 1.0)

            mockLocationRepositoryFlow(location)

            val userCase = PlacesUserCaseImpl(
                placesRepository,
                geolocationRepository,
                coroutinesTestRule.testDispatcherProvider
            )

            mockPlacesRepositoryFlow(createPlaces(3))

            userCase.loadPlaces()

            verify(placesRepository, times(2)).getVenueRecommendations(eq(location))
        }

    private fun mockPlacesRepositoryFlow(places: List<Place> = createPlaces(3)) {
        val placeFlow = flow { emit(places) }
        whenever(placesRepository.getPlaceListFlow()).thenReturn(placeFlow)
    }

    private fun mockLocationRepositoryFlow(location: Location) {
        val locationFlow = flow { emit(location) }
        whenever(geolocationRepository.fetchLocations()).thenReturn(locationFlow)
    }

    private fun createPlace(name: String) = Place(name)

    private fun createPlaces(amount: Int) =
        (0 until amount).map { index -> createPlace("Place-$index") }
}