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
import com.example.football_team_frontend.ui.components.MetricBox
import com.example.football_team_frontend.ui.components.MetricBoxRow
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

// ── Paleta compartida con JugadoresScreen ──────────────────────────────────

private fun colorPosicion(posicion: String): Color = when {
    posicion.contains("Portero",       ignoreCase = true) -> WarningYellow
    posicion.contains("Defensa",       ignoreCase = true) ||
            posicion.contains("Lateral",       ignoreCase = true) -> InfoBlue
    posicion.contains("Mediocampista", ignoreCase = true) ||
            posicion.contains("Medio",         ignoreCase = true) -> Verde
    posicion.contains("Delantero",     ignoreCase = true) ||
            posicion.contains("Extremo",       ignoreCase = true) -> ErrorRed
    else                                                   -> Verde
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
                Text("¿ELIMINAR JUGADOR?", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
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
                    text = "DETALLE JUGADOR",
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
            // ── HERO con degradado ────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(FondoOscuro, SuperficieAlt))
                    )
                    .padding(horizontal = 20.dp, vertical = 30.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    // Avatar grande con iniciales de posición
                    Box(
                        modifier         = Modifier
                            .size(100.dp)
                            .background(posColor.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            posInicial,
                            color      = posColor,
                            fontSize   = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        jugador.nombre.uppercase(),
                        color      = Color.White,
                        fontSize   = 24.sp,
                        fontWeight = FontWeight.Black,
                        textAlign  = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                    
                    Spacer(Modifier.height(10.dp))

                    // Pill de posición
                    Box(
                        modifier = Modifier
                            .background(posColor.copy(alpha = 0.18f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            jugador.posicion.uppercase(),
                            color      = posColor,
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // ── Cuerpo de la ficha ────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    "INFORMACIÓN TÉCNICA",
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
                        label    = "DORSAL",
                        value    = "#${jugador.dorsal}",
                        icon     = Icons.Default.Numbers,
                        color    = Verde
                    )
                    MetricBox(
                        modifier = Modifier.weight(1f),
                        label    = "NACIONALIDAD",
                        value    = jugador.nacionalidad.uppercase(),
                        icon     = Icons.Default.Public,
                        color    = InfoBlue
                    )
                }

                MetricBoxRow(
                    label = "FECHA DE NACIMIENTO",
                    value = jugador.fechaNac ?: "NO REGISTRADA",
                    icon  = Icons.Default.CalendarToday,
                    color = WarningYellow
                )

                MetricBoxRow(
                    label = "CLUB ACTUAL",
                    value = equipo?.nombre?.uppercase() ?: "SIN EQUIPO",
                    icon  = Icons.Default.Shield,
                    color = Verde
                )

                Spacer(Modifier.height(16.dp))

                // Botones de acción al pie
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick  = onEditarClick,
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