package com.example.football_team_frontend.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Entrenador
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioEntrenadorScreen(
    entrenador: Entrenador? = null,
    equipos: List<Equipo>,
    onBackClick: () -> Unit,
    onGuardarClick: (Entrenador) -> Unit
) {
    var nombre              by remember { mutableStateOf(entrenador?.nombre ?: "") }
    var especialidad        by remember { mutableStateOf(entrenador?.especialidad ?: "") }
    var equipoSeleccionado  by remember { mutableStateOf(entrenador?.equipo) }
    var expandedEspecialidad by remember { mutableStateOf(false) }
    var expandedEquipo      by remember { mutableStateOf(false) }
    var errorTexto          by remember { mutableStateOf<String?>(null) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    val especialidades = listOf(
        "Entrenador Principal",
        "Asistente Técnico",
        "Entrenador de Porteros",
        "Preparador Físico",
        "Analista Táctico"
    )

    fun validar() {
        if (nombre.isBlank() || especialidad.isBlank() || equipoSeleccionado == null) {
            errorTexto = "Por favor, completa todos los campos obligatorios."
            return
        }
        errorTexto = null
        mostrarConfirmacion = true
    }

    // ── Diálogo de confirmación ───────────────────────────────────────────
    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest  = { mostrarConfirmacion = false },
            containerColor    = Color(0xFF1F4A43),
            titleContentColor = Blanco,
            textContentColor  = GrisClaro,
            title = { Text("¿Guardar cambios?", fontWeight = FontWeight.Bold) },
            text  = { Text("Se guardará la información de $nombre en la base de datos.") },
            confirmButton = {
                TextButton(onClick = {
                    onGuardarClick(
                        Entrenador(
                            idEntrenador = entrenador?.idEntrenador ?: 0L,
                            nombre       = nombre,
                            especialidad = especialidad,
                            equipo       = equipoSeleccionado!!
                        )
                    )
                    mostrarConfirmacion = false
                }) {
                    Text("Aceptar", color = Verde)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("Cancelar", color = GrisClaro)
                }
            }
        )
    }

    // ── Contenido principal ───────────────────────────────────────────────
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
                    onClick  = onBackClick,
                    modifier = Modifier
                        .background(Color(0xFF1F4A43), CircleShape)
                        .size(36.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                        tint     = Blanco,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text       = if (entrenador == null) "Nuevo Entrenador" else "Editar Entrenador",
                    color      = Blanco,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Avatar con iniciales
            Box(
                modifier         = Modifier
                    .size(110.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color(0xFF1F4A43), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (nombre.isBlank()) "?" else nombre
                        .split(" ")
                        .filter { it.isNotEmpty() }
                        .take(2)
                        .joinToString("") { it.first().uppercaseChar().toString() },
                    color      = Verde,
                    fontSize   = 32.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Error
            if (errorTexto != null) {
                Text(
                    errorTexto!!,
                    color    = Color(0xFFFF6B6B),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // ── Nombre ────────────────────────────────────────────────────
            FormTextField(
                label         = "Nombre Completo",
                value         = nombre,
                onValueChange = { nombre = it },
                icon          = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Especialidad dropdown ─────────────────────────────────────
            ExposedDropdownMenuBox(
                expanded        = expandedEspecialidad,
                onExpandedChange = { expandedEspecialidad = !expandedEspecialidad }
            ) {
                OutlinedTextField(
                    value         = especialidad,
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Especialidad", color = GrisClaro) },
                    modifier      = Modifier.menuAnchor().fillMaxWidth(),
                    leadingIcon   = { Icon(Icons.Default.Star, null, tint = Verde) },
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEspecialidad) },
                    shape         = RoundedCornerShape(18.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor   = Color(0xFF1F4A43),
                        unfocusedContainerColor = Color(0xFF1F4A43),
                        focusedBorderColor      = Verde,
                        unfocusedBorderColor    = Color.Transparent,
                        focusedTextColor        = Blanco,
                        unfocusedTextColor      = Blanco
                    )
                )
                ExposedDropdownMenu(
                    expanded        = expandedEspecialidad,
                    onDismissRequest = { expandedEspecialidad = false },
                    modifier        = Modifier.background(Color(0xFF1F4A43))
                ) {
                    especialidades.forEach { esp ->
                        DropdownMenuItem(
                            text    = { Text(esp, color = Blanco) },
                            onClick = { especialidad = esp; expandedEspecialidad = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Equipo dropdown ───────────────────────────────────────────
            ExposedDropdownMenuBox(
                expanded         = expandedEquipo,
                onExpandedChange = { expandedEquipo = !expandedEquipo }
            ) {
                OutlinedTextField(
                    value         = equipoSeleccionado?.nombre ?: "Seleccionar Equipo",
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Equipo", color = GrisClaro) },
                    modifier      = Modifier.menuAnchor().fillMaxWidth(),
                    leadingIcon   = { Icon(Icons.Default.Shield, null, tint = Verde) },
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipo) },
                    shape         = RoundedCornerShape(18.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor   = Color(0xFF1F4A43),
                        unfocusedContainerColor = Color(0xFF1F4A43),
                        focusedBorderColor      = Verde,
                        unfocusedBorderColor    = Color.Transparent,
                        focusedTextColor        = Blanco,
                        unfocusedTextColor      = Blanco
                    )
                )
                ExposedDropdownMenu(
                    expanded         = expandedEquipo,
                    onDismissRequest = { expandedEquipo = false },
                    modifier         = Modifier.background(Color(0xFF1F4A43))
                ) {
                    equipos.forEach { eq ->
                        DropdownMenuItem(
                            text    = { Text(eq.nombre, color = Blanco) },
                            onClick = { equipoSeleccionado = eq; expandedEquipo = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Botón guardar ─────────────────────────────────────────────
            Button(
                onClick  = { validar() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape    = RoundedCornerShape(18.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Verde)
            ) {
                Text(
                    text       = if (entrenador == null) "GUARDAR ENTRENADOR" else "ACTUALIZAR ENTRENADOR",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    color      = Blanco
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick  = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Descartar cambios", color = GrisClaro)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ── Preview ───────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0D2B1C, showSystemUi = true)
@Composable
fun FormularioEntrenadorPreview() {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val equiposFake = listOf(
        Equipo(1L, "Millonarios FC",    "Bogotá",   formato.parse("1946-06-18")!!),
        Equipo(2L, "Atlético Nacional", "Medellín", formato.parse("1947-03-07")!!)
    )
    val entrenadorFake = Entrenador(
        idEntrenador = 1L,
        nombre       = "Alberto Gamero",
        especialidad = "Entrenador Principal",
        equipo       = equiposFake[0]
    )
    FormularioEntrenadorScreen(
        entrenador     = entrenadorFake,
        equipos        = equiposFake,
        onBackClick    = {},
        onGuardarClick = {}
    )
}