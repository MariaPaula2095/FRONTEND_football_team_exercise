package com.example.football_team_frontend.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.model.ResultadoPartido
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PartidosScreen(
    resultados: List<ResultadoPartido>,
    jugadores: List<Jugador>,
    equipos: List<Equipo>,
    cargando: Boolean,
    mensaje: String? = null,
    onDismissMensaje: () -> Unit = {},
    onBackClick: () -> Unit,
    onRefrescarClick: () -> Unit,
    onAgregarClick: () -> Unit,
    onDetalleClick: (Long) -> Unit
) {
    var searchTexto by remember { mutableStateOf("") }
    var equipoSeleccionadoGoles by remember { mutableStateOf<String?>(null) }

    val partidosFiltrados = resultados.filter { res ->
        res.equipoLocal.contains(searchTexto, ignoreCase = true) ||
                res.equipoVisitante.contains(searchTexto, ignoreCase = true) ||
                res.estadio.contains(searchTexto, ignoreCase = true)
    }

    val golesTotales = remember(equipoSeleccionadoGoles, resultados) {
        if (equipoSeleccionadoGoles == null) 0
        else {
            resultados.sumOf { res ->
                when {
                    res.equipoLocal.equals(equipoSeleccionadoGoles, true) -> res.golesLocal
                    res.equipoVisitante.equals(equipoSeleccionadoGoles, true) -> res.golesVisitante
                    else -> 0
                }
            }
        }
    }

    Scaffold(
        containerColor = VerdeOscuro,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarClick,
                containerColor = Verde,
                contentColor = Blanco,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Partido")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Cabecera
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.background(Color.White.copy(0.1f), CircleShape).size(36.dp)) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = Blanco, modifier = Modifier.size(16.dp))
                    }
                    Text("PARTIDOS Y GOLES", color = Verde, fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    IconButton(onClick = onRefrescarClick, modifier = Modifier.background(Color.White.copy(0.1f), CircleShape).size(36.dp)) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Verde, modifier = Modifier.size(18.dp))
                    }
                }

                if (mensaje != null) {
                    Snackbar(
                        modifier = Modifier.padding(top = 8.dp),
                        containerColor = if (mensaje.contains("éxito")) Color(0xFF2E7D32) else Color(0xFFB00020),
                        action = {
                            TextButton(onClick = onDismissMensaje) {
                                Text("OK", color = Blanco)
                            }
                        }
                    ) {
                        Text(mensaje)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Buscador
                OutlinedTextField(
                    value = searchTexto,
                    onValueChange = { searchTexto = it },
                    placeholder = { Text("Buscar por equipo o estadio...", color = GrisClaro.copy(0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Verde) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF16312C),
                        unfocusedContainerColor = Color(0xFF16312C),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Blanco,
                        unfocusedTextColor = Blanco
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("VER GOLES TOTALES POR EQUIPO", color = Verde, fontSize = 11.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    equipos.map { it.nombre }.distinct().forEach { eq ->
                        val isSelected = equipoSeleccionadoGoles == eq
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Verde else Color(0xFF1F4A43))
                                .clickable { equipoSeleccionadoGoles = if (isSelected) null else eq }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(eq.uppercase(), color = Blanco, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (equipoSeleccionadoGoles != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A3F39))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Verde)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("$equipoSeleccionadoGoles: ", color = Verde, fontWeight = FontWeight.Bold)
                            Text("$golesTotales Goles", color = Blanco, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            // Lista
            if (cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Verde)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp, start = 24.dp, end = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(partidosFiltrados) { resultado ->
                        PartidoScoreCard(
                            resultado = resultado,
                            equipos = equipos,
                            onClick = { resultado.idPartido?.let { onDetalleClick(it) } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTab(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) Verde else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Blanco, fontSize = 13.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun PartidoScoreCard(
    resultado: ResultadoPartido,
    equipos: List<Equipo>,
    onClick: () -> Unit
) {
    val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val sdfDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    
    val fecha = try { sdfDateTime.parse(resultado.fecha) ?: sdfDate.parse(resultado.fecha) } catch (e: Exception) { try { sdfDate.parse(resultado.fecha) } catch (e2: Exception) { null } }
    val esFuturo = fecha?.after(Date()) ?: false

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F4A43).copy(0.8f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Stadium, contentDescription = null, tint = Verde, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(resultado.estadio, color = Blanco.copy(0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.size(4.dp).background(Verde, CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Text(resultado.fecha.split(" ")[0], color = Blanco.copy(0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TeamColumnMinimal(resultado.equipoLocal, Modifier.weight(1f))

                Box(modifier = Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                    if (esFuturo) {
                        Text("VS", color = Verde, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(resultado.golesLocal.toString(), color = Blanco, fontSize = 36.sp, fontWeight = FontWeight.Black)
                            Text(" : ", color = Blanco, fontSize = 28.sp, fontWeight = FontWeight.Black)
                            Text(resultado.golesVisitante.toString(), color = Blanco, fontSize = 36.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                TeamColumnMinimal(resultado.equipoVisitante, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun TeamColumnMinimal(nombre: String, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Icon(Icons.Default.Shield, contentDescription = null, tint = Blanco.copy(0.3f), modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(nombre.uppercase(), color = Blanco, fontSize = 13.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center, maxLines = 1)
    }
}
