package com.adyen.android.assignment.di

import com.adyen.android.assignment.api.PlacesServicesApi
import com.adyen.android.assignment.api.PlacesServicesApiImpl
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
}