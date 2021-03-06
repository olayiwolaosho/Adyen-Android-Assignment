package com.adyen.android.assignment.di

import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.data.repo.PlanetaryDbRepo
import com.adyen.android.assignment.data.repo.PlanetaryDbRepoImpl
import com.adyen.android.assignment.data.repo.PlanetaryRepo
import com.adyen.android.assignment.data.repo.PlanetaryRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlanetaryModule {

    @Provides
    @Singleton
    fun providesPlanetaryService(retrofit: Retrofit): PlanetaryService {
        return retrofit.create(PlanetaryService::class.java)
    }

    @Provides
    @Singleton
    fun providesPlanetaryRepo(planetaryRepoImpl: PlanetaryRepoImpl): PlanetaryRepo {
        return planetaryRepoImpl
    }

    @Provides
    @Singleton
    fun providesPlanetaryDBRepo(planetaryDbRepoImpl: PlanetaryDbRepoImpl): PlanetaryDbRepo {
        return planetaryDbRepoImpl
    }

}