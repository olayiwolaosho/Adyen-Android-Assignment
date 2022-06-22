package com.adyen.android.assignment.di

import android.content.Context
import com.adyen.android.assignment.AppDatabase
import com.adyen.android.assignment.data.db.AstronomyPictureDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideAstronomyPictureDao(appDatabase: AppDatabase): AstronomyPictureDao {
        return appDatabase.astronomyPictureDao()
    }

}