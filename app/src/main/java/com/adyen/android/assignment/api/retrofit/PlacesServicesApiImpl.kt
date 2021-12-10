package com.adyen.android.assignment.api.retrofit

import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.api.PlacesServicesApi
import com.adyen.android.assignment.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.repository.geolocalization.model.Location
import com.adyen.android.assignment.repository.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlacesServicesApiImpl : PlacesServicesApi {

    companion object {
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
                .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
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

    override suspend fun getVenueRecommendations(location: Location): List<Place> =
        withContext(Dispatchers.IO) {

            val query = VenueRecommendationsQueryBuilder()
                .setLatitudeLongitude(location.latitude, location.longitude)
                .build()

            instance.getVenueRecommendations(query).execute().body()?.results?.map { place ->
                Place(place.name)
            } ?: emptyList()
        }
}