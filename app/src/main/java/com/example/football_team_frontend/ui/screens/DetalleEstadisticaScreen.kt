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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.EstadisticasJugadorDto
import com.example.football_team_frontend.model.ResultadoPartido
import com.example.football_team_frontend.ui.components.MetricBox
import com.example.football_team_frontend.ui.components.MetricBoxRow
import com.example.football_team_frontend.ui.theme.*

@Composable
fun DetalleEstadisticaScreen(
    estadistica: EstadisticasJugadorDto?,
    partido: ResultadoPartido?,
    onBackClick: () -> Unit,
    onEditarClick: (EstadisticasJugadorDto) -> Unit,
    onEliminarClick: (Long) -> Unit
) {
    // ── Estado vacío ──────────────────────────────────────────────────────
    if (estadistica == null) {
        Box(
            modifier         = Modifier.fillMaxSize().background(SuperficieAlt),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier         = Modifier
                        .size(72.dp)
                        .background(Superficie, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.BarChart, null, tint = TextoSec, modifier = Modifier.size(36.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("Estadística no encontrada", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("No hay información disponible", color = TextoSec, fontSize = 13.sp)
            }
        }
        return
    }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    // ── Diálogo confirmar eliminación ─────────────────────────────────────
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            containerColor   = FichaFondo,
            shape            = RoundedCornerShape(20.dp),
            title = {
                Text("¿ELIMINAR REGISTRO?", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
            },
            text = {
                Text(
                    "Se eliminará este registro de rendimiento para ${estadistica.nombreJugador}. Esta acción no se puede deshacer.",
                    color = TextoSec, fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    estadistica.idEstadistica?.let { onEliminarClick(it) }
                    mostrarDialogoEliminar = false
                }) {
                    Text("ELIMINAR", color = ErrorRed, fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
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
                    onClick  = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Atrás",
                        tint               = Color.White,
                        modifier           = Modifier.size(18.dp)
                    )
                }
                
                Text(
                    text = "DETALLE DE RENDIMIENTO",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White,
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
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header con info de jugador y partido ──────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(listOf(FondoOscuro, SuperficieAlt))
                    )
                    .padding(horizontal = 20.dp, vertical = 30.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier         = Modifier
                            .size(80.dp)
                            .background(Verde.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.SportsSoccer, null, tint = Verde, modifier = Modifier.size(40.dp))
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        estadistica.nombreJugador?.uppercase() ?: "JUGADOR",
                        color      = Color.White,
                        fontSize   = 24.sp,
                        fontWeight = FontWeight.Black,
                        textAlign  = TextAlign.Center
                    )
                    
                    Spacer(Modifier.height(8.dp))

                    val partidoLabel = if (partido != null) {
                        "${partido.equipoLocal} VS ${partido.equipoVisitante}"
                    } else {
                        "PARTIDO #${estadistica.idPartido}"
                    }

                    Box(
                        modifier = Modifier
                            .background(Verde.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            partidoLabel.uppercase(),
                            color      = Verde,
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    if (partido != null) {
                        Text(
                            text = partido.fecha,
                            color = TextoSec,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            // ── Cuerpo de métricas ────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "MÉTRICAS DEL PARTIDO",
                    color         = TextoSec,
                    fontSize      = 12.sp,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MetricBox(
                        modifier = Modifier.weight(1f),
                        label    = "GOLES",
                        value    = "${estadistica.goles ?: 0}",
                        icon     = Icons.Default.SportsSoccer,
                        color    = Verde
                    )
                    MetricBox(
                        modifier = Modifier.weight(1f),
                        label    = "ASISTENCIAS",
                        value    = "${estadistica.asistencias ?: 0}",
                        icon     = Icons.Default.Star,
                        color    = InfoBlue
                    )
                }

                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MetricBox(
                        modifier = Modifier.weight(1f),
                        label    = "T. AMARILLAS",
                        value    = "${estadistica.tarjetasAmarillas ?: 0}",
                        icon     = Icons.Default.Rectangle,
                        color    = WarningYellow
                    )
                    MetricBox(
                        modifier = Modifier.weight(1f),
                        label    = "T. ROJAS",
                        value    = "${estadistica.tarjetasRojas ?: 0}",
                        icon     = Icons.Default.Rectangle,
                        color    = ErrorRed
                    )
                }

                MetricBoxRow(
                    label = "MINUTOS JUGADOS",
                    value = "${estadistica.minutosJugados ?: 0} MIN",
                    icon  = Icons.Default.Timer,
                    color = WarningYellow
                )

                Spacer(Modifier.height(16.dp))

                // Botones de acción al pie
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick  = { onEditarClick(estadistica) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Verde)
                    ) {
                        Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("EDITAR", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                    Button(
                        onClick  = { mostrarDialogoEliminar = true },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                    ) {
                        Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("ELIMINAR", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                }
                
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

