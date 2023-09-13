package com.acdev.pokedex.Composables

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.acdev.pokedex.EventHandler.ListEventHandling
import com.acdev.pokedex.EventHandler.SearchEventHandling
import com.acdev.pokedex.ModelClasses.PokedexListEntry
import com.acdev.pokedex.R
import com.acdev.pokedex.SubComposables.DisplayToast
import com.acdev.pokedex.SubComposables.PokemonEntrySample
import com.acdev.pokedex.SubComposables.SearchBar
import com.acdev.pokedex.Utils.TriggerProgressBar
import com.acdev.pokedex.ViewModel.PokeViewModel
import com.acdev.pokedex.ui.theme.Monserrat
import com.acdev.pokedex.ui.theme.Purple40
import com.acdev.pokedex.ui.theme.RobotoCondensed
import kotlinx.coroutines.flow.Flow


@Composable
fun MainListScreen(
    pagingData: Flow<PagingData<PokedexListEntry>>,
    eventOccur: (ListEventHandling) -> Unit,
    searchEvent: (SearchEventHandling) -> Unit,
    searchState: PokeViewModel.SearchListState,
    pokeDetailArgs: (Int, String) -> Unit,
) {
    val pagingItems = pagingData.collectAsLazyPagingItems()
    val context = LocalContext.current
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        if (pagingItems.loadState.refresh is LoadState.Error) {
            DisplayToast(
                context = context,
                msg = (pagingItems.loadState.refresh as LoadState.Error).error.message ?: ""
            )
        }
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_pokemon_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SearchBar(
                searchEvent,
                searchState, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (searchState.StartSearch && searchState.dataWeReceived != null) {
                Box(modifier = Modifier.padding(start = 20.dp)) {
                    PokemonEntrySample(
                        pokeDetailArgs = pokeDetailArgs,
                        eventOccur = eventOccur,
                        pokeDexListEntry = searchState.dataWeReceived,Modifier
                            .size(150.dp)
                            .padding(10.dp)
                    )
                }
            }
            if (pagingItems.loadState.refresh is LoadState.Loading || searchState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.inverseOnSurface)
                }
            } else if (!searchState.StartSearch) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(count = pagingItems.itemCount) { currentIndex ->
                        val pokemon = pagingItems[currentIndex]
                        pokemon?.let {
                            PokemonEntrySample(pokeDetailArgs, eventOccur, it, modifier = Modifier
                                .size(125.dp)
                                .padding(top = 10.dp))
                        }
                    }
                    item {
                        if (pagingItems.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.inverseOnSurface)
                        }
                    }
                }
            }
        }
    }
}


