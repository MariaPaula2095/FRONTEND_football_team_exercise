package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Entrenador
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.ui.components.FormTextField
import com.example.football_team_frontend.ui.theme.*

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
            errorTexto = "POR FAVOR, COMPLETA TODOS LOS CAMPOS."
            return
        }
        errorTexto = null
        mostrarConfirmacion = true
    }

    // ── Diálogo de confirmación ───────────────────────────────────────────
    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest  = { mostrarConfirmacion = false },
            containerColor    = FichaFondo,
            shape            = RoundedCornerShape(20.dp),
            title = {
                Text("¿GUARDAR CAMBIOS?", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
            },
            text  = {
                Text(
                    "Se guardará la información de $nombre en la base de datos.",
                    color = TextoSec, fontSize = 14.sp
                )
            },
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
                    text = if (entrenador == null) "NUEVO ENTRENADOR" else "EDITAR ENTRENADOR",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White,
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

            // Avatar con iniciales
            Box(
                modifier         = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Verde.copy(alpha = 0.1f), CircleShape)
                    .padding(4.dp)
                    .background(Verde.copy(alpha = 0.05f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (nombre.isBlank()) "?" else nombre
                        .split(" ")
                        .filter { it.isNotEmpty() }
                        .take(2)
                        .joinToString("") { it.first().uppercaseChar().toString() },
                    color      = Verde,
                    fontSize   = 36.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "DATOS PROFESIONALES",
                color = TextoSec,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Error
            if (errorTexto != null) {
                Text(
                    errorTexto!!,
                    color    = ErrorRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // ── Nombre ────────────────────────────────────────────────────
            FormTextField(
                label         = "NOMBRE COMPLETO",
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
                    value         = especialidad.uppercase(),
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("ESPECIALIDAD", fontSize = 10.sp, fontWeight = FontWeight.Black) },
                    modifier      = Modifier.menuAnchor().fillMaxWidth(),
                    leadingIcon   = { Icon(Icons.Default.Star, null, tint = Verde, modifier = Modifier.size(20.dp)) },
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEspecialidad) },
                    shape         = RoundedCornerShape(16.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor   = FichaFondo,
                        unfocusedContainerColor = FichaFondo,
                        focusedBorderColor      = Verde,
                        unfocusedBorderColor    = Color.Transparent,
                        focusedTextColor        = Color.White,
                        unfocusedTextColor      = Color.White,
                        focusedLabelColor       = Verde,
                        unfocusedLabelColor     = TextoSec
                    )
                )
                ExposedDropdownMenu(
                    expanded        = expandedEspecialidad,
                    onDismissRequest = { expandedEspecialidad = false },
                    modifier        = Modifier.background(FichaFondo)
                ) {
                    especialidades.forEach { esp ->
                        DropdownMenuItem(
                            text    = { Text(esp.uppercase(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
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
                    value         = (equipoSeleccionado?.nombre ?: "SELECCIONAR EQUIPO").uppercase(),
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("EQUIPO ASIGNADO", fontSize = 10.sp, fontWeight = FontWeight.Black) },
                    modifier      = Modifier.menuAnchor().fillMaxWidth(),
                    leadingIcon   = { Icon(Icons.Default.Shield, null, tint = Verde, modifier = Modifier.size(20.dp)) },
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipo) },
                    shape         = RoundedCornerShape(16.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor   = FichaFondo,
                        unfocusedContainerColor = FichaFondo,
                        focusedBorderColor      = Verde,
                        unfocusedBorderColor    = Color.Transparent,
                        focusedTextColor        = Color.White,
                        unfocusedTextColor      = Color.White,
                        focusedLabelColor       = Verde,
                        unfocusedLabelColor     = TextoSec
                    )
                )
                ExposedDropdownMenu(
                    expanded         = expandedEquipo,
                    onDismissRequest = { expandedEquipo = false },
                    modifier         = Modifier.background(FichaFondo)
                ) {
                    equipos.forEach { eq ->
                        DropdownMenuItem(
                            text    = { Text(eq.nombre.uppercase(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            onClick = { equipoSeleccionado = eq; expandedEquipo = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // ── Botón guardar ─────────────────────────────────────────────
            Button(
                onClick  = { validar() },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Verde)
            ) {
                Icon(if(entrenador==null) Icons.Default.Add else Icons.Default.Save, null)
                Spacer(Modifier.width(8.dp))
                Text(
                    text       = if (entrenador == null) "GUARDAR ENTRENADOR" else "ACTUALIZAR ENTRENADOR",
                    fontWeight = FontWeight.Black,
                    fontSize   = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick  = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("CANCELAR", color = TextoSec, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
