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
private val SuperficieAlt = Color(0xFF153224)
private val FichaFondo    = Color(0xFF1F4A34)
private val BorderSutil   = Color(0xFF2D6645)
private val TextoSec      = Color(0xFF7FB99A)

// Color por especialidad
private fun colorEspecialidad(especialidad: String): Color = when {
    especialidad.contains("Portero",    ignoreCase = true) -> Color(0xFFFFA726)
    especialidad.contains("Defensivo",  ignoreCase = true) -> Color(0xFF42A5F5)
    especialidad.contains("Ofensivo",   ignoreCase = true) -> Color(0xFFEF5350)
    especialidad.contains("Físico",     ignoreCase = true) ||
            especialidad.contains("Fisico",     ignoreCase = true) -> Color(0xFF26C6DA)
    especialidad.contains("Táctico",    ignoreCase = true) ||
            especialidad.contains("Tactico",    ignoreCase = true) -> Color(0xFFAB47BC)
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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick        = onAgregarClick,
                containerColor = Verde,
                contentColor   = Color.White,
                shape          = RoundedCornerShape(50.dp),
                icon           = { Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp)) },
                text           = { Text("Nuevo entrenador", fontWeight = FontWeight.Bold, fontSize = 14.sp) }
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

                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text       = "Entrenadores",
                                modifier   = Modifier.weight(1f),
                                color      = Color.White,
                                fontSize   = 30.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 32.sp
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Box(
                                modifier = Modifier
                                    .background(Verde.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text       = "${entrenadores.size}",
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
                        placeholder   = { Text("Buscar por nombre o especialidad...", color = TextoSec, fontSize = 14.sp) },
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

            // ── Separador ─────────────────────────────────────────────────
            item {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text          = "Cuerpo técnico  •  ${entrenadores.size} entrenadores",
                        color         = TextoSec,
                        fontSize      = 12.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
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
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = FichaFondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(14.dp),
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
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(Modifier.width(14.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = entrenador.nombre,
                    color      = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 15.sp,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Box(
                    modifier = Modifier
                        .background(color.copy(alpha = 0.18f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text       = entrenador.especialidad,
                        color      = color,
                        fontSize   = 10.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines   = 1
                    )
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    text       = entrenador.equipo.nombre,
                    color      = Verde,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(10.dp))

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint               = TextoSec,
                modifier           = Modifier.size(18.dp)
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