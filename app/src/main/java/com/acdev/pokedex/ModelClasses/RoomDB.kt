package com.acdev.pokedex.ModelClasses

import androidx.room.Database
import androidx.room.RoomDatabase
import com.acdev.pokedex.DaoInterfaces.PokeListDao
import com.acdev.pokedex.DaoInterfaces.RemoteKeysDao

@Database(entities = [PokedexListEntry::class, RemoteKeys::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun pokeListDao(): PokeListDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}