package com.acdev.pokedex.SubComposables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.acdev.pokedex.EventHandler.SearchEventHandling
import com.acdev.pokedex.ViewModel.PokeViewModel




@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    searchEvent: (SearchEventHandling) -> Unit,
    searchState: PokeViewModel.SearchListState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (searchState.error.trim().isNotEmpty()) {
        DisplayToast(context = LocalContext.current, msg = searchState.error)
        searchEvent.invoke(SearchEventHandling.ClearError)
    }

    Box(modifier = modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(26.dp))
                .clip(AbsoluteRoundedCornerShape(20.dp))
                .onFocusChanged {
                    if (it.isFocused) {
                        searchEvent.invoke(SearchEventHandling.SearchFocused(true))
                    }
                },
            value = searchState.ToSearch,
            placeholder = {
                Text(text = "Search Pokemon")
            },
            leadingIcon = {
                if (searchState.StartSearch) {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        searchEvent.invoke(SearchEventHandling.SearchFocused(false))
                        searchEvent.invoke(SearchEventHandling.SearchData(""))
                        searchEvent.invoke(SearchEventHandling.ClearSearchedData)
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            "",
                            tint = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }
            },
            trailingIcon = {
                if (searchState.StartSearch) {
                    IconButton(onClick = {
                        if (searchState.ToSearch.trim().isNotEmpty()) {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            searchEvent.invoke(SearchEventHandling.GoForSearch)
                        }
                    }, modifier = Modifier.padding(end = 0.dp)) {
                        Icon(
                            Icons.Filled.Search,
                            "",
                            tint = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }
            },
            onValueChange = {
                searchEvent.invoke(SearchEventHandling.SearchData(it))
            },
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                textColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}