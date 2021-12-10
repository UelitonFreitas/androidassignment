package com.adyen.android.assignment.di

import android.app.Application
import androidx.room.Room
import com.adyen.android.assignment.api.PlacesServicesApi
import com.adyen.android.assignment.api.retrofit.PlacesServicesApiImpl
import com.adyen.android.assignment.repository.PlacesRepository
import com.adyen.android.assignment.repository.PlacesRepositoryImpl
import com.adyen.android.assignment.repository.dataBase.PlaceDao
import com.adyen.android.assignment.repository.dataBase.PlacesDatabase
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepository
import com.adyen.android.assignment.repository.geolocalization.GeolocationRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun providePlacesService(): PlacesServicesApi {
        return PlacesServicesApiImpl()
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
        placeDao: PlaceDao
    ): PlacesRepository {
        return PlacesRepositoryImpl(placesServicesApi, placeDao)
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