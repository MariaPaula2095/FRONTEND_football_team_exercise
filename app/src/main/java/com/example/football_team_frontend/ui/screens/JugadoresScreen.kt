package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.ui.theme.VerdeOscuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadoresScreen(
    jugadores: List<Jugador>,
    equipos: List<Equipo>,
    cargando: Boolean = false,
    mensaje: String? = null,
    onDismissMensaje: () -> Unit = {},
    onBackClick: () -> Unit,
    onAgregarClick: () -> Unit,
    onEditarClick: (Jugador) -> Unit,
    onEliminarClick: (Long) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedEquipoId by remember { mutableStateOf<Long?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar mensaje si existe
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            onDismissMensaje()
        }
    }

    // Estado para el diálogo de eliminación
    var jugadorParaEliminar by remember { mutableStateOf<Jugador?>(null) }

    // Lógica de filtrado combinada
    val jugadoresFiltrados = remember(searchQuery, selectedEquipoId, jugadores) {
        jugadores.filter { jugador ->
            val matchesSearch = jugador.nombre.contains(searchQuery, ignoreCase = true)
            val matchesEquipo = selectedEquipoId == null || jugador.idEquipo == selectedEquipoId
            matchesSearch && matchesEquipo
        }
    }

    if (jugadorParaEliminar != null) {
        AlertDialog(
            onDismissRequest = { jugadorParaEliminar = null },
            title = { Text("Confirmar eliminación", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar al jugador ${jugadorParaEliminar?.nombre}? Esta acción es irreversible.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        jugadorParaEliminar?.idJugador?.let { onEliminarClick(it) }
                        jugadorParaEliminar = null
                    }
                ) {
                    Text("ELIMINAR", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { jugadorParaEliminar = null }) {
                    Text("CANCELAR", color = Color.Gray)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("LISTA DE JUGADORES", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdeOscuro)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Jugadores",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                FloatingActionButton(
                    onClick = onAgregarClick,
                    containerColor = VerdeOscuro,
                    contentColor = Color.White,
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Filtrar por equipo", fontSize = 14.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text(
                        text = equipos.find { it.idEquipo == selectedEquipoId }?.nombre ?: "Todos los equipos",
                        color = Color.Black
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    DropdownMenuItem(
                        text = { Text("Todos los equipos") },
                        onClick = {
                            selectedEquipoId = null
                            expanded = false
                        }
                    )
                    equipos.forEach { equipo ->
                        DropdownMenuItem(
                            text = { Text(equipo.nombre) },
                            onClick = {
                                selectedEquipoId = equipo.idEquipo
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar por nombre...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeOscuro,
                    unfocusedBorderColor = Color.LightGray,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    cursorColor = VerdeOscuro
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = VerdeOscuro)
                }
            } else if (jugadoresFiltrados.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron jugadores", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(jugadoresFiltrados) { jugador ->
                        val equipoNombre = equipos.find { it.idEquipo == jugador.idEquipo }?.nombre ?: "Sin equipo"
                        JugadorItem(
                            jugador = jugador,
                            equipoNombre = equipoNombre,
                            onEdit = { onEditarClick(jugador) },
                            onDelete = { jugadorParaEliminar = jugador }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JugadorItem(
    jugador: Jugador,
    equipoNombre: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(VerdeOscuro.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = VerdeOscuro, modifier = Modifier.size(30.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = jugador.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "${jugador.posicion} • #${jugador.dorsal}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = equipoNombre,
                    color = VerdeOscuro,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.DarkGray)
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFE53935))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JugadoresScreenPreview() {
    val jugadoresDePrueba = listOf(
        Jugador(idJugador = 1, nombre = "Juan David Pérez", posicion = "Delantero", dorsal = 9, idEquipo = 1),
        Jugador(idJugador = 2, nombre = "Santiago Gómez", posicion = "Volante", dorsal = 10, idEquipo = 1)
    )
    val equiposDePrueba = listOf(
        Equipo(idEquipo = 1, nombre = "Atlético Nacional", ciudad = "Medellín", fundacion = java.util.Date())
    )

    JugadoresScreen(
        jugadores = jugadoresDePrueba,
        equipos = equiposDePrueba,
        onBackClick = {},
        onAgregarClick = {},
        onEditarClick = {},
        onEliminarClick = {}
    )
}
