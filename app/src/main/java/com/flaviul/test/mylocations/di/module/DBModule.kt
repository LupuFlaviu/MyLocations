package com.flaviul.test.mylocations.di.module;

import android.content.Context
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton


@Module
class DBModule {

    @Provides
    @Singleton
    internal fun provideRealm(context: Context): Realm {
        Realm.init(context)
        val config = RealmConfiguration.Builder()
            .name("locations.realm")
            .schemaVersion(1)
            .build()
        // Get a Realm instance for this thread
        return Realm.getInstance(config)
    }
}
