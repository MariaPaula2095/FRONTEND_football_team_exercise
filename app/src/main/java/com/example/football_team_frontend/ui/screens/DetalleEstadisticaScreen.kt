package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.EstadisticasJugadorDto
import com.example.football_team_frontend.model.ResultadoPartido
import com.example.football_team_frontend.ui.theme.*

@Composable
fun DetalleEstadisticaScreen(
    estadistica: EstadisticasJugadorDto?,
    partido: ResultadoPartido?,
    onBackClick: () -> Unit,
    onEditarClick: (EstadisticasJugadorDto) -> Unit,
    onEliminarClick: (Long) -> Unit
) {
    if (estadistica == null) {
        Box(modifier = Modifier.fillMaxSize().background(VerdeOscuro), contentAlignment = Alignment.Center) {
            Text("No se encontró la información", color = Blanco)
        }
        return
    }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            containerColor = CardColor,
            titleContentColor = Blanco,
            textContentColor = GrisClaro,
            title = { Text("¿Eliminar registro?", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar las estadísticas de ${estadistica.nombreJugador} en este partido?") },
            confirmButton = {
                TextButton(onClick = {
                    estadistica.idEstadistica?.let { onEliminarClick(it) }
                    mostrarDialogoEliminar = false
                }) {
                    Text("ELIMINAR", color = Color(0xFFFF4D4D), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("CANCELAR", color = Blanco)
                }
            }
        )
    }

    Scaffold(
        containerColor = VerdeOscuro,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.background(CardColor, CircleShape).size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Atrás", tint = Blanco, modifier = Modifier.size(18.dp))
                }
                
                Text(
                    text = "DETALLE DE RENDIMIENTO",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Blanco,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Row(modifier = Modifier.align(Alignment.CenterEnd), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { onEditarClick(estadistica) },
                        modifier = Modifier.background(Verde, CircleShape).size(40.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Blanco, modifier = Modifier.size(20.dp))
                    }
                    IconButton(
                        onClick = { mostrarDialogoEliminar = true },
                        modifier = Modifier.background(Color(0xFF780000), CircleShape).size(40.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Blanco, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Info de Cabecera
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardColor)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = estadistica.nombreJugador?.uppercase() ?: "JUGADOR",
                        color = Blanco,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    
                    val partidoLabel = if (partido != null) {
                        "${partido.equipoLocal} vs ${partido.equipoVisitante}"
                    } else {
                        "Partido #${estadistica.idPartido}"
                    }
                    
                    Text(
                        text = partidoLabel,
                        color = Verde,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (partido != null) {
                        Text(
                            text = partido.fecha,
                            color = GrisClaro,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "MÉTRICAS DEL PARTIDO",
                color = Blanco,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Rejilla de Estadísticas
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricBox(Modifier.weight(1f), "GOLES", "${estadistica.goles ?: 0}", Icons.Default.SportsSoccer, Verde)
                    MetricBox(Modifier.weight(1f), "ASISTENCIAS", "${estadistica.asistencias ?: 0}", Icons.Default.Star, Color(0xFF64B5F6))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricBox(Modifier.weight(1f), "T. AMARILLAS", "${estadistica.tarjetasAmarillas ?: 0}", Icons.Default.Square, Color.Yellow)
                    MetricBox(Modifier.weight(1f), "T. ROJAS", "${estadistica.tarjetasRojas ?: 0}", Icons.Default.Square, Color.Red)
                }
                MetricBox(Modifier.fillMaxWidth(), "MINUTOS JUGADOS", "${estadistica.minutosJugados ?: 0} min", Icons.Default.Timer, Blanco)
            }
        }
    }
}

@Composable
fun MetricBox(modifier: Modifier, label: String, value: String, icon: ImageVector, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(label, color = GrisClaro, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(value, color = Blanco, fontSize = 18.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}
