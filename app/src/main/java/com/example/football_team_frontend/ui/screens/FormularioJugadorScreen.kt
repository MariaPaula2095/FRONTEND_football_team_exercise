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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.football_team_frontend.ui.theme.VerdeOscuro

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
    var fechaNac by remember { mutableStateOf(jugador?.fechaNac ?: "") }
    var nacionalidad by remember { mutableStateOf(jugador?.nacionalidad ?: "") }
    var idEquipo by remember { mutableStateOf(jugador?.idEquipo) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var errorTexto by remember { mutableStateOf<String?>(null) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var expandedEquipo by remember { mutableStateOf(false) }
    var expandedPosicion by remember { mutableStateOf(false) }

    // Sincronizar imageUri si el jugador ya tiene una (suponiendo que guardamos el path)
    // Por ahora se mantiene local en la sesión de edición.

    val posiciones = listOf(
        "Portero", "Arquero", "Guardameta",
        "Defensa central", "Lateral derecho", "Lateral izquierdo", "Líbero", "Carrilero derecho", "Carrilero izquierdo",
        "Mediocampista defensivo", "Volante defensivo", "Mediocentro", "Mediocampista ofensivo", "Volante creativo", "Interior derecho", "Interior izquierdo",
        "Extremo derecho", "Extremo izquierdo",
        "Delantero centro", "Centrodelantero", "Segundo delantero", "Punta", "Falso 9"
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    fun validarYConfirmar() {
        if (nombre.isBlank()) {
            errorTexto = "El nombre no puede estar vacío"
            return
        }
        val dorsalInt = dorsal.toIntOrNull() ?: 0
        if (dorsalInt <= 0) {
            errorTexto = "Ingresa un dorsal válido"
            return
        }
        if (posicion.isBlank()) {
            errorTexto = "Debes seleccionar una posición"
            return
        }
        if (idEquipo == null) {
            errorTexto = "Debes seleccionar un equipo"
            return
        }

        val existeDorsal = jugadoresExistentes.any { 
            it.idEquipo == idEquipo && it.dorsal == dorsalInt && it.idJugador != jugador?.idJugador 
        }

        if (existeDorsal) {
            errorTexto = "El dorsal $dorsal ya existe en este equipo"
        } else {
            errorTexto = null
            mostrarConfirmacion = true
        }
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text(if (jugador == null) "Confirmar Creación" else "Confirmar Actualización", fontWeight = FontWeight.Bold) },
            text = { Text("¿Deseas guardar los cambios para el jugador $nombre?") },
            confirmButton = {
                TextButton(onClick = {
                    val nuevoJugador = Jugador(
                        idJugador = jugador?.idJugador,
                        nombre = nombre,
                        posicion = posicion,
                        dorsal = dorsal.toIntOrNull() ?: 0,
                        fechaNac = fechaNac,
                        nacionalidad = nacionalidad,
                        idEquipo = idEquipo
                    )
                    onGuardarClick(nuevoJugador)
                    mostrarConfirmacion = false
                }) {
                    Text("ACEPTAR", color = VerdeOscuro, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("CANCELAR", color = Color.Gray)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (jugador == null) "NUEVO JUGADOR" else "ACTUALIZAR JUGADOR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VerdeOscuro)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Foto de perfil con selector
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEEEEE))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(32.dp))
                        Text("Foto", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }

            if (errorTexto != null) {
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = errorTexto!!, color = Color(0xFFC62828), modifier = Modifier.padding(12.dp), fontSize = 14.sp)
                }
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeOscuro,
                    unfocusedBorderColor = Color.Gray,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    cursorColor = VerdeOscuro,
                    focusedLabelColor = VerdeOscuro
                )
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = dorsal,
                    onValueChange = { if (it.all { char -> char.isDigit() }) dorsal = it },
                    label = { Text("Dorsal") },
                    modifier = Modifier.weight(0.3f),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VerdeOscuro,
                        unfocusedBorderColor = Color.Gray,
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black
                    )
                )

                // Selector de Posición Mejorado con ExposedDropdownMenuBox
                Box(modifier = Modifier.weight(0.7f)) {
                    ExposedDropdownMenuBox(
                        expanded = expandedPosicion,
                        onExpandedChange = { expandedPosicion = !expandedPosicion }
                    ) {
                        OutlinedTextField(
                            value = posicion,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Posición") },
                            placeholder = { Text("Seleccionar") },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPosicion) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdeOscuro,
                                unfocusedBorderColor = Color.Gray,
                                unfocusedTextColor = Color.Black,
                                focusedTextColor = Color.Black
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPosicion,
                            onDismissRequest = { expandedPosicion = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            posiciones.forEach { pos ->
                                DropdownMenuItem(
                                    text = { Text(pos, color = Color.Black) },
                                    onClick = {
                                        posicion = pos
                                        expandedPosicion = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = nacionalidad,
                onValueChange = { nacionalidad = it },
                label = { Text("Nacionalidad") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VerdeOscuro,
                    unfocusedBorderColor = Color.Gray,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black
                )
            )

            // Selector de Equipo Mejorado
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Asignar Equipo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VerdeOscuro)
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    ExposedDropdownMenuBox(
                        expanded = expandedEquipo,
                        onExpandedChange = { expandedEquipo = !expandedEquipo }
                    ) {
                        OutlinedTextField(
                            value = equipos.find { it.idEquipo == idEquipo }?.nombre ?: "Seleccionar Equipo",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipo) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VerdeOscuro,
                                unfocusedBorderColor = Color.Gray,
                                unfocusedTextColor = if (idEquipo == null) Color.Gray else Color.Black,
                                focusedTextColor = Color.Black
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedEquipo,
                            onDismissRequest = { expandedEquipo = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            equipos.forEach { equipo ->
                                DropdownMenuItem(
                                    text = { Text(equipo.nombre, color = Color.Black) },
                                    onClick = {
                                        idEquipo = equipo.idEquipo
                                        expandedEquipo = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { validarYConfirmar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro)
            ) {
                Text("GUARDAR CAMBIOS", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("DESCARTAR", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FormularioJugadorPreview() {
    val equiposDePrueba = listOf(
        Equipo(idEquipo = 1, nombre = "Atlético Nacional", ciudad = "Medellín", fundacion = java.util.Date())
    )
    FormularioJugadorScreen(
        jugador = null,
        equipos = equiposDePrueba,
        jugadoresExistentes = emptyList(),
        onBackClick = {},
        onGuardarClick = {}
    )
}
