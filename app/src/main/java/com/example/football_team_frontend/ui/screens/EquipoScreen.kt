package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

// ── Paleta interna ──────────────────────────────────────────────────────────
private val Superficie    = Color(0xFF1A3D2C)
private val SuperficieAlt = FondoOscuro
private val FichaFondo    = Color(0xFF1F4A34)
private val BorderSutil   = Color(0xFF2D6645)
private val TextoSec      = Color(0xFF7FB99A)



@Composable
fun EquiposScreen(
    equipos: List<Equipo>,
    searchQuery: String,
    cargando: Boolean = false,
    mensaje: String? = null,
    onDismissMensaje: () -> Unit = {},
    onSearchChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onRefrescarClick: () -> Unit,
    onAgregarClick: () -> Unit,
    onDetalleClick: (Equipo) -> Unit
) {
    val snackbarHostState  = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            onDismissMensaje()
        }
    }



    Scaffold(
        containerColor = SuperficieAlt,
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick          = onAgregarClick,
                containerColor   = Verde,
                contentColor     = Color.White,
                shape            = RoundedCornerShape(16.dp),
                icon             = { Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp)) },
                text             = { Text("AGREGAR", fontWeight = FontWeight.Black, fontSize = 14.sp, letterSpacing = 1.sp) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            // ── HERO ─────────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF0D2B1C), Color(0xFF163222))
                            )
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

                            IconButton(
                                onClick  = onRefrescarClick,
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Verde.copy(alpha = 0.25f), CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Refrescar",
                                    tint               = Verde,
                                    modifier           = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        // Título + contador
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text       = "Lista de Equipos",
                                modifier   = Modifier.weight(1f),
                                color      = Color.White,
                                fontSize   = 30.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 32.sp
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Box(
                                modifier = Modifier
                                    .background(
                                        Verde.copy(alpha = 0.2f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text       = "${equipos.size}",
                                    color      = Verde,
                                    fontSize   = 22.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // ── Buscador ──────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF163222))
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    OutlinedTextField(
                        value         = searchQuery,
                        onValueChange = onSearchChange,
                        modifier      = Modifier.fillMaxWidth(),
                        placeholder   = { Text("Buscar por nombre o ciudad...", color = TextoSec, fontSize = 14.sp) },
                        leadingIcon   = {
                            Icon(Icons.Default.Search, null, tint = Verde, modifier = Modifier.size(20.dp))
                        },
                        trailingIcon  = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(Icons.Default.Close, null, tint = TextoSec, modifier = Modifier.size(18.dp))
                                }
                            }
                        },
                        singleLine    = true,
                        shape         = RoundedCornerShape(16.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor   = Superficie,
                            unfocusedContainerColor = Superficie,
                            focusedBorderColor      = Verde,
                            unfocusedBorderColor    = BorderSutil,
                            focusedTextColor        = Color.White,
                            unfocusedTextColor      = Color.White,
                            cursorColor             = Verde
                        )
                    )
                }
            }



            // ── Separador sección ─────────────────────────────────────────
            item {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text          = "Equipos registrados  •  ${equipos.size} equipos",
                        color         = TextoSec,
                        fontSize      = 12.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier      = Modifier.weight(1f)
                    )
                }
            }

            // ── Estado cargando ───────────────────────────────────────────
            if (cargando) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Verde)
                    }
                }
            }

            // ── Estado vacío ──────────────────────────────────────────────
            if (!cargando && equipos.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier         = Modifier
                                    .size(72.dp)
                                    .background(Superficie, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.SearchOff,
                                    null,
                                    tint     = TextoSec,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            Text("Sin resultados", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text("Intenta con otro nombre o ciudad", color = TextoSec, fontSize = 13.sp)
                        }
                    }
                }
            }

            // ── Lista de equipos ──────────────────────────────────────────
            if (!cargando) {
                items(equipos) { equipo ->
                    EquipoCard(
                        equipo  = equipo,
                        onClick = { onDetalleClick(equipo) }
                    )
                }
            }

            item {
                AppFooter()
            }
        }
    }
}

// ── Chip de ordenamiento ──────────────────────────────────────────────────
@Composable
fun OrdenChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg     = if (selected) Verde       else Superficie
    val text   = if (selected) Color.White else TextoSec
    val weight = if (selected) FontWeight.Bold else FontWeight.Normal

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(label, color = text, fontSize = 13.sp, fontWeight = weight, maxLines = 1)
    }
}

// ── Tarjeta de equipo ─────────────────────────────────────────────────────
@Composable
fun EquipoCard(equipo: Equipo, onClick: () -> Unit) {
    val sdf          = remember { SimpleDateFormat("yyyy", Locale.getDefault()) }
    val anioFundacion = sdf.format(equipo.fundacion)

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = FichaFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con iniciales del equipo
            Box(
                modifier         = Modifier
                    .size(48.dp)
                    .background(Verde.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = equipo.nombre
                        .split(" ")
                        .filter { it.isNotBlank() }
                        .take(2)
                        .joinToString("") { it.first().uppercaseChar().toString() },
                    color      = Verde,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(Modifier.width(16.dp))

            // Info central
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = equipo.nombre.uppercase(),
                    color      = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize   = 14.sp,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(4.dp))
                // Ciudad con pill
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint     = TextoSec,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text     = equipo.ciudad,
                        color    = TextoSec,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Año fundación pill
            Box(
                modifier = Modifier
                    .background(Verde.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = anioFundacion,
                    color      = Verde,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────
@Composable
@Preview(showBackground = true, backgroundColor = 0xFF0D2B1C, showSystemUi = true)
fun EquiposScreenPreview() {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val equiposFake = listOf(
        Equipo(1L, "Millonarios FC",     "Bogotá",   formato.parse("1946-06-18")!!),
        Equipo(2L, "Atlético Nacional",  "Medellín", formato.parse("1947-03-07")!!),
        Equipo(3L, "América de Cali",    "Cali",     formato.parse("1927-02-13")!!),
        Equipo(4L, "Deportivo Cali",     "Cali",     formato.parse("1912-07-28")!!),
        Equipo(5L, "Junior FC",          "Barranquilla", formato.parse("1924-08-13")!!),
    )

    EquiposScreen(
        equipos         = equiposFake,
        searchQuery     = "",
        cargando        = false,
        mensaje         = null,
        onDismissMensaje = {},
        onSearchChange  = {},
        onBackClick     = {},
        onRefrescarClick = {},
        onAgregarClick  = {},
        onDetalleClick  = {}
    )
}