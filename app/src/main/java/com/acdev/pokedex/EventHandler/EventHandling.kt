package com.acdev.pokedex.EventHandler

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color

sealed interface ListEventHandling {
    data class GetPaletteColor(val drawable: Drawable, val onFinish: (Color) -> Unit) :
        ListEventHandling
}

sealed interface SearchEventHandling {
    data class SearchData(val dataToSearch: String) : SearchEventHandling
    data class SearchFocused(val active: Boolean) : SearchEventHandling
    object ClearError : SearchEventHandling
    object ClearSearchedData : SearchEventHandling
    object GoForSearch : SearchEventHandling
}
