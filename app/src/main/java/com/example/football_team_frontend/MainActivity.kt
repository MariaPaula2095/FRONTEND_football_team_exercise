package com.example.football_team_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.football_team_frontend.ui.screens.InicioScreen
import com.example.football_team_frontend.ui.theme.FootballTheme
import androidx.navigation.compose.*
import com.example.football_team_frontend.ui.screens.CrearEquipoScreen
import com.example.football_team_frontend.ui.screens.EquipoScreen
import com.example.football_team_frontend.viewmodel.EquipoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FootballTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "inicio",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("inicio") {
                            InicioScreen(
                                onEquiposClick = {
                                    navController.navigate("equipos")
                                }
                            )
                        }

                        composable("equipos") {
                            val viewModel: EquipoViewModel = viewModel()
                            val equipos by viewModel.equipos.collectAsState()
                            val searchText by viewModel.search.collectAsState()

                            EquipoScreen(
                                equipos = equipos,
                                searchText = searchText,
                                onSearchChange = { viewModel.onSearchChange(it) },
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onAgregarClick = {
                                    navController.navigate("crear_equipo")
                                },
                                onEliminarClick = { id ->
                                    viewModel.eliminarEquipo(id)
                                }
                            )
                        }

                        composable("crear_equipo") {
                            CrearEquipoScreen(
                                nombre = "",
                                ciudad = "",
                                fundacion = "",
                                onNombreChange = {},
                                onCiudadChange = {},
                                onFundacionChange = {},
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onGuardarClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}