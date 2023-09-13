package com.acdev.pokedex.ResponseClasses

data class Ability(
    val ability: AbilityX,
    val is_hidden: Boolean,
    val slot: Int
)