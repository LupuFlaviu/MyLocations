package com.flaviul.test.mylocations.di.module

import com.flaviul.test.mylocations.data.api.service.LocationsService
import com.flaviul.test.mylocations.data.repository.LocationsRepository
import com.flaviul.test.mylocations.di.scope.ActivityScope
import com.flaviul.test.mylocations.ui.main.MainActivity
import com.flaviul.test.mylocations.ui.main.MainController
import com.flaviul.test.mylocations.ui.main.model.MainViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainActivityModule {

    @Provides
    @ActivityScope
    fun provideMainViewModelFactory(locationsRepository: LocationsRepository) =
        MainViewModelFactory(locationsRepository)

    @Provides
    @ActivityScope
    internal fun provideMainControllerNavigation(mainActivity: MainActivity): MainController.Navigator = mainActivity

    @Provides
    @ActivityScope
    internal fun provideMainControllerView(mainActivity: MainActivity): MainController.View = mainActivity

    @Provides
    @ActivityScope
    fun provideLocationsService(retrofit: Retrofit) = retrofit.create(LocationsService::class.java)!!
}