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
import com.example.football_team_frontend.ui.components.FormTextField
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
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val selectedJugador = jugadores.find { it.idJugador == idJugador }
    val selectedPartido = partidos.find { it.idPartido == idPartido }

    fun validar() {
        if (idJugador == null || idPartido == null) {
            errorMsg = "POR FAVOR SELECCIONA UN JUGADOR Y UN PARTIDO."
            return
        }
        errorMsg = null
        mostrarConfirmacion = true
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            containerColor = FichaFondo,
            shape = RoundedCornerShape(20.dp),
            title = { Text("¿GUARDAR ESTADÍSTICA?", color = Blanco, fontWeight = FontWeight.Black, fontSize = 16.sp) },
            text = { Text("Se registrarán las estadísticas de ${selectedJugador?.nombre} en el partido seleccionado.", color = TextoSec, fontSize = 14.sp) },
            confirmButton = {
                TextButton(onClick = {
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
                    mostrarConfirmacion = false
                }) {
                    Text("ACEPTAR", color = Verde, fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("CANCELAR", color = TextoSec)
                }
            }
        )
    }

    Scaffold(
        containerColor = SuperficieAlt,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FondoOscuro)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBackIosNew, null, tint = Blanco, modifier = Modifier.size(18.dp))
                }
                Text(
                    text = if (isEditMode) "EDITAR ESTADÍSTICA" else "NUEVA ESTADÍSTICA",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Blanco,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            if (errorMsg != null) {
                Text(errorMsg!!, color = ErrorRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Selección de Partido
            Text("PARTIDO", color = TextoSec, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expandedPartidos,
                onExpandedChange = { expandedPartidos = it }
            ) {
                OutlinedTextField(
                    value = (selectedPartido?.let { "${it.equipoLocal} vs ${it.equipoVisitante}" } ?: "SELECCIONAR PARTIDO").uppercase(),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPartidos) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FichaFondo,
                        unfocusedContainerColor = FichaFondo,
                        focusedBorderColor = Verde,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Blanco,
                        unfocusedTextColor = Blanco,
                        focusedLabelColor = Verde,
                        unfocusedLabelColor = TextoSec
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedPartidos,
                    onDismissRequest = { expandedPartidos = false },
                    modifier = Modifier.background(FichaFondo)
                ) {
                    partidos.forEach { partido ->
                        DropdownMenuItem(
                            text = { Text("${partido.equipoLocal} VS ${partido.equipoVisitante} (${partido.fecha})", color = Blanco, fontWeight = FontWeight.Bold) },
                            onClick = {
                                idPartido = partido.idPartido
                                expandedPartidos = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Selección de Jugador
            Text("JUGADOR", color = TextoSec, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expandedJugadores,
                onExpandedChange = { expandedJugadores = it }
            ) {
                OutlinedTextField(
                    value = (selectedJugador?.nombre ?: "SELECCIONAR JUGADOR").uppercase(),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJugadores) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FichaFondo,
                        unfocusedContainerColor = FichaFondo,
                        focusedBorderColor = Verde,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Blanco,
                        unfocusedTextColor = Blanco,
                        focusedLabelColor = Verde,
                        unfocusedLabelColor = TextoSec
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedJugadores,
                    onDismissRequest = { expandedJugadores = false },
                    modifier = Modifier.background(FichaFondo)
                ) {
                    jugadores.forEach { jugador ->
                        DropdownMenuItem(
                            text = { Text(jugador.nombre.uppercase(), color = Blanco, fontWeight = FontWeight.Bold) },
                            onClick = {
                                idJugador = jugador.idJugador
                                expandedJugadores = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("MÉTRICAS DEL DESEMPEÑO", color = TextoSec, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Campos Numéricos
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FormTextField(
                    label = "GOLES",
                    value = goles,
                    onValueChange = { newVal: String -> if (newVal.all { c: Char -> c.isDigit() }) goles = newVal },
                    icon = Icons.Default.SportsFootball,
                    modifier = Modifier.weight(1f)
                )
                FormTextField(
                    label = "ASISTENCIAS",
                    value = asistencias,
                    onValueChange = { newVal: String -> if (newVal.all { c: Char -> c.isDigit() }) asistencias = newVal },
                    icon = Icons.Default.Handshake,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FormTextField(
                    label = "T. AMARILLAS",
                    value = amarillas,
                    onValueChange = { newVal: String -> if (newVal.all { c: Char -> c.isDigit() }) amarillas = newVal },
                    icon = Icons.Default.Warning,
                    modifier = Modifier.weight(1f)
                )
                FormTextField(
                    label = "T. ROJAS",
                    value = rojas,
                    onValueChange = { newVal: String -> if (newVal.all { c: Char -> c.isDigit() }) rojas = newVal },
                    icon = Icons.Default.Dangerous,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FormTextField(
                label = "MINUTOS JUGADOS",
                value = minutos,
                onValueChange = { newVal: String -> if (newVal.all { c: Char -> c.isDigit() }) minutos = newVal },
                icon = Icons.Default.Timer
            )


            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { validar() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verde)
            ) {
                Text("GUARDAR REGISTRO", fontWeight = FontWeight.Black, color = Blanco, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("CANCELAR", color = TextoSec, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

