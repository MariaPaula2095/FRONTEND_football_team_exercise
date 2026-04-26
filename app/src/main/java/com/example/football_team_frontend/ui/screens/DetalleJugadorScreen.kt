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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

// ── Paleta compartida con JugadoresScreen ──────────────────────────────────
private val Superficie    = Color(0xFF1A3D2C)
private val SuperficieAlt = Color(0xFF153224)
private val FichaFondo    = Color(0xFF1F4A34)
private val BorderSutil   = Color(0xFF2D6645)
private val TextoSec      = Color(0xFF7FB99A)

private fun colorPosicion(posicion: String): Color = when {
    posicion.contains("Portero",       ignoreCase = true) -> Color(0xFFFFA726)
    posicion.contains("Defensa",       ignoreCase = true) ||
            posicion.contains("Lateral",       ignoreCase = true) -> Color(0xFF42A5F5)
    posicion.contains("Mediocampista", ignoreCase = true) ||
            posicion.contains("Medio",         ignoreCase = true) -> Color(0xFFAB47BC)
    posicion.contains("Extremo",       ignoreCase = true) -> Color(0xFF26C6DA)
    posicion.contains("Delantero",     ignoreCase = true) -> Color(0xFFEF5350)
    else                                                   -> Color(0xFF66BB6A)
}

private fun inicialPosicion(posicion: String): String = when {
    posicion.contains("Portero",       ignoreCase = true) -> "PO"
    posicion.contains("Defensa",       ignoreCase = true) -> "DF"
    posicion.contains("Lateral",       ignoreCase = true) -> "LT"
    posicion.contains("Mediocampista", ignoreCase = true) ||
            posicion.contains("Medio",         ignoreCase = true) -> "MC"
    posicion.contains("Extremo",       ignoreCase = true) -> "EX"
    posicion.contains("Delantero",     ignoreCase = true) -> "DC"
    else                                                   -> "JG"
}

@Composable
fun DetalleJugadorScreen(
    jugador: Jugador?,
    equipo: Equipo?,
    onBackClick: () -> Unit,
    onEditarClick: () -> Unit,
    onEliminarClick: (Long) -> Unit
) {
    // ── Estado vacío ──────────────────────────────────────────────────────
    if (jugador == null) {
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
                    Icon(Icons.Default.PersonOff, null, tint = TextoSec, modifier = Modifier.size(36.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("Jugador no encontrado", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("No hay información disponible", color = TextoSec, fontSize = 13.sp)
            }
        }
        return
    }

    val posColor   = colorPosicion(jugador.posicion)
    val posInicial = inicialPosicion(jugador.posicion)
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    // ── Diálogo confirmar eliminación ─────────────────────────────────────
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            containerColor   = FichaFondo,
            shape            = RoundedCornerShape(20.dp),
            title = {
                Text("¿Eliminar jugador?", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
            },
            text = {
                Text(
                    "Se eliminará a ${jugador.nombre} del sistema. Esta acción no se puede deshacer.",
                    color = TextoSec, fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    jugador.idJugador?.let { onEliminarClick(it) }
                    mostrarDialogoEliminar = false
                }) {
                    Text("Eliminar", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar", color = TextoSec)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SuperficieAlt)
            .verticalScroll(rememberScrollState())
    ) {
        // ── HERO con degradado ────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF0D2B1C), Color(0xFF163222)))
                )
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column {
                // Fila de navegación
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Atrás
                    IconButton(
                        onClick  = onBackClick,
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White.copy(alpha = 0.12f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Atrás",
                            tint               = Color.White,
                            modifier           = Modifier.size(20.dp)
                        )
                    }

                    Spacer(Modifier.weight(1f))


                }

                Spacer(Modifier.height(28.dp))

                // Avatar + nombre en el hero
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar grande con iniciales de posición
                    Box(
                        modifier         = Modifier
                            .size(80.dp)
                            .background(posColor.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            posInicial,
                            color      = posColor,
                            fontSize   = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(Modifier.width(18.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Ficha técnica",
                            color         = TextoSec,
                            fontSize      = 12.sp,
                            fontWeight    = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            jugador.nombre,
                            color      = Color.White,
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 26.sp,
                            maxLines   = 2,
                            overflow   = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(6.dp))
                        // Pill de posición con color
                        Box(
                            modifier = Modifier
                                .background(posColor.copy(alpha = 0.18f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                jugador.posicion,
                                color      = posColor,
                                fontSize   = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // ── Cuerpo de la ficha ────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Etiqueta de sección
            Text(
                "Información del jugador",
                color         = TextoSec,
                fontSize      = 12.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            // Dorsal + Nacionalidad en fila
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FichaInfoBox(
                    modifier = Modifier.weight(1f),
                    label    = "Dorsal",
                    value    = "#${jugador.dorsal}",
                    icon     = Icons.Default.Numbers,
                    color    = Verde
                )
                FichaInfoBox(
                    modifier = Modifier.weight(1f),
                    label    = "Nacionalidad",
                    value    = jugador.nacionalidad,
                    icon     = Icons.Default.Public,
                    color    = Color(0xFF42A5F5)
                )
            }

            // Fecha de nacimiento
            FichaInfoRow(
                label = "Fecha de nacimiento",
                value = jugador.fechaNac ?: "No registrada",
                icon  = Icons.Default.CalendarToday,
                color = Color(0xFFFFD600)
            )

            // Club actual
            FichaInfoRow(
                label = "Club actual",
                value = equipo?.nombre ?: "Sin equipo",
                icon  = Icons.Default.Shield,
                color = Verde
            )

            // Ciudad del club (si existe)
            if (!equipo?.ciudad.isNullOrBlank()) {
                FichaInfoRow(
                    label = "Ciudad",
                    value = equipo!!.ciudad!!,
                    icon  = Icons.Default.LocationOn,
                    color = Color(0xFF26C6DA)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Botones de acción al pie
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Editar
                Button(
                    onClick  = onEditarClick,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = Verde)
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Editar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                // Eliminar
                OutlinedButton(
                    onClick  = { mostrarDialogoEliminar = true },
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF5350)),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF5350).copy(alpha = 0.5f))
                ) {
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Eliminar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

// ── Card cuadrada de info (dorsal / nacionalidad) ─────────────────────────
@Composable
fun FichaInfoBox(
    modifier: Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(18.dp),
        colors   = CardDefaults.cardColors(containerColor = FichaFondo)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier         = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(label, color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                color      = Color.White,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Black,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis
            )
        }
    }
}

// ── Fila de info horizontal (club, fecha, ciudad) ─────────────────────────
@Composable
fun FichaInfoRow(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = FichaFondo)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier         = Modifier
                    .size(38.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(
                    value,
                    color      = Color.White,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
            }
        }
    }
}


// ── Preview ───────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0D2B1C, showSystemUi = true)
@Composable
fun DetalleJugadorScreenPreviewprueba() {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val jugadorFake = Jugador(
        idJugador   = 5L,
        nombre      = "Daniel Ruiz",
        posicion    = "Extremo Izquierdo",
        dorsal      = 18,
        fechaNac    = "2001-07-30",
        nacionalidad = "Colombiano",
        idEquipo    = 1L
    )
    val equipoFake = Equipo(
        idEquipo = 1L,
        nombre   = "Millonarios FC",
        ciudad   = "Bogotá", formato.parse("2002-02-02")!!
    )
    DetalleJugadorScreen(
        jugador         = jugadorFake,
        equipo          = equipoFake,
        onBackClick     = {},
        onEditarClick   = {},
        onEliminarClick = {}
    )
}