package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.EstadisticasJugadorDto
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.model.ResultadoPartido
import com.example.football_team_frontend.ui.theme.*
import kotlinx.coroutines.delay

// Colores de stats definidos una sola vez para consistencia
private val ColorGoles      = Color(0xFF66BB6A)
private val ColorAsistencia = Color(0xFF64B5F6)
private val ColorAmarilla   = Color(0xFFFFD600)
private val ColorRoja       = Color(0xFFEF5350)
private val ColorMinutos    = Color(0xFFB0BEC5)

// Ancho fijo de cada columna de stat — mismo valor en header y celdas
private val StatColWidth = 38.dp

@Composable
fun EstadisticasScreen(
    estadisticas: List<EstadisticasJugadorDto>,
    partidos: List<ResultadoPartido>,
    jugadoresConMasGoles: List<Jugador>,
    onBuscarPorGoles: (Int) -> Unit,
    cargando: Boolean,
    mensaje: String?,
    onDismissMensaje: () -> Unit,
    onBackClick: () -> Unit,
    onRefrescarClick: () -> Unit,
    onAgregarClick: () -> Unit,
    onDetalleClick: (EstadisticasJugadorDto) -> Unit,
    onLimpiarBusqueda: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showLegend by remember { mutableStateOf(false) }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            onDismissMensaje()
        }
    }

    // ── Diálogo glosario ──
    if (showLegend) {
        AlertDialog(
            onDismissRequest = { showLegend = false },
            containerColor = FichaFondo,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    "GLOSARIO DE ESTADÍSTICAS",
                    color = Blanco,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    LegendItem("G",   "Goles anotados",         ColorGoles)
                    LegendItem("A",   "Asistencias realizadas", ColorAsistencia)
                    LegendItem("TA",  "Tarjetas amarillas",     ColorAmarilla)
                    LegendItem("TR",  "Tarjetas rojas",         ColorRoja)
                    LegendItem("MIN", "Minutos jugados",        ColorMinutos)
                }
            },
            confirmButton = {
                TextButton(onClick = { showLegend = false }) {
                    Text("ENTENDIDO", color = Verde, fontWeight = FontWeight.Black)
                }
            }
        )
    }

    Scaffold(
        containerColor = SuperficieAlt,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    text = "ESTADÍSTICAS",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Blanco,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Row(modifier = Modifier.align(Alignment.CenterEnd), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { showLegend = true },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Blanco.copy(alpha = 0.12f), CircleShape)
                    ) {
                        Icon(Icons.Outlined.Info, contentDescription = "Glosario", tint = Blanco, modifier = Modifier.size(20.dp))
                    }
                    IconButton(
                        onClick  = {
                            onLimpiarBusqueda()
                            onRefrescarClick()
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Blanco.copy(alpha = 0.12f), CircleShape)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar", tint = Verde, modifier = Modifier.size(20.dp))
                    }
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAgregarClick,
                containerColor = Verde,
                contentColor = Blanco,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp)) },
                text = { Text("AGREGAR", fontWeight = FontWeight.Black, fontSize = 14.sp) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ── Cards de resumen ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatSummaryCard(
                    title = "Goleador",
                    value = estadisticas.maxByOrNull { it.goles ?: 0 }?.nombreJugador ?: "---",
                    icon  = Icons.Default.EmojiEvents,
                    modifier = Modifier.weight(1f)
                )
                StatSummaryCard(
                    title = "Asistente",
                    value = estadisticas.maxByOrNull { it.asistencias ?: 0 }?.nombreJugador ?: "---",
                    icon  = Icons.Default.Star,
                    modifier = Modifier.weight(1f)
                )
            }

            // ── Buscador por goles ────────────────────────────────────────────
            var expandedSearch by remember { mutableStateOf(false) }
            var minGoles by remember { mutableStateOf("") }
            var mostrarMensajeVacio by remember { mutableStateOf(false) }
            var ultimoBuscado by remember { mutableStateOf("") }

            LaunchedEffect(jugadoresConMasGoles) {
                if (ultimoBuscado.isNotEmpty() && jugadoresConMasGoles.isEmpty()) {
                    mostrarMensajeVacio = true
                    delay(3000)
                    mostrarMensajeVacio = false
                } else {
                    mostrarMensajeVacio = false
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = FichaFondo)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { expandedSearch = !expandedSearch },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "🏆 FILTRAR POR MÍNIMO DE GOLES",
                            color      = TextoSec,
                            fontSize   = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Icon(
                            if (expandedSearch) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = TextoSec
                        )
                    }

                    if (expandedSearch) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value         = minGoles,
                                onValueChange = { 
                                    if (it.length <= 3) {
                                        minGoles = it
                                        // Si borra el texto, limpiamos los resultados previos
                                        if (it.isEmpty()) {
                                            onLimpiarBusqueda()
                                            ultimoBuscado = ""
                                        }
                                    }
                                },
                                modifier      = Modifier.weight(1f),
                                placeholder   = { Text("Ej: 5", color = TextoSec.copy(0.6f), fontSize = 13.sp) },
                                singleLine    = true,
                                shape         = RoundedCornerShape(12.dp),
                                colors        = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor   = Superficie.copy(0.5f),
                                    unfocusedContainerColor = Superficie.copy(0.5f),
                                    focusedBorderColor      = Verde,
                                    unfocusedBorderColor    = BorderSutil,
                                    focusedTextColor        = Blanco,
                                    unfocusedTextColor      = Blanco,
                                    cursorColor             = Verde
                                )
                            )
                            Button(
                                onClick = { 
                                    val goles = minGoles.toIntOrNull()
                                    if (goles != null) {
                                        ultimoBuscado = minGoles
                                        onBuscarPorGoles(goles) 
                                    }
                                },
                                modifier = Modifier.height(52.dp),
                                shape   = RoundedCornerShape(12.dp),
                                colors  = ButtonDefaults.buttonColors(containerColor = Verde)
                            ) {
                                Text("BUSCAR", fontWeight = FontWeight.Black, fontSize = 13.sp)
                            }
                        }
                        
                        if (mostrarMensajeVacio) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No hay ningún jugador que haya hecho más de $ultimoBuscado goles.",
                                color = ErrorRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (jugadoresConMasGoles.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            jugadoresConMasGoles.forEach { jugador ->
                                Row(
                                    modifier          = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier         = Modifier
                                            .size(36.dp)
                                            .background(Verde.copy(alpha = 0.18f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            jugador.nombre.split(" ").filter { it.isNotEmpty() }
                                                .take(2).joinToString("") { it.first().uppercaseChar().toString() },
                                            color      = Verde,
                                            fontSize   = 12.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(jugador.nombre.uppercase(), color = Blanco, fontSize = 13.sp, fontWeight = FontWeight.Black)
                                        Text(jugador.posicion.uppercase(), color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                HorizontalDivider(color = BorderSutil.copy(0.3f), thickness = 1.dp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Encabezado de tabla ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(FondoOscuro)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "JUGADOR",
                    color = TextoSec,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.weight(1f),
                    letterSpacing = 0.5.sp
                )

                StatHeaderCol("G",   ColorGoles)
                StatHeaderCol("A",   ColorAsistencia)
                StatHeaderCol("TA",  ColorAmarilla)
                StatHeaderCol("TR",  ColorRoja)
                StatHeaderCol("MIN", ColorMinutos)

                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint = TextoSec,
                    modifier = Modifier.size(14.dp)
                )
            }

            // ── Lista / estados ──
            when {
                cargando -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Verde)
                    }
                }
                estadisticas.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.SportsScore,
                                contentDescription = null,
                                tint = TextoSec,
                                modifier = Modifier.size(52.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "SIN ESTADÍSTICAS REGISTRADAS",
                                color = TextoSec,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(estadisticas.withIndex().toList()) { (index, estadistica) ->
                            val partido = partidos.find { it.idPartido == estadistica.idPartido }
                            EstadisticaRow(
                                stat    = estadistica,
                                partido = partido,
                                isEven  = index % 2 == 0,
                                onClick = { onDetalleClick(estadistica) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Cabecera de columna con ancho fijo ──
@Composable
fun StatHeaderCol(label: String, color: Color) {
    Text(
        text       = label,
        color      = color,
        fontSize   = 11.sp,
        fontWeight = FontWeight.Black,
        modifier   = Modifier.width(StatColWidth),
        textAlign  = TextAlign.Center,
        maxLines   = 1
    )
}

// ── Fila alternada de estadística ──
@Composable
fun EstadisticaRow(
    stat: EstadisticasJugadorDto,
    partido: ResultadoPartido?,
    isEven: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isEven) Color(0xFF244A34) else Color(0xFF1E3D2B)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(0.dp),
        colors   = CardDefaults.cardColors(containerColor = bgColor),
        onClick  = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Izquierda: nombre / partido / fecha en líneas separadas ──
            Column(
                modifier            = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Nombre del jugador
                Text(
                    stat.nombreJugador?.uppercase() ?: "JUGADOR",
                    color      = Blanco,
                    fontWeight = FontWeight.Black,
                    fontSize   = 13.sp,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                if (partido != null) {
                    // Partido: local vs visitante
                    Text(
                        "${partido.equipoLocal} vs ${partido.equipoVisitante}",
                        color      = Verde.copy(alpha = 0.9f),
                        fontSize   = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                    // Fecha en su propia línea
                    Text(
                        partido.fecha,
                        color    = GrisClaro,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                } else {
                    Text(
                        "Partido #${stat.idPartido}",
                        color    = GrisClaro,
                        fontSize = 10.sp
                    )
                }
            }

            // ── Derecha: valores alineados con las cabeceras ──
            StatCell("${stat.goles ?: 0}",             ColorGoles)
            StatCell("${stat.asistencias ?: 0}",       ColorAsistencia)
            StatCell("${stat.tarjetasAmarillas ?: 0}", ColorAmarilla)
            StatCell("${stat.tarjetasRojas ?: 0}",     ColorRoja)
            StatCell("${stat.minutosJugados ?: 0}",    ColorMinutos, small = true)

            // Espacio para alinear con el ⓘ del header
            Spacer(modifier = Modifier.width(20.dp))
        }
        HorizontalDivider(color = Color(0xFF12291C), thickness = 1.dp)
    }
}

// ── Celda de valor numérico ──
@Composable
fun StatCell(value: String, color: Color, small: Boolean = false) {
    Text(
        text       = value,
        color      = color,
        fontWeight = FontWeight.Black,
        fontSize   = if (small) 12.sp else 15.sp,
        modifier   = Modifier.width(StatColWidth),
        textAlign  = TextAlign.Center,
        maxLines   = 1
    )
}

// ── Card resumen (goleador / asistente) ──
@Composable
fun StatSummaryCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = Color(0xFF244A34))
    ) {
        Row(
            modifier              = Modifier.padding(14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier         = Modifier
                    .size(38.dp)
                    .background(Verde.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Verde, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(
                    title.uppercase(),
                    color      = GrisClaro,
                    fontSize   = 9.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    value,
                    color      = Blanco,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Black,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ── Item del glosario ──
@Composable
fun LegendItem(label: String, description: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier         = Modifier
                .size(32.dp)
                .background(color.copy(alpha = 0.18f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Black)
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(description, color = Blanco, fontSize = 13.sp)
    }
}

// ── Preview ──
@Preview(showBackground = true, backgroundColor = 0xFF0F2318, showSystemUi = true)
@Composable
fun EstadisticasprincipalPreview() {
    val estadisticasFake = listOf(
        EstadisticasJugadorDto(1, 1, "Daniel Ruiz",     2, 1, 0, 0, 87),
        EstadisticasJugadorDto(2, 1, "Jhon Córdoba",    1, 2, 1, 0, 90),
        EstadisticasJugadorDto(3, 2, "Leonardo Castro", 1, 0, 2, 1, 72),
        EstadisticasJugadorDto(4, 2, "Daniel Giraldo",  0, 1, 1, 0, 90),
        EstadisticasJugadorDto(5, 1, "Andrés Llinás",   0, 0, 1, 0, 90),
    )
    val partidosFake = listOf(
        ResultadoPartido(1, "Millonarios", "Nacional", "15/04/2025"),
        ResultadoPartido(2, "Millonarios", "Tolima",   "08/04/2025"),
    )
    EstadisticasScreen(
        estadisticas     = estadisticasFake,
        partidos         = partidosFake,
        jugadoresConMasGoles = emptyList(),
        onBuscarPorGoles     = {},
        cargando         = false,
        mensaje          = null,
        onDismissMensaje = {},
        onBackClick      = {},
        onRefrescarClick = {},
        onAgregarClick   = {},
        onDetalleClick   = {}
    )
}