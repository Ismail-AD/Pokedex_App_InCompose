package com.acdev.pokedex.ResponseClasses

import com.google.gson.annotations.SerializedName

data class GenerationV(
    @SerializedName("black-white")
    val blackwhite: BlackWhite
)