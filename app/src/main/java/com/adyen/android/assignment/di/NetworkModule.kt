package com.adyen.android.assignment.di

import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.api.model.DayAdapter
import com.adyen.android.assignment.data.interceptor.ConnectivityInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.NASA_BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttp(
        connectivityInterceptor: ConnectivityInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.addInterceptor(connectivityInterceptor)

        return builder
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(): MoshiConverterFactory {
        val moshi = Moshi
            .Builder()
            .add(DayAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        return MoshiConverterFactory.create(moshi)
    }

}