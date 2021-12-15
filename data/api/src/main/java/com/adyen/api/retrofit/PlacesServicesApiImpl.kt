package com.adyen.api.retrofit

import com.adyen.api.PlacesServicesApi
import com.adyen.api.VenueRecommendationsQueryBuilder
import com.adyen.dispatchers.DispatcherProvider
import com.adyen.envvar.EnvVar
import com.adyen.model.Location
import com.adyen.model.Place
import com.adyen.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@ExperimentalCoroutinesApi
class PlacesServicesApiImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val envVar: EnvVar
) :
    PlacesServicesApi {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(envVar.FOURSQUARE_BASE_URL)
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
            .addInterceptor(AuthenticationInterceptor(envVar))
            .build()
    }

    private val coreFields = listOf("name", "fsq_id")

    class AuthenticationInterceptor(private val envVar: EnvVar) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder().apply {
                addHeader("Authorization", envVar.FOUR_SQUARE_API_KEY)
            }.build()

            return chain.proceed(request)
        }
    }

    private suspend fun getVenueRecommendations(location: Location): List<Place> =
        withContext(dispatcherProvider.io()) {

            val query = VenueRecommendationsQueryBuilder()
                .setLatitudeLongitude(location.latitude, location.longitude)
                .build()

            instance.getVenueRecommendations(query).execute().body()?.results?.map { place ->
                Place(place.fsq_id, place.name)
            } ?: emptyList()
        }

    override fun getPlacesByLocationFlow(location: Location): Flow<Resource<List<Place>>> =
        callbackFlow {
            offer(Resource.loading(null))
            try {
                offer(Resource.success(getVenueRecommendations(location)))
            } catch (e: Throwable) {
                offer(Resource.error<List<Place>>(emptyList()))
            }
            awaitClose()
        }

    override fun getPlacesByQueryFlow(
        query: String,
        location: Location
    ): Flow<Resource<List<Place>>> = callbackFlow {

        offer(Resource.loading(null))
        try {
            offer(Resource.success(getPlacesByQuery(query, location)))
        } catch (e: Throwable) {
            offer(Resource.error<List<Place>>(emptyList()))
        }

        awaitClose()
    }
    private suspend fun getPlacesByQuery(aQuery: String, location: Location): List<Place> =
        withContext(dispatcherProvider.io()) {
            val query = VenueRecommendationsQueryBuilder()
                .setLatitudeLongitude(location.latitude, location.longitude)
                .setQuery(aQuery)
                .setFields(coreFields)
                .build()

            instance.getPlacesBYQuery(query).execute().body()?.results?.map { place ->
                Place(place.fsq_id, place.name)
            } ?: emptyList()
        }
}