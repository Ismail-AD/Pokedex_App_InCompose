package com.acdev.pokedex.SubComposables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acdev.pokedex.ResponseClasses.PokemonData
import com.acdev.pokedex.Utils.parseStatToAbbr
import com.acdev.pokedex.Utils.parseStatToColor
import com.acdev.pokedex.ui.theme.Monserrat
import com.acdev.pokedex.ui.theme.Poppins

@Composable
fun SingleStatComposable(
    statName: String,
    statValue: Int,
    statColor: Color,
    Height: Dp = 28.dp,
    maxValue: Int,
    animationDuration: Int = 1000,
    Delay: Int = 0,
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / maxValue.toFloat()
        } else 0f, animationSpec = tween(
            delayMillis = Delay,
            durationMillis = animationDuration
        )
    )
    LaunchedEffect(key1 = Unit) {
        animationPlayed = true
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        Text(
            text = statName,
            color = if(isSystemInDarkTheme()) Color.White else Color.Black,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = (currentPercentage.value * maxValue).toInt().toString(),
            color = if(isSystemInDarkTheme()) Color.White else Color.Black,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.width(80.dp).graphicsLayer { translationX = 30.dp.toPx() }
        )
        Box(
            modifier = Modifier
                .height(Height).fillMaxWidth().padding(start = 100.dp)
                .clip(CircleShape)
                .background(if (isSystemInDarkTheme()) Color(0xFF505050) else Color.LightGray)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(currentPercentage.value)
                    .clip(CircleShape)
                    .background(statColor)
                    .padding(horizontal = 8.dp)
            ) {

            }
        }
    }
}

@Composable
fun CompleteStateComposable(pokemonData: PokemonData, animationPerItem: Int = 0) {
    val maxStatValue = remember {
        pokemonData.stats.maxOf { it.base_stat }
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)) {
        Text(
            text = "Base Stats",
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 20.sp, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        for (i in pokemonData.stats.indices) {
            val statPerIndex = pokemonData.stats[i]
            SingleStatComposable(
                statName = parseStatToAbbr(statPerIndex),
                statValue = statPerIndex.base_stat,
                statColor = parseStatToColor(statPerIndex),
                maxValue = maxStatValue,
                Delay = i * animationPerItem
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}