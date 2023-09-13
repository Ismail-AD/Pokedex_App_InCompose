package com.acdev.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.acdev.pokedex.Composables.MainListScreen
import com.acdev.pokedex.Composables.PokemonDetail
import com.acdev.pokedex.ViewModel.PokeDetailViewModel
import com.acdev.pokedex.ViewModel.PokeViewModel
import com.acdev.pokedex.ui.theme.PokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main_screen") {
                    composable("main_screen") {
                        val viewModel: PokeViewModel = hiltViewModel()
                        MainListScreen(
                            viewModel.getPokeList,
                            viewModel::eventCall,
                            viewModel::searchEventCall,
                            viewModel
                                .searchState
                        ) { color, name ->
                            navController.navigate("poke_detail/$color/$name")
                        }
                    }
                    composable(
                        "poke_detail/{color}/{name}",
                        arguments = listOf(navArgument("color") {
                            type = NavType.IntType
                        }, navArgument("name") {
                            type = NavType.StringType
                        })
                    ) {
                        val remColor = remember {
                            val color = it.arguments?.getInt("color")
                            color?.let { colour -> Color(colour) } ?: Color.White
                        }
                        val remName = remember {
                           it.arguments?.getString("name");
                        }
                        val pokeDetailViewModel: PokeDetailViewModel = hiltViewModel()
                        PokemonDetail(remColor,pokeDetailViewModel::getPokemonInfo,remName){
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}

