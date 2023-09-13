package com.acdev.pokedex.SubComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.acdev.pokedex.EventHandler.ListEventHandling
import com.acdev.pokedex.ModelClasses.PokedexListEntry
import com.acdev.pokedex.R
import com.acdev.pokedex.ui.theme.Monserrat


@Composable
fun PokemonEntrySample(
    pokeDetailArgs: (Int, String) -> Unit,
    eventOccur: (ListEventHandling) -> Unit,
    pokeDexListEntry: PokedexListEntry,
    modifier: Modifier = Modifier,
) {
    val defaultBackColor = MaterialTheme.colorScheme.surface
    var dominantColorState by remember {
        mutableStateOf(defaultBackColor)
    }
    Box(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(dominantColorState)
            .clickable {
                pokeDetailArgs(dominantColorState.toArgb(), pokeDexListEntry.name)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).placeholder(R.drawable.ph)
                        .error(R.drawable.ph)
                        .data(pokeDexListEntry.imageUrl).decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = pokeDexListEntry.name,
                    modifier = modifier,
                    onSuccess = {
                        eventOccur.invoke(ListEventHandling.GetPaletteColor(it.result.drawable) { colorWeReceive ->
                            dominantColorState = colorWeReceive
                        })
                    }
                )
            }
            Text(
                text = pokeDexListEntry.name,
                fontFamily = Monserrat,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                fontSize = 17.sp,
                textAlign = TextAlign.Center, modifier = Modifier
                    .padding(vertical = 10.dp)
            )
        }
    }
}