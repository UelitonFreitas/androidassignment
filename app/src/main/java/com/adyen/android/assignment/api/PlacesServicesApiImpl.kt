package com.adyen.android.assignment.api

import com.adyen.android.assignment.BuildConfig
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

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val instance: PlacesService by lazy { retrofit.create(PlacesService::class.java) }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(AuthenticationInterceptor())
            .build()
    }

    class AuthenticationInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder().apply {
                addHeader("Authorization", BuildConfig.FOUR_SQUARE_API_KEY)
            }.build()

            return chain.proceed(request)
        }
    }

    override suspend fun getVenueRecommendations(query: Map<String, String>): List<Place> =
        withContext(Dispatchers.IO) {
            instance.getVenueRecommendations(query).results.map { Place("uhuuuuu") }
        }
}