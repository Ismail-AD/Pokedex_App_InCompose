package com.acdev.pokedex.ModelClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acdev.pokedex.Utils.tableNameOfSinglePokemon

@Entity(tableName = tableNameOfSinglePokemon)
data class PokedexListEntry(
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    val name: String,
    val imageUrl: String)