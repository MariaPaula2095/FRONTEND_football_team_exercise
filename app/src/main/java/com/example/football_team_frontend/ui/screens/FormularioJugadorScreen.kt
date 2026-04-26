

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