package com.flaviul.test.mylocations.di

import com.flaviul.test.mylocations.di.scope.ActivityScope
import com.flaviul.test.mylocations.ui.main.MainActivity
import com.flaviul.test.mylocations.di.module.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentsProvider::class])
    abstract fun provideMainActivityInjector(): MainActivity
}