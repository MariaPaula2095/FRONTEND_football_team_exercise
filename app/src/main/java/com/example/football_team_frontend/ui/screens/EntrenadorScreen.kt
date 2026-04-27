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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Entrenador
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

// ── Paleta ──────────────────────────────────────────────────────────────────
private val Superficie    = Color(0xFF1A3D2C)
private val SuperficieAlt = FondoOscuro
private val FichaFondo    = Color(0xFF1F4A34)
private val BorderSutil   = Color(0xFF2D6645)
private val TextoSec      = Color(0xFF7FB99A)

// Color por especialidad
private fun colorEspecialidad(especialidad: String): Color = when {
    especialidad.contains("Portero",    ignoreCase = true) -> WarningYellow
    especialidad.contains("Asistente",  ignoreCase = true) ||
            especialidad.contains("Analista",   ignoreCase = true) ||
            especialidad.contains("Físico",     ignoreCase = true) ||
            especialidad.contains("Fisico",     ignoreCase = true) ||
            especialidad.contains("Defensivo",  ignoreCase = true) -> InfoBlue
    especialidad.contains("Ofensivo",   ignoreCase = true) -> ErrorRed
    else                                                    -> Verde
}

@Composable
fun EntrenadoresScreen(
    entrenadores: List<Entrenador>,
    searchQuery: String,
    cargando: Boolean = false,
    mensaje: String? = null,
    onDismissMensaje: () -> Unit = {},
    onSearchChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onRefrescarClick: () -> Unit,
    onAgregarClick: () -> Unit,
    onDetalleClick: (Entrenador) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            onDismissMensaje()
        }
    }

    Scaffold(
        containerColor = SuperficieAlt,
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D2B1C))
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
                    text = "ENTRENADORES",
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
                onClick        = onAgregarClick,
                containerColor = Verde,
                contentColor   = Color.White,
                shape          = RoundedCornerShape(16.dp),
                icon           = { Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp)) },
                text           = { Text("AGREGAR", fontWeight = FontWeight.Black, fontSize = 14.sp) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            // ── Buscador ──────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    OutlinedTextField(
                        value         = searchQuery,
                        onValueChange = onSearchChange,
                        modifier      = Modifier.fillMaxWidth(),
                        placeholder   = { Text("Buscar por nombre o especialidad...", color = TextoSec.copy(0.6f), fontSize = 14.sp) },
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
                            focusedContainerColor   = FichaFondo.copy(0.5f),
                            unfocusedContainerColor = FichaFondo.copy(0.5f),
                            focusedBorderColor      = Verde,
                            unfocusedBorderColor    = BorderSutil,
                            focusedTextColor        = Color.White,
                            unfocusedTextColor      = Color.White,
                            cursorColor             = Verde
                        )
                    )
                }
            }

            // ── Separador ─────────────────────────────────────────────────
            item {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text          = "CUERPO TÉCNICO  •  ${entrenadores.size} INTEGRANTES",
                        color         = TextoSec,
                        fontSize      = 12.sp,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 1.sp,
                        modifier      = Modifier.weight(1f)
                    )
                }
            }

            // ── Cargando ──────────────────────────────────────────────────
            if (cargando) {
                item {
                    Box(
                        Modifier.fillMaxWidth().height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Verde)
                    }
                }
            }

            // ── Vacío ─────────────────────────────────────────────────────
            if (!cargando && entrenadores.isEmpty()) {
                item {
                    Box(
                        Modifier.fillMaxWidth().height(280.dp),
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
                            Text("Intenta con otro nombre o especialidad", color = TextoSec, fontSize = 13.sp)
                        }
                    }
                }
            }

            // ── Lista ─────────────────────────────────────────────────────
            if (!cargando) {
                items(entrenadores) { entrenador ->
                    EntrenadorCard(
                        entrenador = entrenador,
                        onClick    = { onDetalleClick(entrenador) }
                    )
                }
            }
        }
    }
}

// ── Tarjeta de entrenador ─────────────────────────────────────────────────
@Composable
fun EntrenadorCard(entrenador: Entrenador, onClick: () -> Unit) {
    val color = colorEspecialidad(entrenador.especialidad)

    val iniciales = entrenador.nombre
        .split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = FichaFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier         = Modifier
                    .size(52.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = iniciales,
                    color      = color,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = entrenador.nombre.uppercase(),
                    color      = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize   = 15.sp,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(color.copy(alpha = 0.18f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text       = entrenador.especialidad.uppercase(),
                            color      = color,
                            fontSize   = 9.sp,
                            fontWeight = FontWeight.Black,
                            maxLines   = 1
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text       = entrenador.equipo.nombre.uppercase(),
                        color      = Verde,
                        fontSize   = 10.sp,
                        fontWeight = FontWeight.Black,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint               = TextoSec,
                modifier           = Modifier.size(20.dp)
            )
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0D2B1C, showSystemUi = true)
@Composable
fun EntrenadoresScreenPreview() {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val equipoFake = Equipo(1L, "Millonarios FC", "Bogotá", formato.parse("1946-06-18")!!)

    val entrenadoresFake = listOf(
        Entrenador(1L, "Alberto Gamero",  "Táctico Ofensivo",  equipoFake),
        Entrenador(2L, "Jorge Bermúdez",  "Defensivo",         equipoFake),
        Entrenador(3L, "Luis Amaranto",   "Porteros",          equipoFake),
        Entrenador(4L, "Carlos Paniagua", "Físico",            equipoFake),
    )

    EntrenadoresScreen(
        entrenadores     = entrenadoresFake,
        searchQuery      = "",
        cargando         = false,
        mensaje          = null,
        onDismissMensaje = {},
        onSearchChange   = {},
        onBackClick      = {},
        onRefrescarClick = {},
        onAgregarClick   = {},
        onDetalleClick   = {}
    )
}