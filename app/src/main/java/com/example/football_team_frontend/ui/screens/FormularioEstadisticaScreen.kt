package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.football_team_frontend.model.*
import com.example.football_team_frontend.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioEstadisticaScreen(
    estadistica: EstadisticasJugadorDto? = null,
    jugadores: List<Jugador>,
    partidos: List<ResultadoPartido>,
    isEditMode: Boolean,
    onBackClick: () -> Unit,
    onGuardarClick: (EstadisticasJugadorDto) -> Unit
) {
    var idJugador by remember { mutableStateOf(estadistica?.idJugador) }
    var idPartido by remember { mutableStateOf(estadistica?.idPartido) }
    var minutos by remember { mutableStateOf(estadistica?.minutosJugados?.toString() ?: "0") }
    var goles by remember { mutableStateOf(estadistica?.goles?.toString() ?: "0") }
    var asistencias by remember { mutableStateOf(estadistica?.asistencias?.toString() ?: "0") }
    var amarillas by remember { mutableStateOf(estadistica?.tarjetasAmarillas?.toString() ?: "0") }
    var rojas by remember { mutableStateOf(estadistica?.tarjetasRojas?.toString() ?: "0") }

    var expandedJugadores by remember { mutableStateOf(false) }
    var expandedPartidos by remember { mutableStateOf(false) }

    val selectedJugador = jugadores.find { it.idJugador == idJugador }
    val selectedPartido = partidos.find { it.idPartido == idPartido }

    Scaffold(
        containerColor = VerdeOscuro,
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "EDITAR ESTADÍSTICA" else "NUEVA ESTADÍSTICA", color = Blanco, fontSize = 18.sp, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Atrás", tint = Blanco)
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Selección de Partido
            Text("PARTIDO", color = Verde, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            ExposedDropdownMenuBox(
                expanded = expandedPartidos,
                onExpandedChange = { expandedPartidos = it }
            ) {
                OutlinedTextField(
                    value = selectedPartido?.let { "${it.equipoLocal} vs ${it.equipoVisitante}" } ?: "Seleccionar Partido",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Blanco,
                        unfocusedTextColor = Blanco,
                        unfocusedBorderColor = CardColor,
                        focusedBorderColor = Verde
                    ),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPartidos) }
                )
                ExposedDropdownMenu(
                    expanded = expandedPartidos,
                    onDismissRequest = { expandedPartidos = false },
                    modifier = Modifier.background(CardColor)
                ) {
                    partidos.forEach { partido ->
                        DropdownMenuItem(
                            text = { Text("${partido.equipoLocal} vs ${partido.equipoVisitante} (${partido.fecha})", color = Blanco) },
                            onClick = {
                                idPartido = partido.idPartido
                                expandedPartidos = false
                            }
                        )
                    }
                }
            }

            // Selección de Jugador
            Text("JUGADOR", color = Verde, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            ExposedDropdownMenuBox(
                expanded = expandedJugadores,
                onExpandedChange = { expandedJugadores = it }
            ) {
                OutlinedTextField(
                    value = selectedJugador?.nombre ?: "Seleccionar Jugador",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Blanco,
                        unfocusedTextColor = Blanco,
                        unfocusedBorderColor = CardColor,
                        focusedBorderColor = Verde
                    ),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJugadores) }
                )
                ExposedDropdownMenu(
                    expanded = expandedJugadores,
                    onDismissRequest = { expandedJugadores = false },
                    modifier = Modifier.background(CardColor)
                ) {
                    jugadores.forEach { jugador ->
                        DropdownMenuItem(
                            text = { Text(jugador.nombre, color = Blanco) },
                            onClick = {
                                idJugador = jugador.idJugador
                                expandedJugadores = false
                            }
                        )
                    }
                }
            }

            // Campos Numéricos
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("GOLES", color = Verde, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = goles,
                        onValueChange = { if(it.all { char -> char.isDigit() }) goles = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Blanco, unfocusedTextColor = Blanco)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("ASISTENCIAS", color = Verde, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = asistencias,
                        onValueChange = { if(it.all { char -> char.isDigit() }) asistencias = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Blanco, unfocusedTextColor = Blanco)
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("T. AMARILLAS", color = Color.Yellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = amarillas,
                        onValueChange = { if(it.all { char -> char.isDigit() }) amarillas = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Blanco, unfocusedTextColor = Blanco)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("T. ROJAS", color = Color.Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = rojas,
                        onValueChange = { if(it.all { char -> char.isDigit() }) rojas = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Blanco, unfocusedTextColor = Blanco)
                    )
                }
            }

            Text("MINUTOS JUGADOS", color = Verde, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = minutos,
                onValueChange = { if(it.all { char -> char.isDigit() }) minutos = it },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Blanco, unfocusedTextColor = Blanco)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val dto = EstadisticasJugadorDto(
                        idEstadistica = estadistica?.idEstadistica,
                        idJugador = idJugador,
                        nombreJugador = selectedJugador?.nombre,
                        idPartido = idPartido,
                        minutosJugados = minutos.toIntOrNull() ?: 0,
                        goles = goles.toIntOrNull() ?: 0,
                        asistencias = asistencias.toIntOrNull() ?: 0,
                        tarjetasAmarillas = amarillas.toIntOrNull() ?: 0,
                        tarjetasRojas = rojas.toIntOrNull() ?: 0
                    )
                    onGuardarClick(dto)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verde)
            ) {
                Text("GUARDAR REGISTRO", fontWeight = FontWeight.Black)
            }
        }
    }
}
