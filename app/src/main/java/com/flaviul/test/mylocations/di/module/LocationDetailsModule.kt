package com.flaviul.test.mylocations.di.module

import com.flaviul.test.mylocations.di.scope.FragmentScope
import com.flaviul.test.mylocations.ui.main.details.model.LocationDetailsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class LocationDetailsModule {

    @Provides
    @FragmentScope
    fun provideLocationDetailsViewModelFactory() =
        LocationDetailsViewModelFactory()
}