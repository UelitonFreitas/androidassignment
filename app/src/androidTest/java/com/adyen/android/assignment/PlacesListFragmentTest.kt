package com.adyen.android.assignment

import android.view.KeyEvent
import android.view.View
import androidx.databinding.DataBindingComponent
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.adyen.android.assignment.binding.FragmentBindingAdapters
import com.adyen.android.assignment.repository.model.Place
import com.adyen.android.assignment.ui.placesList.PlaceListFragment
import com.adyen.android.assignment.ui.placesList.PlacesListViewModel
import com.adyen.android.assignment.util.*
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(AndroidJUnit4::class)
class PlacesListFragmentTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)

    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()

    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()

    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityRule)


    private lateinit var mockBindingAdapter: FragmentBindingAdapters
    private lateinit var viewModel: PlacesListViewModel
    private val navController: NavController = mock()
    private val placeList = MutableLiveData<List<Place>>()
    private val showLoading = MutableLiveData<Boolean>()
    private val showErrorMessage = MutableLiveData<String?>()
    private val placesListFragment = PlaceListFragment()

    @Before
    fun init() {
        viewModel = mock()
        whenever(viewModel.places).thenReturn(placeList)
        whenever(viewModel.shouldShowSpinner).thenReturn(showLoading)
        whenever(viewModel.snackbar).thenReturn(showErrorMessage)

        mockBindingAdapter = Mockito.mock(FragmentBindingAdapters::class.java)

        placesListFragment.appExecutors = countingAppExecutors.appExecutors
        placesListFragment.viewModelFactory = ViewModelUtil.createFor(viewModel)
        placesListFragment.dataBindingComponent = object : DataBindingComponent {
            override fun getFragmentBindingAdapters(): FragmentBindingAdapters {
                return mockBindingAdapter
            }
        }
        Navigation.setViewNavController(
            activityRule.activity.findViewById<View>(R.id.container),
            navController
        )
        activityRule.activity.setFragment(placesListFragment)
        EspressoTestUtil.disableProgressBarAnimations(activityRule)
    }

    @Test
    fun shouldShowPlacesList() {
        val places = TestUtil.createPlaces(3)
        placeList.postValue(places)
        showLoading.postValue(false)

        places.forEachIndexed { index, place ->
            Espresso.onView(listMatcher().atPosition(index))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText(place.name))))
        }

        Espresso.onView(withId(R.id.progress_bar))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun shouldShowLoading() {
        val places = TestUtil.createPlaces(3)
        placeList.postValue(places)
        showLoading.postValue(true)

        places.forEachIndexed { index, place ->
            Espresso.onView(listMatcher().atPosition(index))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText(place.name))))
        }

        Espresso.onView(withId(R.id.progress_bar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun shouldErrorMessage() {
        val places = TestUtil.createPlaces(3)
        placeList.postValue(places)
        showLoading.postValue(false)
        showErrorMessage.postValue("Error on load Places from API")

        places.forEachIndexed { index, place ->
            Espresso.onView(listMatcher().atPosition(index))
                .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText(place.name))))
        }

        Espresso.onView(withId(R.id.progress_bar))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))

        Espresso.onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(ViewAssertions.matches(withText(R.string.error_message)))
    }

    @Test
    fun shouldSearch() {
        Espresso.onView(withId(R.id.input)).perform(
            ViewActions.typeText("foo"),
            ViewActions.pressKey(KeyEvent.KEYCODE_ENTER)
        )

        Mockito.verify(viewModel).setQuery("foo")
    }

    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.recycler_view_places_list)
    }
}