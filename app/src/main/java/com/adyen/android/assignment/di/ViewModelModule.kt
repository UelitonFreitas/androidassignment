package com.adyen.android.assignment.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adyen.android.assignment.ui.placesList.PlacesListViewModel
import com.adyen.android.assignment.viewModel.PlacesListViewModelFactory
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
    abstract fun bindPlacesListViewModel(placesListViewModel: PlacesListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: PlacesListViewModelFactory): ViewModelProvider.Factory
}