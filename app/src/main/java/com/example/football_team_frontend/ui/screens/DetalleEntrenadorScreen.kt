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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Entrenador
import com.example.football_team_frontend.ui.components.MetricBox
import com.example.football_team_frontend.ui.components.MetricBoxRow
import com.example.football_team_frontend.ui.theme.*

private fun colorEspecialidadDetalle(especialidad: String): Color = when {
    especialidad.contains("Principal",  ignoreCase = true) -> Verde
    especialidad.contains("Asistente",  ignoreCase = true) -> InfoBlue
    especialidad.contains("Portero",    ignoreCase = true) -> WarningYellow
    especialidad.contains("Físico",     ignoreCase = true) ||
            especialidad.contains("Fisico",     ignoreCase = true) -> InfoBlue
    especialidad.contains("Analista",   ignoreCase = true) -> InfoBlue
    else                                                    -> Verde
}

@Composable
fun DetalleEntrenadorScreen(
    entrenador: Entrenador?,
    onBackClick: () -> Unit,
    onEditarClick: () -> Unit,
    onEliminarClick: (Long) -> Unit
) {
    // ── Estado vacío ──────────────────────────────────────────────────────
    if (entrenador == null) {
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
                Text("ENTRENADOR NO ENCONTRADO", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("No hay información disponible", color = TextoSec, fontSize = 13.sp)
            }
        }
        return
    }

    val color = colorEspecialidadDetalle(entrenador.especialidad)

    val iniciales = entrenador.nombre
        .split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    // ── Diálogo confirmar eliminación ─────────────────────────────────────
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            containerColor   = FichaFondo,
            shape            = RoundedCornerShape(20.dp),
            title = {
                Text("¿ELIMINAR ENTRENADOR?", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
            },
            text = {
                Text(
                    "Se eliminará a ${entrenador.nombre} del sistema. Esta acción no se puede deshacer.",
                    color = TextoSec, fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onEliminarClick(entrenador.idEntrenador)
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
                    text = "FICHA DEL ENTRENADOR",
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
            // ── Hero Section ──────────────────────────────────────────────
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
                            .background(color.copy(alpha = 0.18f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            iniciales,
                            color      = color,
                            fontSize   = 28.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        entrenador.nombre.uppercase(),
                        color      = Color.White,
                        fontSize   = 24.sp,
                        fontWeight = FontWeight.Black,
                        textAlign  = TextAlign.Center,
                        maxLines   = 2,
                        overflow   = TextOverflow.Ellipsis
                    )
                    
                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .background(color.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            entrenador.especialidad.uppercase(),
                            color      = color,
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // ── Cuerpo de información ─────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "DATOS PROFESIONALES",
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
                        label    = "ESPECIALIDAD",
                        value    = entrenador.especialidad.uppercase(),
                        icon     = Icons.Default.Star,
                        color    = WarningYellow
                    )
                    MetricBox(
                        modifier = Modifier.weight(1f),
                        label    = "EXPERIENCIA",
                        value    = "PERFIL VERIFICADO",
                        icon     = Icons.Default.Verified,
                        color    = Verde
                    )
                }

                MetricBoxRow(
                    label = "EQUIPO ASIGNADO",
                    value = entrenador.equipo.nombre.uppercase(),
                    icon  = Icons.Default.Shield,
                    color = Verde
                )

                MetricBoxRow(
                    label = "CIUDAD DEL EQUIPO",
                    value = entrenador.equipo.ciudad.uppercase(),
                    icon  = Icons.Default.LocationOn,
                    color = InfoBlue
                )

                Spacer(Modifier.height(16.dp))

                // Botones de acción
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
