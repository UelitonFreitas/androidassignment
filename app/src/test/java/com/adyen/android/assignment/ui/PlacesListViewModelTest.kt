package com.adyen.android.assignment.ui

import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepository
import com.adyen.android.assignment.ui.placesList.PlacesListViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito

class PlacesListViewModelTest {

    private val placesRepository = Mockito.mock(PlacesRepository::class.java)
    private val geolocationRepository = Mockito.mock(GeolocationRepository::class.java)

    @Test
    fun `should load places`() = runBlocking {

        val placesListViewModel = PlacesListViewModel(placesRepository, geolocationRepository)
    }
}