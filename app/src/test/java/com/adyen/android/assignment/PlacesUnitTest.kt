package com.adyen.android.assignment

import com.adyen.api.retrofit.PlacesServicesApiImpl
import com.adyen.api.VenueRecommendationsQueryBuilder
import com.adyen.dispatchers.DefaultDispatcherProvider
import com.adyen.envvar.EnvVar
import org.junit.Assert.*
import org.junit.Test

class PlacesUnitTest {
    @Test
    fun testResponseCode() {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(52.376510, 4.905890)
            .build()
        val response = PlacesServicesApiImpl(DefaultDispatcherProvider(), object : EnvVar {
            override val FOUR_SQUARE_API_KEY = BuildConfig.FOUR_SQUARE_API_KEY
            override val FOURSQUARE_BASE_URL = BuildConfig.FOURSQUARE_BASE_URL
        }).instance
            .getVenueRecommendations(query)
            .execute()

        val errorBody = response.errorBody()
        assertNull("Received an error: ${errorBody?.string()}", errorBody)

        val responseWrapper = response.body()
        assertNotNull("Response is null.", responseWrapper)
        assertEquals("Response code", 200, response.code() )
    }
}
