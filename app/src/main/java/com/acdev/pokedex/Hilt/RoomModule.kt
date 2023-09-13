package com.acdev.pokedex.Hilt

import android.content.Context
import androidx.room.Room
import com.acdev.pokedex.ModelClasses.RoomDB
import com.acdev.pokedex.Utils.DatabaseName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun roomBuilder(@ApplicationContext context: Context): RoomDB {
        return Room.databaseBuilder(context, RoomDB::class.java, DatabaseName).build()
    }

}