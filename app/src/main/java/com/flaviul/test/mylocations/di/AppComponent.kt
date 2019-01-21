package com.flaviul.test.mylocations.di

import android.app.Application
import com.flaviul.test.mylocations.MyLocationsApp
import com.flaviul.test.mylocations.di.module.ApiModule
import com.flaviul.test.mylocations.di.module.ApplicationModule
import com.flaviul.test.mylocations.di.module.DBModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ActivityBuilder::class,
        ApiModule::class,
        DBModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(myLocationsApp: MyLocationsApp)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}