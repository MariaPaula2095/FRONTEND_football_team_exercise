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
                    text = "PARTIDOS Y GOLES",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                IconButton(
                    onClick  = onRefrescarClick,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Refrescar",
                        tint               = Verde,
                        modifier           = Modifier.size(20.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAgregarClick,
                containerColor = Verde,
                contentColor = Blanco,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, "Agregar") },
                text = { Text("AGREGAR", fontWeight = FontWeight.Black) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Cabecera con Buscador y Filtros
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                if (mensaje != null) {
                    Snackbar(
                        modifier = Modifier.padding(bottom = 16.dp),
                        containerColor = if (mensaje.contains("éxito", true)) Verde else ErrorRed,
                        action = {
                            TextButton(onClick = onDismissMensaje) {
                                Text("OK", color = Blanco, fontWeight = FontWeight.Bold)
                            }
                        }
                    ) {
                        Text(mensaje)
                    }
                }

                // Buscador
                OutlinedTextField(
                    value = searchTexto,
                    onValueChange = { searchTexto = it },
                    placeholder = { Text("Buscar por equipo o estadio...", color = TextoSec.copy(0.6f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Verde) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FichaFondo.copy(0.5f),
                        unfocusedContainerColor = FichaFondo.copy(0.5f),
                        focusedBorderColor = Verde,
                        unfocusedBorderColor = BorderSutil,
                        focusedTextColor = Blanco,
                        unfocusedTextColor = Blanco
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text("VER GOLES TOTALES POR EQUIPO", color = TextoSec, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(10.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    equipos.map { it.nombre }.distinct().forEach { eq ->
                        val isSelected = equipoSeleccionadoGoles == eq
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Verde else FichaFondo)
                                .clickable { equipoSeleccionadoGoles = if (isSelected) null else eq }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(eq.uppercase(), color = Blanco, fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                if (equipoSeleccionadoGoles != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FichaFondo)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(40.dp).background(Verde.copy(0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Verde, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(equipoSeleccionadoGoles?.uppercase() ?: "", color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Black)
                                Text("$golesTotales GOLES TOTALES", color = Blanco, fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
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
                    contentPadding = PaddingValues(bottom = 100.dp, start = 24.dp, end = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(partidosFiltrados) { resultado ->
                        PartidoScoreCard(
                            resultado = resultado,
                            onClick = { resultado.idPartido?.let { onDetalleClick(it) } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PartidoScoreCard(
    resultado: ResultadoPartido,
    onClick: () -> Unit
) {
    val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val sdfDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    
    val fecha = try { sdfDateTime.parse(resultado.fecha) ?: sdfDate.parse(resultado.fecha) } catch (e: Exception) { try { sdfDate.parse(resultado.fecha) } catch (e2: Exception) { null } }
    val esFuturo = fecha?.after(Date()) ?: false

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FichaFondo)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Stadium, contentDescription = null, tint = Verde, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(resultado.estadio.uppercase(), color = TextoSec, fontSize = 11.sp, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.weight(1f))
                Text(resultado.fecha.split(" ")[0], color = TextoSec, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TeamColumnMinimal(resultado.equipoLocal, Modifier.weight(1f))

                Box(modifier = Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                    if (esFuturo) {
                        Text("VS", color = Verde, fontSize = 24.sp, fontWeight = FontWeight.Black)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(resultado.golesLocal.toString(), color = Blanco, fontSize = 32.sp, fontWeight = FontWeight.Black)
                            Text(" : ", color = Verde, fontSize = 24.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 8.dp))
                            Text(resultado.golesVisitante.toString(), color = Blanco, fontSize = 32.sp, fontWeight = FontWeight.Black)
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
