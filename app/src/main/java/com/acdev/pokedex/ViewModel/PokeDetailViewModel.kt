package com.acdev.pokedex.ViewModel


import androidx.lifecycle.ViewModel
import com.acdev.pokedex.Repository.PokeRepository
import com.acdev.pokedex.ResponseClasses.PokemonData
import com.acdev.pokedex.Utils.ResultProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokeDetailViewModel @Inject constructor(private val repository: PokeRepository) :
    ViewModel() {
    suspend fun getPokemonInfo(name: String): ResultProvider<PokemonData> {
        return repository.getPokemon(name)
    }
}