package com.acdev.pokedex.Repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.acdev.pokedex.ApiCalls.PokeApi
import com.acdev.pokedex.ModelClasses.PokedexListEntry
import com.acdev.pokedex.ResponseClasses.PokemonData
import com.acdev.pokedex.ResponseClasses.PokemonList
import com.acdev.pokedex.Utils.ResultProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokeRepository @Inject constructor(
    private val pokeApi: PokeApi,
    private val pager: Pager<Int, PokedexListEntry>,
) {
    fun getPokemonList(): Flow<PagingData<PokedexListEntry>> {
        return pager.flow
    }

    suspend fun getPokemon(name: String): ResultProvider<PokemonData> {

        val response = try {
            pokeApi.getPokemonData(name.lowercase())
        } catch (e: Exception) {
            return ResultProvider.Error(msg = "Internet Connection Lost !")
        }
        return ResultProvider.Success(response)
    }
}