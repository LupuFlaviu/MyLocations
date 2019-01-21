package com.flaviul.test.mylocations.di.module

import com.flaviul.test.mylocations.di.scope.FragmentScope
import com.flaviul.test.mylocations.ui.main.list.model.LocationsListViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class LocationsListModule {

    @Provides
    @FragmentScope
    fun provideLocationsListViewModelFactory() =
        LocationsListViewModelFactory()

}