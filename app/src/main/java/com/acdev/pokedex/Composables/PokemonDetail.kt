package com.acdev.pokedex.Composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.acdev.pokedex.ResponseClasses.PokemonData
import com.acdev.pokedex.ResponseClasses.Type
import com.acdev.pokedex.SubComposables.CompleteStateComposable
import com.acdev.pokedex.Utils.ResultProvider
import com.acdev.pokedex.Utils.parseTypeToColor
import com.acdev.pokedex.ui.theme.Monserrat
import com.acdev.pokedex.ui.theme.Poppins
import kotlin.math.round


@Composable
fun PokemonDetail(
    colorWeReceived: Color,
    pokeInfoCaller: suspend (String) -> ResultProvider<PokemonData>,
    remName: String?,
    navigateBack: () -> Unit,
) {
    val pokeInfo =
        produceState<ResultProvider<PokemonData>>(initialValue = ResultProvider.Loading()) {
            value = pokeInfoCaller.invoke(remName!!)
        }.value

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        when (pokeInfo) {
            is ResultProvider.Error -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    ErrorPhase(error = pokeInfo.msg!!, modifier = Modifier.align(Center))
                }
            }

            is ResultProvider.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoadingPhase(modifier = Modifier.align(Center))
                }
            }

            is ResultProvider.Success -> DataScreen(
                colorWeReceived = colorWeReceived,
                pokeInfo.data,navigateBack
            )
        }
    }

}

@Composable
fun LoadingPhase(modifier: Modifier = Modifier) {
    CircularProgressIndicator(color = MaterialTheme.colorScheme.inverseOnSurface, modifier = modifier)
}

@Composable
fun ErrorPhase(modifier: Modifier = Modifier, error: String) {
    Text(
        text = error,
        textAlign = TextAlign.Center,
        modifier = modifier,
        fontSize = 18.sp,
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataScreen(colorWeReceived: Color, data: PokemonData?, navigateBack: () -> Unit) {
    Scaffold(topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = { navigateBack.invoke() }) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        "backIcon",
                        modifier = Modifier.size(30.dp), tint = if(colorWeReceived.luminance() < 0.3f) Color.White else MaterialTheme.colorScheme.onBackground
                    )
                }
            }, actions = {
                Text(
                    text = "#${data!!.id}",
                    color = if(colorWeReceived.luminance() < 0.3f) Color.White else MaterialTheme.colorScheme.onBackground,
                    fontFamily = Monserrat,
                    fontWeight = FontWeight.Normal, fontSize = 19.sp, modifier = Modifier.padding(end = 15.dp)
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = colorWeReceived,
                navigationIconContentColor = Color.White
            )
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.inverseSurface)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(
                        colorWeReceived,
                        RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)
                    ), contentAlignment = Center
            ) {
                data?.sprites?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://unpkg.com/pokeapi-sprites@2.0.2/sprites/pokemon/other/dream-world/${data.id}.svg")
                            .decoderFactory(SvgDecoder.Factory())
                            .build(),
                        contentDescription = "Image",
                        modifier = Modifier.fillMaxHeight().padding(15.dp).fillMaxWidth(0.8f)
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                data?.let {
                    PokemonStat(pokemonData = it)
                }
            }
        }
    }
}

@Composable
fun PokemonStat(pokemonData: PokemonData, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = pokemonData.name.replaceFirstChar { it.uppercase() },
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center, fontSize = 25.sp
        )
        PokemonTypeRow(typeOf = pokemonData.types)
        HeiWeiRow(PokemonHeight = pokemonData.height, PokemonWeight = pokemonData.weight)
        CompleteStateComposable(pokemonData = pokemonData)
    }
}

@Composable
fun PokemonTypeRow(typeOf: List<Type>) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (type in typeOf) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
                    .clip(CircleShape)
                    .background(
                        parseTypeToColor(type.type.name)
                    )
                    .height(36.dp), contentAlignment = Center
            ) {
                Text(
                    text = type.type.name.replaceFirstChar { it.uppercase() },
                    color = Color.White,
                    fontFamily = Monserrat,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}

@Composable
fun HeiWeiRow(PokemonHeight: Int, PokemonWeight: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        val actualHeight = remember {
            round(PokemonHeight * 100f) / 1000f
        }
        val actualWeight = remember {
            round(PokemonWeight * 100f) / 1000f
        }
        PokeHeightWeightData(
            heightOrWeight = actualWeight,
            Unit = "KG",
            modifier = Modifier.weight(1f)
        )
        PokeHeightWeightData(
            heightOrWeight = actualHeight,
            Unit = "M",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PokeHeightWeightData(heightOrWeight: Float, Unit: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$heightOrWeight $Unit",
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )

        Text(
            text = if (Unit == "KG") "Weight" else "Height",
            color = MaterialTheme.colorScheme.inverseOnSurface,
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
        )
    }
}

