package com.adyen.android.assignment.di

import android.app.Application
import androidx.room.Room
import com.adyen.android.assignment.BuildConfig
import com.adyen.repositories.places.PlacesRepository
import com.adyen.android.assignment.repository.dataBase.PlacesRepositoryImpl
import com.adyen.android.assignment.repository.dataBase.PlaceDao
import com.adyen.android.assignment.repository.dataBase.PlacesDatabase
import com.adyen.repositories.geolocation.GeolocationRepository
import com.adyen.repositories.geolocation.GeolocationRepositoryImpl
import com.adyen.android.assignment.userCases.PlacesUserCase
import com.adyen.android.assignment.userCases.PlacesUserCaseImpl
import com.adyen.api.PlacesServicesApi
import com.adyen.api.retrofit.PlacesServicesApiImpl
import com.adyen.dispatchers.DefaultDispatcherProvider
import com.adyen.dispatchers.DispatcherProvider
import com.adyen.envvar.EnvVar
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun providePlacesUserCase(
        placesRepository: PlacesRepository,
        geolocationRepository: GeolocationRepository
    ): PlacesUserCase {
        return PlacesUserCaseImpl(placesRepository, geolocationRepository)
    }

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }

    @Singleton
    @Provides
    fun providePlacesService(
        dispatcherProvider: DispatcherProvider,
        envVar: EnvVar
    ): PlacesServicesApi {
        return PlacesServicesApiImpl(dispatcherProvider, envVar)
    }

    @Singleton
    @Provides
    fun provideGeolocationRepository(): GeolocationRepository {
        return GeolocationRepositoryImpl()
    }

    @Singleton
    @Provides
    fun providePlacesRepository(
        placesServicesApi: PlacesServicesApi,
        placeDao: PlaceDao,
        dispatcherProvider: DispatcherProvider
    ): PlacesRepository {
        return PlacesRepositoryImpl(placesServicesApi, placeDao, dispatcherProvider)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): PlacesDatabase {
        return Room
            .databaseBuilder(app, PlacesDatabase::class.java, "places.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePlaceDao(db: PlacesDatabase): PlaceDao {
        return db.placeDao()
    }

    @Singleton
    @Provides
    fun provideEnvVar(): EnvVar {
        return object : EnvVar {
            override val FOUR_SQUARE_API_KEY = BuildConfig.FOUR_SQUARE_API_KEY
            override val FOURSQUARE_BASE_URL = BuildConfig.FOURSQUARE_BASE_URL
        }
    }
}