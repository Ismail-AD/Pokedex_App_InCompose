package com.acdev.pokedex.Hilt

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.acdev.pokedex.ApiCalls.PokeApi
import com.acdev.pokedex.ModelClasses.PokedexListEntry
import com.acdev.pokedex.ModelClasses.RoomDB
import com.acdev.pokedex.Pagging.mediatorObject
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagingModule {
    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun pagerInstance(
        roomDB: RoomDB,
        pokeApi: PokeApi,
    ): Pager<Int, PokedexListEntry> {
        return Pager(
            config =
            PagingConfig(pageSize = 20),
            remoteMediator = mediatorObject(roomDB, pokeApi),
            pagingSourceFactory = {
                roomDB.pokeListDao().getOfflineData()
            }
        )
    }
}