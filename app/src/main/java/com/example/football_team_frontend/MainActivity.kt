package com.example.football_team_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.football_team_frontend.ui.screens.*
import com.example.football_team_frontend.ui.theme.FootballTheme
import com.example.football_team_frontend.viewmodel.EquipoViewModel
import com.example.football_team_frontend.viewmodel.JugadorViewModel

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
                    val jugadorViewModel: JugadorViewModel = viewModel()
                    val equipoViewModel: EquipoViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "inicio",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("inicio") {
                            InicioScreen(
                                onEquiposClick = { navController.navigate("equipos") },
                                onJugadoresClick = { navController.navigate("jugadores") }
                            )
                        }

                        composable("jugadores") {
                            val jugadores by jugadorViewModel.jugadores.collectAsState()
                            val equipos by equipoViewModel.equipos.collectAsState()
                            val cargando by jugadorViewModel.cargando.collectAsState()
                            val mensaje by jugadorViewModel.mensaje.collectAsState()

                            LaunchedEffect(Unit) {
                                if (jugadores.isEmpty()) {
                                    jugadorViewModel.listar()
                                }
                                if (equipos.isEmpty()) {
                                    equipoViewModel.cargarEquipos()
                                }
                            }

                            JugadoresScreen(
                                jugadores = jugadores,
                                equipos = equipos,
                                cargando = cargando,
                                mensaje = mensaje,
                                onDismissMensaje = { jugadorViewModel.limpiarMensaje() },
                                onBackClick = { navController.popBackStack() },
                                onAgregarClick = { navController.navigate("formulario_jugador") },
                                onEditarClick = { jugador ->
                                    navController.navigate("formulario_jugador?id=${jugador.idJugador}")
                                },
                                onEliminarClick = { id -> jugadorViewModel.eliminar(id) }
                            )
                        }

                        composable(
                            route = "formulario_jugador?id={id}",
                            arguments = listOf(
                                navArgument("id") { 
                                    type = NavType.StringType
                                    nullable = true 
                                }
                            )
                        ) { backStackEntry ->
                            val idStr = backStackEntry.arguments?.getString("id")
                            val id = idStr?.toLongOrNull()
                            
                            val jugadores by jugadorViewModel.jugadores.collectAsState()
                            val equipos by equipoViewModel.equipos.collectAsState()

                            val jugadorEditar = jugadores.find { it.idJugador == id }

                            FormularioJugadorScreen(
                                jugador = jugadorEditar,
                                equipos = equipos,
                                jugadoresExistentes = jugadores,
                                onBackClick = { navController.popBackStack() },
                                onGuardarClick = { jugador ->
                                    if (id == null) {
                                        jugadorViewModel.guardar(jugador)
                                    } else {
                                        jugadorViewModel.actualizar(id, jugador)
                                    }
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("equipos") {
                            val equipos by equipoViewModel.equipos.collectAsState()
                            val searchText by equipoViewModel.search.collectAsState()

                            EquipoScreen(
                                equipos = equipos,
                                searchText = searchText,
                                onSearchChange = { equipoViewModel.onSearchChange(it) },
                                onBackClick = { navController.popBackStack() },
                                onAgregarClick = { navController.navigate("crear_equipo") },
                                onEliminarClick = { id -> equipoViewModel.eliminarEquipo(id) }
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
                                onBackClick = { navController.popBackStack() },
                                onGuardarClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
