package com.flaviul.test.mylocations.di

import com.flaviul.test.mylocations.di.scope.FragmentScope
import com.flaviul.test.mylocations.ui.main.details.LocationDetailsFragment
import com.flaviul.test.mylocations.di.module.LocationDetailsModule
import com.flaviul.test.mylocations.ui.main.list.LocationsListFragment
import com.flaviul.test.mylocations.di.module.LocationsListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [LocationsListModule::class])
    abstract fun provideLocationsListFragment(): LocationsListFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [LocationDetailsModule::class])
    abstract fun provideLocationDetailsFragment(): LocationDetailsFragment
}