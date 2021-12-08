package com.adyen.android.assignment.api

import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.api.model.ApiResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
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

    companion object  {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val instance: PlacesService by lazy { retrofit.create(PlacesService::class.java) }

        private val httpClient by lazy {
            OkHttpClient.Builder()
                .addNetworkInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .addInterceptor(AuthenticationInterceptor())
                .build()
        }
    }

    class AuthenticationInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder().apply {
                addHeader("Authorization", BuildConfig.FOUR_SQUARE_API_KEY)
            }.build()

            return chain.proceed(request)
        }
    }
}
