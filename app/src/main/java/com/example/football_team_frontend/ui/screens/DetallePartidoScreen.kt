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

@OptIn(ExperimentalMaterial3Api::class)
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

    val VerdeFondo = VerdeOscuro
    val VerdeCard = CardColor
    val GrisTexto = GrisClaro

    if (resultado == null) {
        Box(modifier = Modifier.fillMaxSize().background(VerdeFondo), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Verde)
        }
        return
    }

    var expandedMenu by remember { mutableStateOf(false) }
    var mostrarConfirmacionEliminar by remember { mutableStateOf(false) }

    if (mostrarConfirmacionEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionEliminar = false },
            containerColor = VerdeCard,
            titleContentColor = Blanco,
            textContentColor = GrisClaro,
            title = { Text("¿Eliminar partido?", fontWeight = FontWeight.Bold) },
            text = { Text("Esta acción no se puede deshacer. Se borrará el registro del encuentro entre ${resultado.equipoLocal} y ${resultado.equipoVisitante}.") },
            confirmButton = {
                TextButton(onClick = {
                    resultado.idPartido?.let { onEliminarClick(it) }
                    mostrarConfirmacionEliminar = false
                }) {
                    Text("ELIMINAR", color = Color(0xFFFF4D4D), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacionEliminar = false }) {
                    Text("CANCELAR", color = GrisClaro)
                }
            }
        )
    }

    Scaffold(
        containerColor = VerdeFondo,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("DETALLES DEL ENCUENTRO", color = Blanco, fontWeight = FontWeight.Black, fontSize = 14.sp) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.background(VerdeCard.copy(0.5f), CircleShape).size(36.dp)
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = Blanco, modifier = Modifier.size(16.dp))
                    }
                },
                actions = {
                    Box {
                        IconButton(
                            onClick = { expandedMenu = true },
                            modifier = Modifier.background(VerdeCard.copy(0.5f), CircleShape).size(36.dp)
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = null, tint = Blanco)
                        }
                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false },
                            modifier = Modifier.background(VerdeCard)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Editar Encuentro", color = Blanco) },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = Verde) },
                                onClick = {
                                    expandedMenu = false
                                    resultado.idPartido?.let { onEditarClick(it) }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Eliminar Encuentro", color = Color(0xFFFF4D4D)) },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFFF4D4D)) },
                                onClick = {
                                    expandedMenu = false
                                    mostrarConfirmacionEliminar = true
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = VerdeFondo)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Card Principal de Marcador
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = VerdeCard)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = Verde.copy(0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            resultado.fecha.uppercase(), 
                            color = Verde, 
                            fontWeight = FontWeight.Black, 
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        TeamDetailCol(resultado.equipoLocal, equipos, Modifier.weight(1f))
                        
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(resultado.golesLocal.toString(), color = Blanco, fontSize = 48.sp, fontWeight = FontWeight.Black)
                                Text("-", color = Verde, fontSize = 36.sp, modifier = Modifier.padding(horizontal = 12.dp))
                                Text(resultado.golesVisitante.toString(), color = Blanco, fontSize = 48.sp, fontWeight = FontWeight.Black)
                            }
                            Text("FINALIZADO", color = Verde, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }

                        TeamDetailCol(resultado.equipoVisitante, equipos, Modifier.weight(1f))
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Ubicación
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(0.2f))
                            .clickable {
                                val gmmIntentUri = Uri.parse("geo:0,0?q=${resultado.estadio}")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                context.startActivity(mapIntent)
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(32.dp).background(Verde.copy(0.1f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Verde, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(resultado.estadio.uppercase(), color = Blanco, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(14.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Secciones de Plantilla
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Groups, contentDescription = null, tint = Verde, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ALINEACIONES", color = Blanco, fontSize = 16.sp, fontWeight = FontWeight.Black)
            }
            Text("Detalles de los jugadores convocados", color = GrisTexto, fontSize = 11.sp)
            
            Spacer(modifier = Modifier.height(20.dp))

            PlayerListPremium(resultado.equipoLocal, equipos, jugadores, isLocal = true, VerdeCard)
            Spacer(modifier = Modifier.height(16.dp))
            PlayerListPremium(resultado.equipoVisitante, equipos, jugadores, isLocal = false, VerdeCard)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun PlayerListPremium(nombreEquipo: String, equipos: List<Equipo>, jugadores: List<Jugador>, isLocal: Boolean, verdeCard: Color) {
    val equipoObj = equipos.find { it.nombre.equals(nombreEquipo, ignoreCase = true) }
    val plantilla = if (equipoObj != null) jugadores.filter { it.idEquipo == equipoObj.idEquipo } else emptyList()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = verdeCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(12.dp).background(if(isLocal) Verde else Color(0xFFFFD700), CircleShape))
                Spacer(modifier = Modifier.width(10.dp))
                Text(nombreEquipo.uppercase(), color = Blanco, fontWeight = FontWeight.Black, fontSize = 14.sp)
                Spacer(modifier = Modifier.weight(1f))
                Surface(color = Color.Black.copy(0.2f), shape = CircleShape) {
                    Text(
                        "${plantilla.size} JUGADORES", 
                        color = Verde, 
                        fontSize = 9.sp, 
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (plantilla.isEmpty()) {
                Text("No hay jugadores registrados para este equipo", color = GrisClaro, fontSize = 11.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            } else {
                plantilla.forEach { jug ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color.Black.copy(0.15f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(38.dp),
                            shape = CircleShape,
                            color = (if(isLocal) Verde else Color(0xFFFFD700)).copy(0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = jug.dorsal.toString(),
                                    color = if(isLocal) Verde else Color(0xFFFFD700),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = jug.nombre.uppercase(),
                                color = Blanco,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1
                            )
                            Text(
                                text = jug.posicion.uppercase(),
                                color = if(isLocal) Verde else GrisClaro,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Blanco.copy(0.1f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeamDetailCol(nombre: String, equipos: List<Equipo>, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Surface(
            shape = CircleShape, 
            color = Color.Black.copy(0.2f), 
            modifier = Modifier.size(70.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, Verde.copy(0.5f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.Shield, 
                    contentDescription = null, 
                    tint = Verde, 
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            nombre.uppercase(), 
            color = Blanco, 
            fontSize = 13.sp, 
            fontWeight = FontWeight.Black, 
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}
