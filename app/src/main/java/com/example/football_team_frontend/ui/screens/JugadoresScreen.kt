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
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

// ── Paleta interna ──────────────────────────────────────────────────────────
private val Superficie   = Color(0xFF1A3D2C)
private val SuperficieAlt= FondoOscuro
private val FichaFondo   = Color(0xFF1F4A34)
private val BorderSutil  = Color(0xFF2D6645)
private val TextoSec     = Color(0xFF7FB99A)

// Colores por posición
private fun colorPosicion(posicion: String): Color = when {
    posicion.contains("Portero",      ignoreCase = true) -> WarningYellow
    posicion.contains("Defensa",      ignoreCase = true) ||
            posicion.contains("Lateral",      ignoreCase = true) -> InfoBlue
    posicion.contains("Mediocampista",ignoreCase = true) ||
            posicion.contains("Medio",        ignoreCase = true) -> Color(0xFF9F25B4)
    posicion.contains("Extremo",      ignoreCase = true) -> Color(0xFF26C6DA)
    posicion.contains("Delantero",    ignoreCase = true) -> ErrorRed
    else                                                  -> Verde
}

private fun inicialPosicion(posicion: String): String = when {
    posicion.contains("Portero",      ignoreCase = true) -> "PO"
    posicion.contains("Defensa",      ignoreCase = true) -> "DF"
    posicion.contains("Lateral",      ignoreCase = true) -> "LT"
    posicion.contains("Mediocampista",ignoreCase = true) ||
            posicion.contains("Medio",        ignoreCase = true) -> "MC"
    posicion.contains("Extremo",      ignoreCase = true) -> "EX"
    posicion.contains("Delantero",    ignoreCase = true) -> "DC"
    else                                                  -> "JG"
}

@Composable
fun JugadoresScreen(
    jugadores: List<Jugador>,
    equipos: List<Equipo>,
    cargando: Boolean = false,
    mensaje: String? = null,
    onDismissMensaje: () -> Unit = {},
    onBackClick: () -> Unit,
    onRefrescarClick: () -> Unit,
    onAgregarClick: () -> Unit,
    onDetalleClick: (Jugador) -> Unit
) {
    var searchQuery        by remember { mutableStateOf("") }
    var selectedEquipoId   by remember { mutableStateOf<Long?>(null) }
    val snackbarHostState  = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            onDismissMensaje()
        }
    }

    val filtrados = jugadores.filter {
        it.nombre.contains(searchQuery, ignoreCase = true) &&
                (selectedEquipoId == null || it.idEquipo == selectedEquipoId)
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
            modifier      = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            // ── HERO: header con fondo degradado ─────────────────────────
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

                            // Refrescar
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
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            // Título
                            Text(
                                text = "Lista de Jugadores",
                                modifier = Modifier.weight(1f),

                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 32.sp
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            // Burbuja contador
                            Box(
                                modifier = Modifier
                                    .background(
                                        Verde.copy(alpha = 0.2f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(
                                        horizontal = 14.dp,
                                        vertical = 8.dp
                                    ),

                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = "${filtrados.size}",

                                    color = Verde,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // ── Buscador ─────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF163222))
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    OutlinedTextField(
                        value         = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier      = Modifier.fillMaxWidth(),
                        placeholder   = { Text("Buscar jugador...", color = TextoSec, fontSize = 14.sp) },
                        leadingIcon   = {
                            Icon(Icons.Default.Search, null, tint = Verde, modifier = Modifier.size(20.dp))
                        },
                        trailingIcon  = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
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

            // ── Filtros de equipo como chips horizontales ─────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF163222))
                        .padding(bottom = 16.dp)
                ) {
                    LazyRow(
                        contentPadding        = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Chip "Todos"
                        item {
                            EquipoChip(
                                label     = "Todos",
                                selected  = selectedEquipoId == null,
                                onClick   = { selectedEquipoId = null }
                            )
                        }
                        items(equipos) { equipo ->
                            EquipoChip(
                                label    = equipo.nombre,
                                selected = selectedEquipoId == equipo.idEquipo,
                                onClick  = { selectedEquipoId = equipo.idEquipo }
                            )
                        }
                    }
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
                        "Plantilla  •  ${filtrados.size} jugadores",
                        color      = TextoSec,
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier   = Modifier.weight(1f)
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
            if (!cargando && filtrados.isEmpty()) {
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
                                    Icons.Default.PersonSearch,
                                    null,
                                    tint     = TextoSec,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(Modifier.height(16.dp))
                            Text("Sin resultados", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text("Intenta con otro nombre o equipo", color = TextoSec, fontSize = 13.sp)
                        }
                    }
                }
            }

            // ── Lista de jugadores ────────────────────────────────────────
            if (!cargando) {
                items(filtrados) { jugador ->
                    val equipoNombre = equipos.find { it.idEquipo == jugador.idEquipo }?.nombre ?: "Sin equipo"
                    JugadorCard(
                        jugador      = jugador,
                        equipoNombre = equipoNombre,
                        onClick      = { onDetalleClick(jugador) }
                    )
                }
            }

            item {
                AppFooter()
            }
        }
    }
}

// ── Chip de filtro de equipo ──────────────────────────────────────────────
@Composable
fun EquipoChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg     = if (selected) Verde          else Superficie
    val border = if (selected) Verde          else BorderSutil
    val text   = if (selected) Color.White    else TextoSec
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

// ── Tarjeta de jugador ────────────────────────────────────────────────────
@Composable
fun JugadorCard(jugador: Jugador, equipoNombre: String, onClick: () -> Unit) {
    val posColor  = colorPosicion(jugador.posicion)
    val posInicial = inicialPosicion(jugador.posicion)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FichaFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con iniciales de posición y color distintivo
            Box(
                modifier         = Modifier
                    .size(48.dp)
                    .background(posColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    posInicial,
                    color      = posColor,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(Modifier.width(16.dp))

            // Info central
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    jugador.nombre.uppercase(),
                    color      = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize   = 14.sp,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(4.dp))
                // Posición con pill de color
                Row(
                    verticalAlignment      = Alignment.CenterVertically,
                    horizontalArrangement  = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(posColor.copy(alpha = 0.18f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            jugador.posicion.uppercase(),
                            color      = posColor,
                            fontSize   = 9.sp,
                            fontWeight = FontWeight.Black,
                            maxLines   = 1
                        )
                    }
                    Text("•", color = TextoSec, fontSize = 12.sp)
                    Text(
                        equipoNombre,
                        color      = Verde,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Dorsal
            Box(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "#${jugador.dorsal}",
                    color      = Color.White,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF0D2B1C, showSystemUi = true)
fun JugadoresScreenPreviewPrueba() {

    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val equiposFake = listOf(
        Equipo(
            1L,
            "Millonarios",
            "Bogotá",
            formato.parse("2002-02-02")!!
        ),

        Equipo(
            2L,
            "Nacional",
            "Medellín",
            formato.parse("1947-03-07")!!
        )
    )

    val jugadoresFake = listOf(
        Jugador(1L, "Juan Pablo Vargas", "Defensa Central", 4, "1995-06-06", "Costarricense", 1L),
        Jugador(2L, "Andrés Llinás", "Defensa Central", 26, "1997-07-23", "Colombiano", 1L),
        Jugador(3L, "Delvin Alfonzo", "Lateral Derecho", 22, "2000-09-04", "Venezolano", 1L),
        Jugador(4L, "Daniel Giraldo", "Mediocampista Central", 27, "1992-07-01", "Colombiano", 1L),
        Jugador(5L, "Daniel Ruiz", "Extremo Izquierdo", 18, "2001-07-30", "Colombiano", 1L),
        Jugador(6L, "Jhon Emerson Córdoba", "Extremo Derecho", 15, "2000-09-14", "Colombiano", 1L),
        Jugador(7L, "Leonardo Castro", "Delantero Centro", 23, "1992-06-14", "Colombiano", 1L),
    )
    JugadoresScreen(
        jugadores = jugadoresFake,
        equipos = equiposFake,
        cargando = false,
        mensaje = null,
        onDismissMensaje = {},
        onBackClick = {},
        onRefrescarClick = {},
        onAgregarClick = {},
        onDetalleClick = {}
    )
}
