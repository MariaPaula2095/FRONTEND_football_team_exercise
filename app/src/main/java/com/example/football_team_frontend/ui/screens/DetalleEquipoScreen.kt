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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

// ── Paleta ──────────────────────────────────────────────────────────────────
private val Superficie    = Color(0xFF1A3D2C)
private val SuperficieAlt = Color(0xFF153224)
private val FichaFondo    = Color(0xFF1F4A34)
private val TextoSec      = Color(0xFF7FB99A)

@Composable
fun DetalleEquipoScreen(
    equipo: Equipo?,
    onBackClick: () -> Unit,
    onEditarClick: () -> Unit,
    onEliminarClick: (Long) -> Unit
) {

    // ── Estado vacío ──────────────────────────────────────────────────────
    if (equipo == null) {
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
                    Icon(Icons.Default.SearchOff, null, tint = TextoSec, modifier = Modifier.size(36.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("Equipo no encontrado", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("No hay información disponible", color = TextoSec, fontSize = 13.sp)
            }
        }
        return
    }

    val iniciales = equipo.nombre
        .split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }

    val fechaFormateada = remember(equipo.fundacion) {
        SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "CO"))
            .format(equipo.fundacion)
    }

    val anioFundacion = remember(equipo.fundacion) {
        SimpleDateFormat("yyyy", Locale.getDefault()).format(equipo.fundacion)
    }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    // ── Diálogo confirmar eliminación ─────────────────────────────────────
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            containerColor   = FichaFondo,
            shape            = RoundedCornerShape(20.dp),
            title = {
                Text(
                    "¿Eliminar equipo?",
                    color      = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize   = 16.sp
                )
            },
            text = {
                Text(
                    "Se eliminará ${equipo.nombre} del sistema. Esta acción no se puede deshacer.",
                    color    = TextoSec,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onEliminarClick(equipo.idEquipo)
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
        // ── HERO ─────────────────────────────────────────────────────────
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
                // Navegación
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

                Spacer(Modifier.height(28.dp))

                // Avatar + nombre
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier         = Modifier
                            .size(80.dp)
                            .background(Verde.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            iniciales,
                            color      = Verde,
                            fontSize   = 26.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(Modifier.width(18.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Ficha del equipo",
                            color         = TextoSec,
                            fontSize      = 12.sp,
                            fontWeight    = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            equipo.nombre,
                            color      = Color.White,
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 26.sp,
                            maxLines   = 2,
                            overflow   = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .background(Verde.copy(alpha = 0.18f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "Desde $anioFundacion",
                                color      = Verde,
                                fontSize   = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // ── Cuerpo ────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Información del equipo",
                color         = TextoSec,
                fontSize      = 12.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            // Nombre + Ciudad en fila
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FichaInfoBox(
                    modifier = Modifier.weight(1f),
                    label    = "Nombre",
                    value    = equipo.nombre,
                    icon     = Icons.Default.Shield,
                    color    = Verde
                )
                FichaInfoBox(
                    modifier = Modifier.weight(1f),
                    label    = "Ciudad",
                    value    = equipo.ciudad,
                    icon     = Icons.Default.LocationOn,
                    color    = Color(0xFF26C6DA)
                )
            }

            // Fecha completa
            FichaInfoRow(
                label = "Fecha de fundación",
                value = fechaFormateada,
                icon  = Icons.Default.CalendarToday,
                color = Color(0xFFFFD600)
            )



            Spacer(Modifier.height(8.dp))

            // Botones
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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

// ── Preview ───────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0D2B1C, showSystemUi = true)
@Composable
fun DetalleEquipoScreenPreview() {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val equipoFake = Equipo(
        idEquipo  = 1L,
        nombre    = "Millonarios FC",
        ciudad    = "Bogotá",
        fundacion = formato.parse("1946-06-18")!!
    )
    DetalleEquipoScreen(
        equipo          = equipoFake,
        onBackClick     = {},
        onEditarClick   = {},
        onEliminarClick = {}
    )
}