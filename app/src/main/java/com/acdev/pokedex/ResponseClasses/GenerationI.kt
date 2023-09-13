package com.acdev.pokedex.ResponseClasses

import com.google.gson.annotations.SerializedName

data class GenerationI(
    @SerializedName("red-blue")
    val redblue: RedBlue,
    val yellow: Yellow
)