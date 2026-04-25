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
import com.example.football_team_frontend.viewmodel.PartidoViewModel
import com.example.football_team_frontend.viewmodel.EntrenadorViewModel

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
                    val partidoViewModel: PartidoViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "inicio",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("inicio") {
                            InicioScreen(
                                onEquiposClick = { navController.navigate("equipos") },
                                onJugadoresClick = { navController.navigate("jugadores") },
                                onPartidosClick = { navController.navigate("partidos") },
                                onEntrenadoresClick = { /* Próximamente */ },
                                onEstadisticasClick = { /* Próximamente */ }
                            )
                        }

                        composable("partidos") {
                            val resultados by partidoViewModel.resultados.collectAsState()
                            val jugadores by jugadorViewModel.jugadores.collectAsState()
                            val equipos by equipoViewModel.equipos.collectAsState()
                            val cargando by partidoViewModel.cargando.collectAsState()
                            val mensaje by partidoViewModel.mensaje.collectAsState()

                            LaunchedEffect(Unit) {
                                partidoViewModel.obtenerResultados()
                                partidoViewModel.listar()
                                if (jugadores.isEmpty()) jugadorViewModel.listar()
                                if (equipos.isEmpty()) equipoViewModel.cargarEquipos()
                            }

                            PartidosScreen(
                                resultados = resultados,
                                jugadores = jugadores,
                                equipos = equipos,
                                cargando = cargando,
                                mensaje = mensaje,
                                onDismissMensaje = { partidoViewModel.limpiarMensaje() },
                                onBackClick = { navController.popBackStack() },
                                onRefrescarClick = { partidoViewModel.obtenerResultados() },
                                onAgregarClick = { navController.navigate("formulario_partido") },
                                onDetalleClick = { id -> navController.navigate("detalle_partido/$id") }
                            )
                        }

                        composable(
                            route = "detalle_partido/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                            val resultados by partidoViewModel.resultados.collectAsState()
                            val jugadores by jugadorViewModel.jugadores.collectAsState()
                            val equipos by equipoViewModel.equipos.collectAsState()

                            val resultado = resultados.find { it.idPartido == id }

                            DetallePartidoScreen(
                                resultado = resultado,
                                jugadores = jugadores,
                                equipos = equipos,
                                onBackClick = { navController.popBackStack() },
                                onEditarClick = { idPartido ->
                                    navController.navigate("formulario_partido?id=$idPartido")
                                },
                                onEliminarClick = { idPartido ->
                                    partidoViewModel.eliminar(idPartido)
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = "formulario_partido?id={id}",
                            arguments = listOf(navArgument("id") { 
                                type = NavType.StringType
                                nullable = true 
                            })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                            val equipos by equipoViewModel.equipos.collectAsState()
                            val partidos by partidoViewModel.partidos.collectAsState()
                            val guardadoExitoso by partidoViewModel.guardadoExitoso.collectAsState()
                            val mensaje by partidoViewModel.mensaje.collectAsState()

                            val partidoEditar = if (id != null) partidos.find { it.idPartido == id } else null

                            LaunchedEffect(id) {
                                if (id != null && partidos.isEmpty()) {
                                    partidoViewModel.listar()
                                }
                                if (equipos.isEmpty()) {
                                    equipoViewModel.cargarEquipos()
                                }
                            }

                            LaunchedEffect(guardadoExitoso) {
                                if (guardadoExitoso) {
                                    navController.popBackStack()
                                    partidoViewModel.resetGuardado()
                                }
                            }

                            FormularioPartidoScreen(
                                partido = partidoEditar,
                                equipos = equipos,
                                isEditMode = id != null,
                                mensajeExterno = mensaje,
                                onBackClick = { 
                                    partidoViewModel.limpiarMensaje()
                                    navController.popBackStack() 
                                },
                                onGuardarClick = { partido ->
                                    if (id == null) {
                                        partidoViewModel.guardar(partido)
                                    } else {
                                        partidoViewModel.actualizar(id, partido)
                                    }
                                }
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
                                onRefrescarClick = { jugadorViewModel.listar() },
                                onDetalleClick = { jugador ->
                                    navController.navigate("detalle_jugador/${jugador.idJugador}")
                                },
                                onAgregarClick = { navController.navigate("formulario_jugador") }
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
                            val guardadoExitoso by jugadorViewModel.guardadoExitoso.collectAsState()

                            LaunchedEffect(guardadoExitoso) {
                                if (guardadoExitoso) {
                                    navController.popBackStack()
                                    jugadorViewModel.resetGuardado()
                                }
                            }

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
                                }
                            )
                        }

                        composable(
                            route = "detalle_jugador/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                            val jugadores by jugadorViewModel.jugadores.collectAsState()
                            val equipos by equipoViewModel.equipos.collectAsState()
                            
                            val jugador = jugadores.find { it.idJugador == id }
                            val equipo = equipos.find { it.idEquipo == jugador?.idEquipo }

                            DetalleJugadorScreen(
                                jugador = jugador,
                                equipo = equipo,
                                onBackClick = { navController.popBackStack() },
                                onEditarClick = { 
                                    navController.navigate("formulario_jugador?id=${id}")
                                },
                                onEliminarClick = { idJugador ->
                                    jugadorViewModel.eliminar(idJugador)
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
