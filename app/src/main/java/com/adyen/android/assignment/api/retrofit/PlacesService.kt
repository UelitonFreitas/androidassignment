package com.adyen.android.assignment.api.retrofit

import com.adyen.api.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface PlacesService {
    /**
     * Get venue recommendations.
     *
     * See [the docs](https://developer.foursquare.com/docs/api/venues/explore)
     */
    @GET("/v3/places/search")
    fun getVenueRecommendations(@QueryMap query: Map<String, String>): Call<ApiResponse>

    @GET("v3/places/search")
    fun getPlacesBYQuery(@QueryMap query: Map<String, String>): Call<ApiResponse>
}
