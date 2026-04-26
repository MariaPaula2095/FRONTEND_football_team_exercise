//package com.example.football_team_frontend.ui.screens
//
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
//import com.example.football_team_frontend.model.Equipo
//import com.example.football_team_frontend.model.Jugador
//import com.example.football_team_frontend.ui.theme.*
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//// ── Paleta compartida ──────────────────────────────────────────────────────
//private val Superficie    = Color(0xFF1A3D2C)
//private val SuperficieAlt = Color(0xFF153224)
//private val FichaFondo    = Color(0xFF1F4A34)
//private val BorderSutil   = Color(0xFF2D6645)
//private val TextoSec      = Color(0xFF7FB99A)
//private val ColorError    = Color(0xFFEF5350)
//
//private val posiciones = listOf(
//    "Portero",
//    "Defensa Central",
//    "Lateral Derecho",
//    "Lateral Izquierdo",
//    "Mediocampista Defensivo",
//    "Mediocampista Central",
//    "Mediocampista Ofensivo",
//    "Extremo Derecho",
//    "Extremo Izquierdo",
//    "Delantero Centro",
//    "Segundo Delantero"
//)
//
//// ── Formatea input de fecha mientras el usuario escribe: AAAA-MM-DD ────────
//private fun formatearFecha(input: String, anterior: String): String {
//    // Quitar caracteres no numéricos
//    val soloNumeros = input.filter { it.isDigit() }
//    // Limitar a 8 dígitos (AAAAMMDD)
//    val limitado = soloNumeros.take(8)
//    return buildString {
//        limitado.forEachIndexed { i, c ->
//            append(c)
//            if (i == 3 || i == 5) append('-')
//        }
//    }
//}
//
//private fun fechaValida(fecha: String): Boolean {
//    if (fecha.length != 10) return false
//    val partes = fecha.split("-")
//    if (partes.size != 3) return false
//    val anio = partes[0].toIntOrNull() ?: return false
//    val mes  = partes[1].toIntOrNull() ?: return false
//    val dia  = partes[2].toIntOrNull() ?: return false
//    return anio in 1900..2025 && mes in 1..12 && dia in 1..31
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FormularioJugadorScreen(
//    jugador: Jugador? = null,
//    equipos: List<Equipo>,
//    jugadoresExistentes: List<Jugador>,
//    onBackClick: () -> Unit,
//    onGuardarClick: (Jugador) -> Unit
//) {
//    var nombre       by remember { mutableStateOf(jugador?.nombre       ?: "") }
//    var posicion     by remember { mutableStateOf(jugador?.posicion     ?: "") }
//    var dorsal       by remember { mutableStateOf(jugador?.dorsal?.toString() ?: "") }
//    var nacionalidad by remember { mutableStateOf(jugador?.nacionalidad ?: "") }
//    var fechaNac     by remember { mutableStateOf(jugador?.fechaNac     ?: "") }
//    var idEquipo     by remember { mutableStateOf(jugador?.idEquipo) }
//    var imageUri     by remember { mutableStateOf<Uri?>(null) }
//
//    var errorTexto         by remember { mutableStateOf<String?>(null) }
//    var mostrarConfirmacion by remember { mutableStateOf(false) }
//    var expandedEquipo     by remember { mutableStateOf(false) }
//    var expandedPosicion   by remember { mutableStateOf(false) }
//
//    val launcher = rememberLauncherForActivityResult(
//        ActivityResultContracts.GetContent()
//    ) { imageUri = it }
//
//    fun validar() {
//        errorTexto = when {
//            nombre.isBlank() || dorsal.isBlank() || posicion.isBlank() || idEquipo == null ->
//                "Por favor, completa todos los campos obligatorios."
//            fechaNac.isNotBlank() && !fechaValida(fechaNac) ->
//                "La fecha debe tener el formato AAAA-MM-DD y ser válida."
//            jugadoresExistentes.any {
//                it.idEquipo == idEquipo &&
//                        it.dorsal   == (dorsal.toIntOrNull() ?: 0) &&
//                        it.idJugador != jugador?.idJugador
//            } -> "El dorsal $dorsal ya está ocupado en este equipo."
//            posicion == "Portero" && jugadoresExistentes.any {
//                it.idEquipo  == idEquipo &&
//                        it.posicion  == "Portero" &&
//                        it.idJugador != jugador?.idJugador
//            } -> "${equipos.find { it.idEquipo == idEquipo }?.nombre ?: "Este equipo"} ya tiene un portero asignado."
//            else -> null
//        }
//        if (errorTexto == null) mostrarConfirmacion = true
//    }
//
//    // ── Diálogo de confirmación ────────────────────────────────────────────
//    if (mostrarConfirmacion) {
//        AlertDialog(
//            onDismissRequest = { mostrarConfirmacion = false },
//            containerColor   = FichaFondo,
//            shape            = RoundedCornerShape(20.dp),
//            title = {
//                Text(
//                    if (jugador == null) "¿Guardar nuevo jugador?" else "¿Guardar cambios?",
//                    color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp
//                )
//            },
//            text = {
//                Text(
//                    "Se guardará la información de $nombre.",
//                    color = TextoSec, fontSize = 14.sp
//                )
//            },
//            confirmButton = {
//                TextButton(onClick = {
//                    onGuardarClick(
//                        Jugador(
//                            idJugador    = jugador?.idJugador,
//                            nombre       = nombre,
//                            posicion     = posicion,
//                            dorsal       = dorsal.toIntOrNull() ?: 0,
//                            fechaNac     = fechaNac.ifBlank { jugador?.fechaNac ?: "" },
//                            nacionalidad = nacionalidad,
//                            idEquipo     = idEquipo
//                        )
//                    )
//                    mostrarConfirmacion = false
//                }) {
//                    Text("Guardar", color = Verde, fontWeight = FontWeight.Bold)
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { mostrarConfirmacion = false }) {
//                    Text("Cancelar", color = TextoSec)
//                }
//            }
//        )
//    }
//
//    // ── Layout principal ───────────────────────────────────────────────────
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(SuperficieAlt)
//            .verticalScroll(rememberScrollState())
//    ) {
//        // ── Hero header ────────────────────────────────────────────────────
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    Brush.verticalGradient(listOf(Color(0xFF0D2B1C), Color(0xFF163222)))
//                )
//                .statusBarsPadding()
//                .padding(horizontal = 20.dp, vertical = 20.dp)
//        ) {
//            Column {
//                // Fila de navegación
//                Row(
//                    modifier          = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(
//                        onClick  = onBackClick,
//                        modifier = Modifier
//                            .size(44.dp)
//                            .background(Color.White.copy(alpha = 0.12f), CircleShape)
//                    ) {
//                        Icon(
//                            Icons.Default.ArrowBackIosNew,
//                            contentDescription = "Atrás",
//                            tint               = Color.White,
//                            modifier           = Modifier.size(20.dp)
//                        )
//                    }
//                    Spacer(Modifier.weight(1f))
//                }
//
//                Spacer(Modifier.height(20.dp))
//
//                // Foto + título
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    // Avatar / foto
//                    Box(
//                        modifier = Modifier
//                            .size(72.dp)
//                            .clip(CircleShape)
//                            .background(FichaFondo)
//                            .clickable { launcher.launch("image/*") },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        if (imageUri != null) {
//                            AsyncImage(
//                                model          = imageUri,
//                                contentDescription = null,
//                                contentScale   = ContentScale.Crop,
//                                modifier       = Modifier.fillMaxSize()
//                            )
//                        } else {
//                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                                Icon(
//                                    Icons.Default.AddAPhoto,
//                                    null,
//                                    tint     = Verde,
//                                    modifier = Modifier.size(24.dp)
//                                )
//                                Spacer(Modifier.height(2.dp))
//                                Text("Foto", color = TextoSec, fontSize = 9.sp)
//                            }
//                        }
//                    }
//
//                    Spacer(Modifier.width(18.dp))
//
//                    Column(modifier = Modifier.weight(1f)) {
//                        Text(
//                            if (jugador == null) "Nuevo jugador" else "Editar perfil",
//                            color         = TextoSec,
//                            fontSize      = 12.sp,
//                            fontWeight    = FontWeight.SemiBold,
//                            letterSpacing = 0.8.sp
//                        )
//                        Text(
//                            if (nombre.isBlank()) "Sin nombre" else nombre,
//                            color      = Color.White,
//                            fontSize   = 22.sp,
//                            fontWeight = FontWeight.Black,
//                            lineHeight = 26.sp,
//                            maxLines   = 2,
//                            overflow   = TextOverflow.Ellipsis
//                        )
//                    }
//                }
//            }
//        }
//
//        // ── Cuerpo del formulario ──────────────────────────────────────────
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 20.dp),
//            verticalArrangement = Arrangement.spacedBy(14.dp)
//        ) {
//
//            // Error
//            if (errorTexto != null) {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    shape    = RoundedCornerShape(12.dp),
//                    colors   = CardDefaults.cardColors(containerColor = ColorError.copy(alpha = 0.12f))
//                ) {
//                    Row(
//                        modifier          = Modifier.padding(12.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(Icons.Default.Warning, null, tint = ColorError, modifier = Modifier.size(18.dp))
//                        Spacer(Modifier.width(10.dp))
//                        Text(errorTexto!!, color = ColorError, fontSize = 13.sp, modifier = Modifier.weight(1f))
//                    }
//                }
//            }
//
//            // ── Sección: Datos personales ──────────────────────────────────
//            SeccionLabel("Datos personales")
//
//            // Nombre
//            FormCampo(
//                label        = "Nombre completo *",
//                value        = nombre,
//                onValueChange = { nombre = it },
//                icon         = Icons.Default.Person
//            )
//
//            // Nacionalidad
//            FormCampo(
//                label        = "Nacionalidad",
//                value        = nacionalidad,
//                onValueChange = { nacionalidad = it },
//                icon         = Icons.Default.Public
//            )
//
//            // Fecha de nacimiento — campo especial con formato automático
//            Column {
//                FormCampo(
//                    label         = "Fecha de nacimiento (AAAA-MM-DD)",
//                    value         = fechaNac,
//                    onValueChange = { raw ->
//                        fechaNac = formatearFecha(raw, fechaNac)
//                    },
//                    icon          = Icons.Default.CalendarToday,
//                    keyboardType  = KeyboardType.Number,
//                    isError       = fechaNac.isNotBlank() && !fechaValida(fechaNac),
//                    placeholder   = "2001-07-30"
//                )
//                // Indicador de progreso de la fecha
//                if (fechaNac.isNotBlank()) {
//                    Spacer(Modifier.height(4.dp))
//                    Row(
//                        modifier          = Modifier.padding(horizontal = 4.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(6.dp)
//                    ) {
//                        val valida = fechaValida(fechaNac)
//                        Icon(
//                            if (valida) Icons.Default.CheckCircle else Icons.Default.Info,
//                            null,
//                            tint     = if (valida) Verde else Color(0xFFFFD600),
//                            modifier = Modifier.size(14.dp)
//                        )
//                        Text(
//                            if (valida) "Fecha válida" else "Escribe 8 dígitos: AAAA-MM-DD",
//                            color    = if (valida) Verde else Color(0xFFFFD600),
//                            fontSize = 11.sp
//                        )
//                    }
//                }
//            }
//
//            // ── Sección: Datos del jugador ─────────────────────────────────
//            Spacer(Modifier.height(4.dp))
//            SeccionLabel("Datos del jugador")
//
//            // Dorsal + Posición en fila
//            Row(
//                modifier              = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                FormCampo(
//                    label         = "Dorsal *",
//                    value         = dorsal,
//                    onValueChange = { if (it.length <= 3) dorsal = it },
//                    icon          = Icons.Default.Numbers,
//                    keyboardType  = KeyboardType.Number,
//                    modifier      = Modifier.weight(0.8f)
//                )
//
//                // Selector posición
//                Box(modifier = Modifier.weight(1.5f)) {
//                    ExposedDropdownMenuBox(
//                        expanded          = expandedPosicion,
//                        onExpandedChange  = { expandedPosicion = !expandedPosicion }
//                    ) {
//                        OutlinedTextField(
//                            value         = posicion.ifBlank { "" },
//                            onValueChange = {},
//                            readOnly      = true,
//                            label         = { Text("Posición *", color = TextoSec, fontSize = 12.sp) },
//                            modifier      = Modifier.menuAnchor().fillMaxWidth(),
//                            shape         = RoundedCornerShape(14.dp),
//                            trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPosicion) },
//                            colors        = campoColores()
//                        )
//                        ExposedDropdownMenu(
//                            expanded          = expandedPosicion,
//                            onDismissRequest  = { expandedPosicion = false },
//                            modifier          = Modifier.background(FichaFondo)
//                        ) {
//                            posiciones.forEach { pos ->
//                                DropdownMenuItem(
//                                    text    = { Text(pos, color = Color.White, fontSize = 13.sp) },
//                                    onClick = { posicion = pos; expandedPosicion = false }
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            // ── Sección: Equipo ────────────────────────────────────────────
//            Spacer(Modifier.height(4.dp))
//            SeccionLabel("Equipo")
//
//            ExposedDropdownMenuBox(
//                expanded         = expandedEquipo,
//                onExpandedChange = { expandedEquipo = !expandedEquipo }
//            ) {
//                OutlinedTextField(
//                    value         = equipos.find { it.idEquipo == idEquipo }?.nombre ?: "",
//                    onValueChange = {},
//                    readOnly      = true,
//                    label         = { Text("Club *", color = TextoSec, fontSize = 12.sp) },
//                    modifier      = Modifier.menuAnchor().fillMaxWidth(),
//                    leadingIcon   = { Icon(Icons.Default.Shield, null, tint = Verde, modifier = Modifier.size(20.dp)) },
//                    shape         = RoundedCornerShape(14.dp),
//                    colors        = campoColores()
//                )
//                ExposedDropdownMenu(
//                    expanded         = expandedEquipo,
//                    onDismissRequest = { expandedEquipo = false },
//                    modifier         = Modifier.background(FichaFondo)
//                ) {
//                    equipos.forEach { eq ->
//                        DropdownMenuItem(
//                            text    = { Text(eq.nombre, color = Color.White, fontSize = 13.sp) },
//                            onClick = { idEquipo = eq.idEquipo; expandedEquipo = false }
//                        )
//                    }
//                }
//            }
//
//            Spacer(Modifier.height(8.dp))
//
//            // ── Botón guardar ──────────────────────────────────────────────
//            Button(
//                onClick  = { validar() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(54.dp),
//                shape    = RoundedCornerShape(16.dp),
//                colors   = ButtonDefaults.buttonColors(containerColor = Verde)
//            ) {
//                Icon(Icons.Default.Save, null, modifier = Modifier.size(20.dp))
//                Spacer(Modifier.width(8.dp))
//                Text(
//                    if (jugador == null) "Guardar jugador" else "Guardar cambios",
//                    fontWeight = FontWeight.Bold,
//                    fontSize   = 15.sp,
//                    color      = Color.White
//                )
//            }
//
//            // Descartar
//            TextButton(
//                onClick  = onBackClick,
//                modifier = Modifier.align(Alignment.CenterHorizontally)
//            ) {
//                Icon(Icons.Default.Close, null, tint = TextoSec, modifier = Modifier.size(14.dp))
//                Spacer(Modifier.width(4.dp))
//                Text("Descartar cambios", color = TextoSec, fontSize = 13.sp)
//            }
//
//            Spacer(Modifier.height(32.dp))
//        }
//    }
//}
//
//// ── Etiqueta de sección ───────────────────────────────────────────────────
//@Composable
//private fun SeccionLabel(texto: String) {
//    Text(
//        texto,
//        color         = TextoSec,
//        fontSize      = 11.sp,
//        fontWeight    = FontWeight.Bold,
//        letterSpacing = 0.6.sp
//    )
//}
//
//// ── Colores compartidos para OutlinedTextField ────────────────────────────
//@Composable
//private fun campoColores() = OutlinedTextFieldDefaults.colors(
//    focusedContainerColor   = FichaFondo,
//    unfocusedContainerColor = FichaFondo,
//    focusedBorderColor      = Verde,
//    unfocusedBorderColor    = BorderSutil,
//    focusedTextColor        = Color.White,
//    unfocusedTextColor      = Color.White,
//    cursorColor             = Verde,
//    focusedLabelColor       = Verde,
//    unfocusedLabelColor     = TextoSec
//)
//
//// ── Campo de texto genérico ───────────────────────────────────────────────
//@Composable
//fun FormCampo(
//    label: String,
//    value: String,
//    onValueChange: (String) -> Unit,
//    icon: ImageVector,
//    modifier: Modifier      = Modifier,
//    keyboardType: KeyboardType = KeyboardType.Text,
//    isError: Boolean        = false,
//    placeholder: String     = ""
//) {
//    OutlinedTextField(
//        value         = value,
//        onValueChange = onValueChange,
//        label         = { Text(label, fontSize = 12.sp) },
//        placeholder   = if (placeholder.isNotBlank()) {
//            { Text(placeholder, color = TextoSec.copy(alpha = 0.6f), fontSize = 13.sp) }
//        } else null,
//        modifier      = modifier.fillMaxWidth(),
//        leadingIcon   = {
//            Icon(icon, null, tint = if (isError) ColorError else Verde, modifier = Modifier.size(20.dp))
//        },
//        shape         = RoundedCornerShape(14.dp),
//        singleLine    = true,
//        isError       = isError,
//        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
//        colors        = OutlinedTextFieldDefaults.colors(
//            focusedContainerColor   = FichaFondo,
//            unfocusedContainerColor = FichaFondo,
//            focusedBorderColor      = if (isError) ColorError else Verde,
//            unfocusedBorderColor    = if (isError) ColorError.copy(alpha = 0.5f) else BorderSutil,
//            focusedTextColor        = Color.White,
//            unfocusedTextColor      = Color.White,
//            cursorColor             = Verde,
//            focusedLabelColor       = if (isError) ColorError else Verde,
//            unfocusedLabelColor     = TextoSec,
//            errorBorderColor        = ColorError,
//            errorLabelColor         = ColorError
//        )
//    )
//}


package com.example.football_team_frontend.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioJugadorScreen(
    jugador: Jugador? = null,
    equipos: List<Equipo>,
    jugadoresExistentes: List<Jugador>,
    onBackClick: () -> Unit,
    onGuardarClick: (Jugador) -> Unit
) {
    var nombre by remember { mutableStateOf(jugador?.nombre ?: "") }
    var posicion by remember { mutableStateOf(jugador?.posicion ?: "") }
    var dorsal by remember { mutableStateOf(jugador?.dorsal?.toString() ?: "") }
    var nacionalidad by remember { mutableStateOf(jugador?.nacionalidad ?: "") }
    var idEquipo by remember { mutableStateOf(jugador?.idEquipo) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var fechaNacimiento by remember { mutableStateOf(jugador?.fechaNac ?: "")
    }
    var mostrarDatePicker by remember { mutableStateOf(false)
    }
    var errorTexto by remember { mutableStateOf<String?>(null) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var expandedEquipo by remember { mutableStateOf(false) }
    var expandedPosicion by remember { mutableStateOf(false) }

    val posiciones = listOf(
        "Portero",
        "Defensa Central",
        "Lateral Derecho",
        "Lateral Izquierdo",
        "Mediocampista Defensivo",
        "Mediocampista Central",
        "Mediocampista Ofensivo",
        "Extremo Derecho",
        "Extremo Izquierdo",
        "Delantero Centro",
        "Segundo Delantero"
    )

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri = it }

    fun validar() {
        if (nombre.isBlank() || dorsal.isBlank() || posicion.isBlank() || idEquipo == null) {
            errorTexto = "Por favor, completa todos los campos obligatorios."
            return
        }
        val dorsalInt = dorsal.toIntOrNull() ?: 0
        if (jugadoresExistentes.any { it.idEquipo == idEquipo && it.dorsal == dorsalInt && it.idJugador != jugador?.idJugador }) {
            errorTexto = "El dorsal $dorsal ya está ocupado en este equipo."
            return
        }

        // Regla: Solo un portero por equipo
        if (posicion == "Portero" && jugadoresExistentes.any { it.idEquipo == idEquipo && it.posicion == "Portero" && it.idJugador != jugador?.idJugador }) {
            val eqNombre = equipos.find { it.idEquipo == idEquipo }?.nombre ?: "este equipo"
            errorTexto = "$eqNombre ya tiene un portero asignado."
            return
        }

        errorTexto = null
        mostrarConfirmacion = true
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            containerColor = Color(0xFF1F4A43),
            titleContentColor = Blanco,
            textContentColor = GrisClaro,
            title = { Text("¿Guardar cambios?", fontWeight = FontWeight.Bold) },
            text = { Text("Se actualizará la información de $nombre en la base de datos.") },
            confirmButton = {
                TextButton(onClick = {

                    onGuardarClick(
                        Jugador(
                            jugador?.idJugador,
                            nombre,
                            posicion,
                            dorsal.toIntOrNull() ?: 0,
                            fechaNacimiento,
                            nacionalidad,
                            idEquipo
                        )
                    )

                    mostrarConfirmacion = false

                }) {

                    Text(
                        "Aceptar",
                        color = Verde
                    )
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        mostrarDatePicker = false
                    }

                ) {

                    Text(
                        "Cancelar",
                        color = GrisClaro
                    )
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(VerdeOscuro)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Cabecera
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.background(Color(0xFF1F4A43), CircleShape).size(36.dp)
                ) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = Blanco, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = if (jugador == null) "Nuevo Jugador" else "Editar Perfil",
                    color = Blanco, fontSize = 22.sp, fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Foto de Perfil
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(Color(0xFF1F4A43))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(model = imageUri, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                } else {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Verde, modifier = Modifier.size(30.dp))
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (errorTexto != null) {
                Text(errorTexto!!, color = Color(0xFFFF6B6B), fontSize = 13.sp, modifier = Modifier.padding(bottom = 16.dp))
            }

            // Campos (Distribución Original)
            FormTextField("Nombre Completo", nombre, { nombre = it }, Icons.Default.Person)

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FormTextField("Dorsal", dorsal, { if (it.length <= 3) dorsal = it }, Icons.Default.Numbers, Modifier.weight(1f))

                // Selector Posición
                Box(modifier = Modifier.weight(1.5f)) {
                    ExposedDropdownMenuBox(expanded = expandedPosicion, onExpandedChange = { expandedPosicion = !expandedPosicion }) {
                        OutlinedTextField(
                            value = posicion, onValueChange = {}, readOnly = true,
                            label = { Text("Posición", color = GrisClaro) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPosicion) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF1F4A43),
                                unfocusedContainerColor = Color(0xFF1F4A43),
                                focusedBorderColor = Verde,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = Blanco,
                                unfocusedTextColor = Blanco
                            )
                        )
                        ExposedDropdownMenu(expanded = expandedPosicion, onDismissRequest = { expandedPosicion = false }, modifier = Modifier.background(Color(0xFF1F4A43))) {
                            posiciones.forEach { pos ->
                                DropdownMenuItem(text = { Text(pos, color = Blanco) }, onClick = { posicion = pos; expandedPosicion = false })
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            FormTextField("Nacionalidad", nacionalidad, { nacionalidad = it }, Icons.Default.Public)

            Spacer(modifier = Modifier.height(16.dp))
            // ── CAMPO FECHA DE NACIMIENTO  ─────────────────────────────

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        mostrarDatePicker = true
                    }
            ) {

                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,

                    label = {
                        Text(
                            "Fecha de Nacimiento",
                            color = GrisClaro
                        )
                    },

                    leadingIcon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Verde
                        )
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(18.dp),

                    colors = OutlinedTextFieldDefaults.colors(

                        disabledContainerColor = Color(0xFF1F4A43),
                        disabledBorderColor = Color.Transparent,
                        disabledTextColor = Blanco,
                        disabledLabelColor = GrisClaro,

                        focusedContainerColor = Color(0xFF1F4A43),
                        unfocusedContainerColor = Color(0xFF1F4A43),

                        focusedBorderColor = Verde,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

// ── DATE PICKER ─────────────────────────────────────────────────────

            if (mostrarDatePicker) {

                val datePickerState = rememberDatePickerState()

                DatePickerDialog(

                    onDismissRequest = {
                        mostrarDatePicker = false
                    },

                    confirmButton = {

                        TextButton(

                            onClick = {

                                datePickerState.selectedDateMillis?.let { millis ->

                                    val formatter = SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    )

                                    fechaNacimiento = formatter.format(
                                        java.util.Date(millis)
                                    )
                                }

                                mostrarDatePicker = false
                            }

                        ) {

                            Text(
                                "Aceptar",
                                color = Verde
                            )
                        }
                    },

                    dismissButton = {

                        TextButton(

                            onClick = {
                                mostrarDatePicker = false
                            }

                        ) {

                            Text(
                                "Cancelar",
                                color = GrisClaro
                            )
                        }
                    }

                ) {

                    DatePicker(
                        state = datePickerState
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Selector Equipo
            ExposedDropdownMenuBox(expanded = expandedEquipo, onExpandedChange = { expandedEquipo = !expandedEquipo }) {
                OutlinedTextField(
                    value = equipos.find { it.idEquipo == idEquipo }?.nombre ?: "Seleccionar Equipo",
                    onValueChange = {}, readOnly = true,
                    label = { Text("Equipo", color = GrisClaro) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Shield, contentDescription = null, tint = Verde) },
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1F4A43),
                        unfocusedContainerColor = Color(0xFF1F4A43),
                        focusedBorderColor = Verde,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Blanco,
                        unfocusedTextColor = Blanco
                    )
                )
                ExposedDropdownMenu(expanded = expandedEquipo, onDismissRequest = { expandedEquipo = false }, modifier = Modifier.background(Color(0xFF1F4A43))) {
                    equipos.forEach { eq ->
                        DropdownMenuItem(text = { Text(eq.nombre, color = Blanco) }, onClick = { idEquipo = eq.idEquipo; expandedEquipo = false })
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { validar() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verde)
            ) {
                Text("GUARDAR JUGADOR", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Blanco)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Descartar cambios", color = GrisClaro)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun FormTextField(label: String, value: String, onValueChange: (String) -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label, color = GrisClaro) },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = { Icon(icon, contentDescription = null, tint = Verde) },
        shape = RoundedCornerShape(18.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1F4A43),
            unfocusedContainerColor = Color(0xFF1F4A43),
            focusedBorderColor = Verde,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Blanco,
            unfocusedTextColor = Blanco,
            cursorColor = Verde
        )
    )
}

// ── Preview ───────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0D2B1C, showSystemUi = true)
@Composable
fun FormularioJugadorPreview() {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val jugadorFake = Jugador(
        idJugador   = 5L,
        nombre      = "Daniel Ruiz",
        posicion    = "Extremo Izquierdo",
        dorsal      = 18,
        fechaNac    = "2001-07-30",
        nacionalidad = "Colombiano",
        idEquipo    = 1L
    )
    val equipoFake = Equipo(
        idEquipo = 1L,
        nombre   = "Millonarios FC",
        ciudad   = "Bogotá", formato.parse("2002-02-02")!!
    )
    DetalleJugadorScreen(
        jugador         = jugadorFake,
        equipo          = equipoFake,
        onBackClick     = {},
        onEditarClick   = {},
        onEliminarClick = {}
    )
}