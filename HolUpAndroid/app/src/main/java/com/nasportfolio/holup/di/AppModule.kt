package com.nasportfolio.holup.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.nasportfolio.holup.data.dao.BlockedAppDao
import com.nasportfolio.holup.data.dao.BlockedAppDaoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

//    @Singleton
//    @Provides
//    fun providesDatabase(
//        @ApplicationContext context: Context
//    ): HUDatabase = Room.databaseBuilder(
//        context = context,
//        klass = HUDatabase::class.java,
//        name = "hu_database"
//    ).build()

//    @Singleton
//    @Provides
//    fun providesBlockedAppDao(
//        database: HUDatabase
//    ): BlockedAppDao = database.blockedAppDao

    @Singleton
    @Provides
    fun providesFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun providesBlockedAppDao(
        firestore: FirebaseFirestore,
        sharedPreferences: SharedPreferences,
    ): BlockedAppDao = BlockedAppDaoImpl(
        firestore = firestore,
        sharedPreferences = sharedPreferences,
    )

    @Singleton
    @Provides
    fun providesSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(
        "hu_shared_preferences",
        Context.MODE_PRIVATE
    )

}