package com.example.football_team_frontend.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.model.ResultadoPartido
import com.example.football_team_frontend.ui.theme.*

@Composable
fun DetallePartidoScreen(
    resultado: ResultadoPartido?,
    jugadores: List<Jugador>,
    equipos: List<Equipo>,
    onBackClick: () -> Unit,
    onEditarClick: (Long) -> Unit,
    onEliminarClick: (Long) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // ── Estado vacío ──────────────────────────────────────────────────────
    if (resultado == null) {
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
                    Icon(Icons.Default.SportsSoccer, null, tint = TextoSec, modifier = Modifier.size(36.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("PARTIDO NO ENCONTRADO", color = Blanco, fontSize = 16.sp, fontWeight = FontWeight.Black)
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
                Text("¿ELIMINAR PARTIDO?", color = Blanco, fontWeight = FontWeight.Black, fontSize = 16.sp)
            },
            text = {
                Text(
                    "Se eliminará el registro del encuentro entre ${resultado.equipoLocal} y ${resultado.equipoVisitante}. Esta acción no se puede deshacer.",
                    color = TextoSec, fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    resultado.idPartido?.let { onEliminarClick(it) }
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
                        .background(Blanco.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Atrás",
                        tint               = Blanco,
                        modifier           = Modifier.size(18.dp)
                    )
                }
                
                Text(
                    text = "RESUMEN DEL ENCUENTRO",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
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
                .verticalScroll(scrollState)
        ) {
            // ── Hero Section (Marcador) ───────────────────────────────────
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
                        modifier = Modifier
                            .background(Verde.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            resultado.fecha.uppercase(),
                            color      = Verde,
                            fontSize   = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TeamHeroCol(resultado.equipoLocal, Modifier.weight(1f))
                        
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(resultado.golesLocal.toString(), color = Blanco, fontSize = 44.sp, fontWeight = FontWeight.Black)
                                Text(" - ", color = Verde, fontSize = 32.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 8.dp))
                                Text(resultado.golesVisitante.toString(), color = Blanco, fontSize = 44.sp, fontWeight = FontWeight.Black)
                            }
                            Text("FINALIZADO", color = Verde, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                        }

                        TeamHeroCol(resultado.equipoVisitante, Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(24.dp))

                    // Ubicación / Estadio
                    Surface(
                        onClick = {
                            val gmmIntentUri = Uri.parse("geo:0,0?q=${resultado.estadio}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            context.startActivity(mapIntent)
                        },
                        color = Negro.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocationOn, null, tint = Verde, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                resultado.estadio.uppercase(),
                                color = Blanco,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, null, tint = TextoSec, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // Espaciador para evitar que la franja verde se vea pegada
            Spacer(modifier = Modifier.height(8.dp))

            // ── Cuerpo: Alineaciones y Acciones ───────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Título Alineaciones
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Groups, null, tint = Verde, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "PLANTILLAS DEL ENCUENTRO",
                        color = Blanco,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Listas de jugadores
                AlineacionCard(resultado.equipoLocal, equipos, jugadores, isLocal = true)
                AlineacionCard(resultado.equipoVisitante, equipos, jugadores, isLocal = false)

                Spacer(Modifier.height(8.dp))

                // Botones de acción
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick  = { resultado.idPartido?.let { onEditarClick(it) } },
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

@Composable
fun TeamHeroCol(nombre: String, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Negro.copy(0.2f), CircleShape)
                .padding(2.dp)
                .background(Verde.copy(0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Shield, null, tint = Verde, modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(12.dp))
        Text(
            nombre.uppercase(),
            color = Blanco,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
fun AlineacionCard(nombreEquipo: String, equipos: List<Equipo>, jugadores: List<Jugador>, isLocal: Boolean) {
    val equipoObj = equipos.find { it.nombre.equals(nombreEquipo, ignoreCase = true) }
    val plantilla = if (equipoObj != null) jugadores.filter { it.idEquipo == equipoObj.idEquipo } else emptyList()
    val colorAccent = if (isLocal) Verde else WarningYellow

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FichaFondo)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(colorAccent, CircleShape))
                Spacer(Modifier.width(8.dp))
                Text(nombreEquipo.uppercase(), color = Blanco, fontWeight = FontWeight.Black, fontSize = 13.sp)
                Spacer(Modifier.weight(1f))
                Text("${plantilla.size} JUGADORES", color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(Modifier.height(12.dp))
            
            if (plantilla.isEmpty()) {
                Text("SIN REGISTROS", color = TextoSec, fontSize = 11.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            } else {
                plantilla.forEach { jug ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Negro.copy(0.15f), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = jug.dorsal.toString(),
                            color = colorAccent,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            modifier = Modifier.width(35.dp),
                            maxLines = 1
                        )
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(jug.nombre.uppercase(), color = Blanco, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                            Text(jug.posicion.uppercase(), color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
