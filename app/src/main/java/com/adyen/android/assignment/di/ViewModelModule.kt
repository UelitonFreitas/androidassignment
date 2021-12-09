package com.adyen.android.assignment.di

import androidx.lifecycle.ViewModel
import com.adyen.android.assignment.ui.placesList.PlacesListViewModel
import com.hero.instadog.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlacesListViewModel::class)
    abstract fun bindBreedListViewModel(placesListViewModel: PlacesListViewModel): ViewModel
}