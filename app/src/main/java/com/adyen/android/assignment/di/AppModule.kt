package com.adyen.android.assignment.di

import PlacesServicesApiImpl
import android.app.Application
import androidx.room.Room
import com.adyen.android.assignment.api.PlacesServicesApi
import com.adyen.android.assignment.dispatchers.DefaultDispatcherProvider
import com.adyen.android.assignment.dispatchers.DispatcherProvider
import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.PlacesRepositoryImpl
import com.adyen.android.assignment.repository.dataBase.PlaceDao
import com.adyen.android.assignment.repository.dataBase.PlacesDatabase
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepository
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepositoryImpl
import com.adyen.android.assignment.userCases.PlacesUserCase
import com.adyen.android.assignment.userCases.PlacesUserCaseImpl
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
    fun providePlacesService(dispatcherProvider: DispatcherProvider): PlacesServicesApi {
        return PlacesServicesApiImpl(dispatcherProvider)
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
}