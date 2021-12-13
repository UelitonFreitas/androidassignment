package com.adyen.android.assignment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.adyen.android.assignment.CoroutinesTestRule
import com.adyen.android.assignment.TestUtil
import com.adyen.android.assignment.repository.model.Place
import com.adyen.android.assignment.repository.model.Resource
import com.adyen.android.assignment.ui.placesList.PlacesListViewModel
import com.adyen.android.assignment.userCases.PlacesUserCase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*


@ExperimentalCoroutinesApi
class PlacesViewModelTest {

    private val placesUserCase: PlacesUserCase = mock()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    var loadingCaptor = argumentCaptor<Boolean>()

    private var errorMessageCaptor = argumentCaptor<String>()

    var placeListCaptor = argumentCaptor<List<Place>>()


    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `should show places`() = coroutinesTestRule.testDispatcher.runBlockingTest {

        val expectedPlaceList = TestUtil.createPlaces(3)
        val placeListObserver = mock<Observer<List<Place>>>()
        val showLoadingObserver = mock<Observer<Boolean>>()

        mockPlacesUserCaseFlow(Resource.success(expectedPlaceList))

        val flow = flow<String> {}
        whenever(placesUserCase.getQueryFLow()).thenReturn(flow)

        val placesViewModel = PlacesListViewModel(placesUserCase)

        placesViewModel.shouldShowSpinner.observeForever(showLoadingObserver)
        placesViewModel.places.observeForever(placeListObserver)

        verify(placeListObserver).onChanged(eq(expectedPlaceList))

        verify(showLoadingObserver, times(2)).onChanged(loadingCaptor.capture())

        assertEquals(false, loadingCaptor.firstValue)
        assertEquals(false, loadingCaptor.secondValue)
    }

    @Test
    fun `should show loading when requesting places`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {

            val expectedPlaceList = TestUtil.createPlaces(3)
            val placeListObserver = mock<Observer<List<Place>>>()
            val showLoadingObserver = mock<Observer<Boolean>>()

            mockPlacesUserCaseFlow(
                listOf(
                    Resource.loading(null),
                    Resource.success(expectedPlaceList)
                )
            )

            val flow = flow<String> {}
            whenever(placesUserCase.getQueryFLow()).thenReturn(flow)

            val placesViewModel = PlacesListViewModel(placesUserCase)

            placesViewModel.shouldShowSpinner.observeForever(showLoadingObserver)
            placesViewModel.places.observeForever(placeListObserver)

            verify(placeListObserver).onChanged(eq(expectedPlaceList))

            verify(showLoadingObserver, times(3)).onChanged(loadingCaptor.capture())

            assertEquals(false, loadingCaptor.firstValue)
            assertEquals(true, loadingCaptor.secondValue)
            assertEquals(false, loadingCaptor.thirdValue)
        }

    @Test
    fun `should show error message when there are an error on load places`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {

            val expectedPlaceList = emptyList<Place>()
            val placeListObserver = mock<Observer<List<Place>>>()
            val showLoadingObserver = mock<Observer<Boolean>>()
            val errorObserver = mock<Observer<String?>>()

            mockPlacesUserCaseFlow(
                listOf(
                    Resource.loading(null),
                    Resource.error(null)
                )
            )

            val flow = flow<String> {}
            whenever(placesUserCase.getQueryFLow()).thenReturn(flow)

            val placesViewModel = PlacesListViewModel(placesUserCase)

            placesViewModel.shouldShowSpinner.observeForever(showLoadingObserver)
            placesViewModel.snackbar.observeForever(errorObserver)
            placesViewModel.places.observeForever(placeListObserver)

            verify(placeListObserver, times(2)).onChanged(placeListCaptor.capture())

            verify(showLoadingObserver, times(3)).onChanged(loadingCaptor.capture())
            verify(errorObserver, times(2)).onChanged(errorMessageCaptor.capture())

            assertEquals(false, loadingCaptor.firstValue)
            assertEquals(true, loadingCaptor.secondValue)
            assertEquals(false, loadingCaptor.thirdValue)

            assertEquals("Error on load Places from API", errorMessageCaptor.secondValue)

            assertEquals(expectedPlaceList, placeListCaptor.firstValue)
            assertEquals(expectedPlaceList, placeListCaptor.secondValue)
        }

    private fun mockPlacesUserCaseFlow(
        resource: Resource<List<Place>>
    ) {
        val placeFlow = flow {
            emit(resource)
        }
        whenever(placesUserCase.getPlacesFlow()).thenReturn(placeFlow)
    }

    private fun mockPlacesUserCaseFlow(
        resources: List<Resource<List<Place>>>
    ) {
        val placeFlow = flow {
            resources.forEach { emit(it) }
        }
        whenever(placesUserCase.getPlacesFlow()).thenReturn(placeFlow)
    }

}