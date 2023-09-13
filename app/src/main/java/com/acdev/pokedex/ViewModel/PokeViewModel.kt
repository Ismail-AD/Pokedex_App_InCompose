package com.acdev.pokedex.ViewModel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.palette.graphics.Palette
import com.acdev.pokedex.EventHandler.ListEventHandling
import com.acdev.pokedex.EventHandler.SearchEventHandling
import com.acdev.pokedex.Mapper.toPokeDexListEntry
import com.acdev.pokedex.ModelClasses.PokedexListEntry
import com.acdev.pokedex.Repository.PokeRepository
import com.acdev.pokedex.ResponseClasses.PokemonData
import com.acdev.pokedex.Utils.PAGE_SIZE
import com.acdev.pokedex.Utils.ResultProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import javax.inject.Inject

@HiltViewModel
class PokeViewModel @Inject constructor(private val repository: PokeRepository) : ViewModel() {

    var searchState by mutableStateOf(SearchListState())

    fun eventCall(eventHandling: ListEventHandling) {
        when (eventHandling) {
            is ListEventHandling.GetPaletteColor -> getPokeBackColor(
                eventHandling.drawable,
                eventHandling.onFinish
            )
        }
    }

    fun searchEventCall(eventHandling: SearchEventHandling) {
        when (eventHandling) {
            is SearchEventHandling.SearchData -> {
                searchState = searchState.copy(ToSearch = eventHandling.dataToSearch)
            }

            is SearchEventHandling.SearchFocused -> searchState =
                searchState.copy(StartSearch = eventHandling.active)

            SearchEventHandling.GoForSearch -> searching()
            SearchEventHandling.ClearError -> searchState = searchState.copy(error = "")
            SearchEventHandling.ClearSearchedData ->  searchState =
                searchState.copy(dataWeReceived = null)
        }
    }

    private fun searching() {
        searchState = searchState.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = repository.getPokemon(searchState.ToSearch)) {
                is ResultProvider.Error -> searchState =
                    searchState.copy(error = "Pokemon Not Found !", isLoading = false)

                is ResultProvider.Loading -> {}
                is ResultProvider.Success -> response.data?.let { pokemonData ->
                    searchState =
                        searchState.copy(
                            dataWeReceived = pokemonData.toPokeDexListEntry(),
                            isLoading = false
                        )
                }
            }
        }
    }


    val getPokeList = repository.getPokemonList().flowOn(Dispatchers.IO).cachedIn(viewModelScope)

    fun getPokeBackColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        // Specification ARGB_8888 means create a pixel with four channels ARGB - Alpha, Red, Green, Blue
        // and allocate each 8 bits of storage. As four eights are 32 this is 32 bit graphics.
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate {
            // The rgb property of the Swatch object returns the RGB value of the most prominent color as an integer.
            it?.dominantSwatch?.rgb?.let {
                onFinish(Color(it))
            }
        }
    }

    data class SearchListState(
        val ToSearch: String = "",
        val StartSearch: Boolean = false,
        val error: String = "",
        val isLoading: Boolean = false,
        val dataWeReceived: PokedexListEntry? = null,
    )
}