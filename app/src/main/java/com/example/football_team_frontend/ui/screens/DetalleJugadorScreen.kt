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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.ui.theme.*

@Composable
fun DetalleJugadorScreen(
    jugador: Jugador?,
    equipo: Equipo?,
    onBackClick: () -> Unit,
    onEditarClick: () -> Unit,
    onEliminarClick: (Long) -> Unit
) {
    if (jugador == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B2A)), contentAlignment = Alignment.Center) {
            Text("No se encontró la información del jugador", color = Blanco)
        }
        return
    }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            containerColor = Color(0xFF1B263B),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFE0E1DD),
            title = { Text("¿Eliminar jugador?", fontWeight = FontWeight.Bold) },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    jugador.idJugador?.let { onEliminarClick(it) }
                    mostrarDialogoEliminar = false
                }) {
                    Text("ELIMINAR", color = Color(0xFFFF4D4D), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("CANCELAR", color = Color(0xFFE0E1DD))
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B2A))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Cabecera estilizada
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.background(Color(0xFF1B263B), CircleShape).size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                
                Text(
                    text = "FICHA TÉCNICA",
                    color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onEditarClick,
                        modifier = Modifier.background(Color(0xFF415A77), CircleShape).size(40.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = { mostrarDialogoEliminar = true },
                        modifier = Modifier.background(Color(0xFF780000).copy(0.8f), CircleShape).size(40.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Tarjeta de Jugador "Cromo"
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors = listOf(Verde, Color(0xFF415A77))
                                ),
                                shape = CircleShape
                            ).padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B2A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Verde, modifier = Modifier.size(60.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = jugador.nombre.uppercase(),
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    
                    Surface(
                        color = Verde,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = " ${jugador.posicion} ",
                            color = Color(0xFF0D1B2A),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bloque de Info Alineado
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoBox(Modifier.weight(1f), "DORSAL", "#${jugador.dorsal}", Icons.Default.Numbers)
                InfoBox(Modifier.weight(1f), "PAÍS", jugador.nacionalidad.uppercase(), Icons.Default.Public)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B).copy(0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = Verde, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("CLUB ACTUAL", color = Color(0xFF778DA9), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(equipo?.nombre ?: "Sin equipo", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(equipo?.ciudad ?: "", color = Color(0xFF778DA9), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun InfoBox(modifier: Modifier, label: String, value: String, icon: ImageVector) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Verde, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(label, color = Color(0xFF778DA9), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
        }
    }
}
