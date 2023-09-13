package com.acdev.pokedex.Utils

import androidx.compose.ui.graphics.Color
import com.acdev.pokedex.ResponseClasses.Stat
import com.acdev.pokedex.ui.theme.*

fun parseTypeToColor(type: String): Color {
    return when (type.lowercase()) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when (stat.stat.name.lowercase()) {
        "hp" -> "HP"
        "attack" -> "ATK"
        "defense" -> "DEF"
        "special-attack" -> "SPATK"
        "special-defense" -> "SPDEF"
        "speed" -> "SPD"
        else -> ""
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when(stat.stat.name.lowercase()) {
        "hp" -> flying
        "attack" -> AtkColor
        "defense" -> md_orange_100
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}