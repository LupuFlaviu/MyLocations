package com.flaviul.test.mylocations.di.module

import com.flaviul.test.mylocations.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    companion object {
        private const val GSON_IDENTIFIER = "gson"
        private const val TIMEOUT = 10L
    }

    @Provides
    @Singleton
    internal fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    internal fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.createWithScheduler(
        Schedulers.io()
    )

    @Provides
    @Singleton
    internal fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    @Named(GSON_IDENTIFIER)
    internal fun provideConverterFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named(GSON_IDENTIFIER) gsonConverter: Converter.Factory,
        adapterFactory: CallAdapter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(adapterFactory)
            .addConverterFactory(gsonConverter)
            .client(okHttpClient)
            .build()
}