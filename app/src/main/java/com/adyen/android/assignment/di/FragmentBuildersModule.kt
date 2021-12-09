package com.adyen.android.assignment.di;

import com.adyen.android.assignment.ui.placesList.PlaceListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributePlaceListFragment(): PlaceListFragment
}
