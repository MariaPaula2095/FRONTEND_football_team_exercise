package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.ui.theme.*

@Composable
fun JugadoresScreen(
    jugadores: List<Jugador>,
    equipos: List<Equipo>,
    cargando: Boolean = false,
    mensaje: String? = null,
    onDismissMensaje: () -> Unit = {},
    onBackClick: () -> Unit,
    onRefrescarClick: () -> Unit,
    onAgregarClick: () -> Unit,
    onDetalleClick: (Jugador) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedEquipoId by remember { mutableStateOf<Long?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            onDismissMensaje()
        }
    }

    Scaffold(
        containerColor = VerdeOscuro,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarClick,
                containerColor = Verde,
                contentColor = Blanco,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cabecera
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.background(Color(0xFF1F4A43), CircleShape).size(36.dp)
                ) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Atrás", tint = Blanco, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Jugadores",
                    color = Blanco,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onRefrescarClick,
                    modifier = Modifier.background(Color(0xFF1F4A43), CircleShape).size(36.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar", tint = Verde, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por nombre...", color = GrisClaro) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Verde) },
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1F4A43),
                    unfocusedContainerColor = Color(0xFF1F4A43),
                    focusedBorderColor = Verde,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Blanco,
                    unfocusedTextColor = Blanco
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtro por Equipo
            Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    onClick = { expanded = true },
                    color = Color(0xFF1F4A43),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = null, tint = Verde)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = equipos.find { it.idEquipo == selectedEquipoId }?.nombre ?: "Todos los equipos",
                            color = Blanco,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = GrisClaro)
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color(0xFF1F4A43)).fillMaxWidth(0.8f)
                ) {
                    DropdownMenuItem(
                        text = { Text("Todos los equipos", color = Blanco) },
                        onClick = { selectedEquipoId = null; expanded = false }
                    )
                    equipos.forEach { equipo ->
                        DropdownMenuItem(
                            text = { Text(equipo.nombre, color = Blanco) },
                            onClick = { selectedEquipoId = equipo.idEquipo; expanded = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Verde)
                }
            } else {
                val filtrados = jugadores.filter {
                    it.nombre.contains(searchQuery, ignoreCase = true) &&
                    (selectedEquipoId == null || it.idEquipo == selectedEquipoId)
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(filtrados) { jugador ->
                        val equipo = equipos.find { it.idEquipo == jugador.idEquipo }?.nombre ?: "Sin equipo"
                        JugadorCard(
                            jugador = jugador,
                            equipoNombre = equipo,
                            onClick = { onDetalleClick(jugador) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JugadorCard(jugador: Jugador, equipoNombre: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F4A43))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(50.dp).background(Verde.copy(0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Verde)
            }
            
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(jugador.nombre, color = Blanco, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${jugador.posicion} • #${jugador.dorsal}", color = GrisClaro, fontSize = 13.sp)
                Text(equipoNombre, color = Verde, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = GrisClaro)
        }
    }
}
