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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.model.Jugador
import com.example.football_team_frontend.ui.components.FormTextField
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
    var fechaNacimiento by remember { mutableStateOf(jugador?.fechaNac ?: "") }
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var errorTexto by remember { mutableStateOf<String?>(null) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var expandedEquipo by remember { mutableStateOf(false) }
    var expandedPosicion by remember { mutableStateOf(false) }

    val posiciones = listOf(
        "Portero", "Defensa Central", "Lateral Derecho", "Lateral Izquierdo",
        "Mediocampista Defensivo", "Mediocampista Central", "Mediocampista Ofensivo",
        "Extremo Derecho", "Extremo Izquierdo", "Delantero Centro", "Segundo Delantero"
    )

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri = it }

    fun validar() {
        if (nombre.isBlank() || dorsal.isBlank() || posicion.isBlank() || idEquipo == null) {
            errorTexto = "POR FAVOR, COMPLETA TODOS LOS CAMPOS OBLIGATORIOS."
            return
        }
        val dorsalInt = dorsal.toIntOrNull() ?: 0
        if (jugadoresExistentes.any { it.idEquipo == idEquipo && it.dorsal == dorsalInt && it.idJugador != jugador?.idJugador }) {
            errorTexto = "EL DORSAL $dorsal YA ESTÁ OCUPADO EN ESTE EQUIPO."
            return
        }

        if (posicion == "Portero" && jugadoresExistentes.any { it.idEquipo == idEquipo && it.posicion == "Portero" && it.idJugador != jugador?.idJugador }) {
            val eqNombre = equipos.find { it.idEquipo == idEquipo }?.nombre ?: "ESTE EQUIPO"
            errorTexto = "$eqNombre YA TIENE UN PORTERO ASIGNADO."
            return
        }

        errorTexto = null
        mostrarConfirmacion = true
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            containerColor = FichaFondo,
            shape = RoundedCornerShape(20.dp),
            title = { Text("¿GUARDAR CAMBIOS?", color = Blanco, fontWeight = FontWeight.Black, fontSize = 16.sp) },
            text = { Text("Se actualizará la información de $nombre en el sistema.", color = TextoSec, fontSize = 14.sp) },
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
                    Text("ACEPTAR", color = Verde, fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("CANCELAR", color = TextoSec)
                }
            }
        )
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
                    onClick = onBackClick,
                    modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBackIosNew, null, tint = Blanco, modifier = Modifier.size(18.dp))
                }
                Text(
                    text = if (jugador == null) "NUEVO JUGADOR" else "EDITAR PERFIL",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Blanco,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Foto de Perfil / Avatar
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(FichaFondo)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(model = imageUri, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                } else {
                    Icon(Icons.Default.AddAPhoto, null, tint = Verde, modifier = Modifier.size(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (errorTexto != null) {
                Text(errorTexto!!, color = ErrorRed, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
            }

            FormTextField("NOMBRE COMPLETO", nombre, { newVal: String -> nombre = newVal }, Icons.Default.Person)

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FormTextField("DORSAL", dorsal, { newVal: String -> if (newVal.all { c: Char -> c.isDigit() } && newVal.length <= 3) dorsal = newVal }, Icons.Default.Numbers, Modifier.weight(1f))

                Box(modifier = Modifier.weight(1.5f)) {
                    ExposedDropdownMenuBox(expanded = expandedPosicion, onExpandedChange = { expandedPosicion = !expandedPosicion }) {
                        OutlinedTextField(
                            value = posicion.uppercase(), onValueChange = {}, readOnly = true,
                            label = { Text("POSICIÓN", color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Black) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPosicion) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = FichaFondo,
                                unfocusedContainerColor = FichaFondo,
                                focusedBorderColor = Verde,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = Blanco,
                                unfocusedTextColor = Blanco,
                                focusedLabelColor = Verde,
                                unfocusedLabelColor = TextoSec
                            )
                        )
                        ExposedDropdownMenu(expanded = expandedPosicion, onDismissRequest = { expandedPosicion = false }, modifier = Modifier.background(FichaFondo)) {
                            posiciones.forEach { pos ->
                                DropdownMenuItem(text = { Text(pos.uppercase(), color = Blanco, fontSize = 12.sp, fontWeight = FontWeight.Bold) }, onClick = { posicion = pos; expandedPosicion = false })
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            FormTextField("NACIONALIDAD", nacionalidad, { newVal: String -> nacionalidad = newVal }, Icons.Default.Public)


            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth().clickable { mostrarDatePicker = true }) {
                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("FECHA DE NACIMIENTO", color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Black) },
                    leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = Verde, modifier = Modifier.size(20.dp)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = FichaFondo,
                        disabledBorderColor = Color.Transparent,
                        disabledTextColor = Blanco,
                        disabledLabelColor = TextoSec,
                        focusedContainerColor = FichaFondo,
                        unfocusedContainerColor = FichaFondo,
                        focusedBorderColor = Verde,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            if (mostrarDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { mostrarDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                fechaNacimiento = formatter.format(java.util.Date(millis))
                            }
                            mostrarDatePicker = false
                        }) { Text("ACEPTAR", color = Verde, fontWeight = FontWeight.Black) }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDatePicker = false }) { Text("CANCELAR", color = TextoSec) }
                    }
                ) { DatePicker(state = datePickerState) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(expanded = expandedEquipo, onExpandedChange = { expandedEquipo = !expandedEquipo }) {
                OutlinedTextField(
                    value = (equipos.find { it.idEquipo == idEquipo }?.nombre ?: "SELECCIONAR EQUIPO").uppercase(),
                    onValueChange = {}, readOnly = true,
                    label = { Text("EQUIPO", color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Black) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Shield, null, tint = Verde, modifier = Modifier.size(20.dp)) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FichaFondo,
                        unfocusedContainerColor = FichaFondo,
                        focusedBorderColor = Verde,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Blanco,
                        unfocusedTextColor = Blanco
                    )
                )
                ExposedDropdownMenu(expanded = expandedEquipo, onDismissRequest = { expandedEquipo = false }, modifier = Modifier.background(FichaFondo)) {
                    equipos.forEach { eq ->
                        DropdownMenuItem(text = { Text(eq.nombre.uppercase(), color = Blanco, fontSize = 12.sp, fontWeight = FontWeight.Bold) }, onClick = { idEquipo = eq.idEquipo; expandedEquipo = false })
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { validar() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verde)
            ) {
                Text("GUARDAR JUGADOR", fontWeight = FontWeight.Black, fontSize = 15.sp, color = Blanco, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("DESCARTAR CAMBIOS", color = TextoSec, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

