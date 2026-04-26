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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.football_team_frontend.ui.screens.*
import com.example.football_team_frontend.ui.theme.FootballTheme
import com.example.football_team_frontend.viewmodel.EntrenadorViewModel
import com.example.football_team_frontend.viewmodel.EquipoViewModel
import com.example.football_team_frontend.viewmodel.EstadisticaViewModel
import com.example.football_team_frontend.viewmodel.JugadorViewModel
import com.example.football_team_frontend.viewmodel.PartidoViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FootballTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val routeActual = currentBackStack?.destination?.route

                Scaffold(
                    modifier  = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (routeActual != "inicio" && routeActual != "splash") {
                            BottomNavigationBar(
                                currentRoute        = routeActual,
                                onInicioClick       = { navController.navigate("inicio") { popUpTo("inicio") { inclusive = true } } },
                                onJugadoresClick    = { navController.navigate("jugadores") { popUpTo("inicio") } },
                                onEquiposClick      = { navController.navigate("equipos") { popUpTo("inicio") } },
                                onEntrenadoresClick = { navController.navigate("entrenadores") { popUpTo("inicio") } }
                            )
                        }
                    }
                ) { innerPadding ->
                    val jugadorViewModel: JugadorViewModel = viewModel()
                    val equipoViewModel: EquipoViewModel = viewModel()
                    val partidoViewModel: PartidoViewModel = viewModel()
                    val estadisticaViewModel: EstadisticaViewModel = viewModel()
                    val entrenadorViewModel: EntrenadorViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                                modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("splash") {
                            SplashScreen(
                                onSplashFinished = {
                                    navController.navigate("inicio") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("inicio") {
                            InicioScreen(
                                onEquiposClick = { navController.navigate("equipos") },
                                onJugadoresClick = { navController.navigate("jugadores") },
                                onPartidosClick = { navController.navigate("partidos") },
                                onEntrenadoresClick = { navController.navigate("entrenadores") },
                                onEstadisticasClick = { navController.navigate("estadisticas") }
                            )
                        }

                        composable("estadisticas") {
                            val estadisticas by estadisticaViewModel.estadisticas.collectAsState()
                            val partidos by partidoViewModel.resultados.collectAsState()
                            val cargando by estadisticaViewModel.cargando.collectAsState()
                            val mensaje by estadisticaViewModel.mensaje.collectAsState()
                            val jugadoresConMasGoles by estadisticaViewModel.jugadoresConMasGoles.collectAsState() // ← nueva

                            LaunchedEffect(Unit) {
                                estadisticaViewModel.obtenerEstadisticas()
                                if (partidos.isEmpty()) partidoViewModel.obtenerResultados()
                            }

                            EstadisticasScreen(
                                estadisticas         = estadisticas,
                                partidos             = partidos,
                                cargando             = cargando,
                                mensaje              = mensaje,
                                jugadoresConMasGoles = jugadoresConMasGoles,                          // ← nueva
                                onBuscarPorGoles     = { estadisticaViewModel.buscarJugadoresPorGoles(it) }, // ← nueva
                                onDismissMensaje     = { estadisticaViewModel.limpiarMensaje() },
                                onBackClick          = { navController.popBackStack() },
                                onRefrescarClick     = { estadisticaViewModel.obtenerEstadisticas() },
                                onAgregarClick       = { navController.navigate("formulario_estadistica") },
                                onDetalleClick       = { stat -> navController.navigate("detalle_estadistica/${stat.idEstadistica}") }
                            )
                        }

                        composable(
                            route = "detalle_estadistica/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                            val estadisticas by estadisticaViewModel.estadisticas.collectAsState()
                            val partidos by partidoViewModel.resultados.collectAsState()

                            val estadistica = estadisticas.find { it.idEstadistica == id }
                            val partido = partidos.find { it.idPartido == estadistica?.idPartido }

                            DetalleEstadisticaScreen(
                                estadistica = estadistica,
                                partido = partido,
                                onBackClick = { navController.popBackStack() },
                                onEditarClick = { stat ->
                                    navController.navigate("formulario_estadistica?id=${stat.idEstadistica}")
                                },
                                onEliminarClick = { idEstadistica ->
                                    estadisticaViewModel.eliminarEstadistica(idEstadistica)
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = "formulario_estadistica?id={id}",
                            arguments = listOf(navArgument("id") { 
                                type = NavType.StringType
                                nullable = true 
                            })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                            val estadisticas by estadisticaViewModel.estadisticas.collectAsState()
                            val jugadores by jugadorViewModel.jugadores.collectAsState()
                            val partidos by partidoViewModel.resultados.collectAsState()
                            val guardadoExitoso by estadisticaViewModel.guardadoExitoso.collectAsState()

                            val estadisticaEditar = if (id != null) estadisticas.find { it.idEstadistica == id } else null

                            LaunchedEffect(Unit) {
                                if (jugadores.isEmpty()) jugadorViewModel.listar()
                                if (partidos.isEmpty()) partidoViewModel.obtenerResultados()
                            }

                            LaunchedEffect(guardadoExitoso) {
                                if (guardadoExitoso) {
                                    navController.popBackStack()
                                    estadisticaViewModel.resetGuardado()
                                }
                            }

                            FormularioEstadisticaScreen(
                                estadistica = estadisticaEditar,
                                jugadores = jugadores,
                                partidos = partidos,
                                isEditMode = id != null,
                                onBackClick = { navController.popBackStack() },
                                onGuardarClick = { dto ->
                                    if (id == null) {
                                        estadisticaViewModel.guardarEstadistica(dto)
                                    } else {
                                        estadisticaViewModel.actualizarEstadistica(id, dto)
                                    }
                                }
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
                            val equipos     by equipoViewModel.equipos.collectAsState()
                            val searchQuery by equipoViewModel.search.collectAsState()
                            val cargando    by equipoViewModel.isLoading.collectAsState()
                            val error       by equipoViewModel.error.collectAsState()

                            EquiposScreen(
                                equipos          = equipos,
                                searchQuery      = searchQuery,
                                cargando         = cargando,
                                mensaje          = error,
                                onDismissMensaje = {},
                                onSearchChange   = { equipoViewModel.onSearchChange(it) },
                                onBackClick      = { navController.popBackStack() },
                                onRefrescarClick = { equipoViewModel.cargarEquipos() },
                                onAgregarClick   = { navController.navigate("crear_equipo") },
                                onDetalleClick   = { equipo -> navController.navigate("detalle_equipo/${equipo.idEquipo}") }
                            )
                        }

                        composable("crear_equipo") {
                            FormularioEquipoScreen(
                                equipo         = null,
                                onBackClick    = { navController.popBackStack() },
                                onGuardarClick = { equipo ->
                                    equipoViewModel.guardarEquipo(equipo)
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("detalle_equipo/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                            val equipos by equipoViewModel.equipos.collectAsState()
                            val equipo = equipos.find { it.idEquipo == id }

                            DetalleEquipoScreen(
                                equipo          = equipo,
                                onBackClick     = { navController.popBackStack() },
                                onEditarClick   = { navController.navigate("editar_equipo/$id") },
                                onEliminarClick = { equipoId ->
                                    equipoViewModel.eliminarEquipo(equipoId)
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("editar_equipo/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                            val equipos by equipoViewModel.equipos.collectAsState()
                            val equipo = equipos.find { it.idEquipo == id }

                            FormularioEquipoScreen(
                                equipo         = equipo,
                                onBackClick    = { navController.popBackStack() },
                                onGuardarClick = { equipoActualizado ->
                                    equipoViewModel.actualizarEquipo(id!!, equipoActualizado)
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("entrenadores") {
                            val entrenadores by entrenadorViewModel.entrenadores.collectAsState()
                            val searchQuery  by entrenadorViewModel.search.collectAsState()
                            val cargando     by entrenadorViewModel.isLoading.collectAsState()
                            val error        by entrenadorViewModel.error.collectAsState()

                            EntrenadoresScreen(
                                entrenadores     = entrenadores,
                                searchQuery      = searchQuery,
                                cargando         = cargando,
                                mensaje          = error,
                                onDismissMensaje = {},
                                onSearchChange   = { entrenadorViewModel.onSearchChange(it) },
                                onBackClick      = { navController.popBackStack() },
                                onRefrescarClick = { entrenadorViewModel.cargarEntrenadores() },
                                onAgregarClick   = { navController.navigate("crear_entrenador") },
                                onDetalleClick   = { entrenador -> navController.navigate("detalle_entrenador/${entrenador.idEntrenador}") }
                            )
                        }

                        composable("crear_entrenador") {
                            val equipos by equipoViewModel.equipos.collectAsState()

                            FormularioEntrenadorScreen(
                                entrenador     = null,
                                equipos        = equipos,
                                onBackClick    = { navController.popBackStack() },
                                onGuardarClick = { entrenador ->
                                    entrenadorViewModel.guardarEntrenador(entrenador)
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("detalle_entrenador/{id}") { backStackEntry ->
                            val id           = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                            val entrenadores by entrenadorViewModel.entrenadores.collectAsState()
                            val entrenador   = entrenadores.find { it.idEntrenador == id }

                            DetalleEntrenadorScreen(
                                entrenador      = entrenador,
                                onBackClick     = { navController.popBackStack() },
                                onEditarClick   = { navController.navigate("editar_entrenador/$id") },
                                onEliminarClick = { entrenadorId ->
                                    entrenadorViewModel.eliminarEntrenador(entrenadorId)
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("editar_entrenador/{id}") { backStackEntry ->
                            val id           = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                            val entrenadores by entrenadorViewModel.entrenadores.collectAsState()
                            val equipos      by equipoViewModel.equipos.collectAsState()
                            val entrenador   = entrenadores.find { it.idEntrenador == id }

                            FormularioEntrenadorScreen(
                                entrenador     = entrenador,
                                equipos        = equipos,
                                onBackClick    = { navController.popBackStack() },
                                onGuardarClick = { entrenadorActualizado ->
                                    entrenadorViewModel.actualizarEntrenador(id!!, entrenadorActualizado)
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
