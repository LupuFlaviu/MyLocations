package com.flaviul.test.mylocations.di.module

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.flaviul.test.mylocations.MyLocationsApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    internal fun provideApplication(application: Application): MyLocationsApp = application as MyLocationsApp

    @Provides
    @Singleton
    internal fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}