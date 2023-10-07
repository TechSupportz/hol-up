package com.nasportfolio.holup.di

import android.content.Context
import androidx.room.Room
import com.nasportfolio.holup.HUDatabase
import com.nasportfolio.holup.dao.BlockedAppDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ): HUDatabase = Room.databaseBuilder(
        context = context,
        klass = HUDatabase::class.java,
        name = "hu_database"
    ).build()

    @Singleton
    @Provides
    fun providesBlockedAppDao(
        database: HUDatabase
    ): BlockedAppDao = database.blockedAppDao

}