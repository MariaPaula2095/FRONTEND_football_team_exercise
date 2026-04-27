package com.example.football_team_frontend.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.model.Partido
import com.example.football_team_frontend.ui.components.FormTextField
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioPartidoScreen(
    partido: Partido? = null,
    equipos: List<Equipo>,
    isEditMode: Boolean = false,
    mensajeExterno: String? = null,
    onBackClick: () -> Unit,
    onGuardarClick: (Partido) -> Unit
) {
    var idLocal by remember { mutableStateOf(partido?.idEquipoLocal) }
    var idVisita by remember { mutableStateOf(partido?.idEquipoVisita) }
    var estadio by remember { mutableStateOf(partido?.estadio ?: "") }
    var fechaStr by remember { mutableStateOf(partido?.fecha ?: "") }
    var golesLocal by remember { mutableStateOf(partido?.golesLocal?.toString() ?: "0") }
    var golesVisita by remember { mutableStateOf(partido?.golesVisita?.toString() ?: "0") }

    var expandedLocal by remember { mutableStateOf(false) }
    var expandedVisita by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    // Sincronizar estados si el partido cambia
    LaunchedEffect(partido) {
        partido?.let {
            idLocal = it.idEquipoLocal
            idVisita = it.idEquipoVisita
            estadio = it.estadio ?: ""
            fechaStr = it.fecha ?: ""
            golesLocal = it.golesLocal?.toString() ?: "0"
            golesVisita = it.golesVisita?.toString() ?: "0"
            errorMsg = null
        }
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            fechaStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    fun validar() {
        if (idLocal == null || idVisita == null || estadio.isBlank() || fechaStr.isBlank()) {
            errorMsg = "POR FAVOR COMPLETA TODOS LOS CAMPOS."
            return
        }
        if (idLocal == idVisita) {
            errorMsg = "UN EQUIPO NO PUEDE JUGAR CONTRA SÍ MISMO."
            return
        }
        errorMsg = null
        mostrarConfirmacion = true
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            containerColor = FichaFondo,
            shape = RoundedCornerShape(20.dp),
            title = { Text("¿GUARDAR PARTIDO?", color = Blanco, fontWeight = FontWeight.Black, fontSize = 16.sp) },
            text = { Text("Se registrará el encuentro entre los equipos seleccionados.", color = TextoSec, fontSize = 14.sp) },
            confirmButton = {
                TextButton(onClick = {
                    val p = Partido(
                        idPartido = partido?.idPartido,
                        fecha = fechaStr,
                        estadio = estadio,
                        idEquipoLocal = idLocal,
                        idEquipoVisita = idVisita,
                        golesLocal = golesLocal.toIntOrNull() ?: 0,
                        golesVisita = golesVisita.toIntOrNull() ?: 0
                    )
                    onGuardarClick(p)
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
                    text = if (!isEditMode) "NUEVO ENCUENTRO" else "EDITAR ENCUENTRO",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
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

            Text("EQUIPOS DEL ENCUENTRO", color = TextoSec, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)

            if (errorMsg != null || mensajeExterno != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (errorMsg ?: mensajeExterno ?: "").uppercase(),
                    color = ErrorRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector Local
            ExposedDropdownMenuBox(expanded = expandedLocal, onExpandedChange = { expandedLocal = !expandedLocal }) {
                OutlinedTextField(
                    value = (equipos.find { it.idEquipo == idLocal }?.nombre ?: "SELECCIONAR LOCAL").uppercase(),
                    onValueChange = {}, readOnly = true,
                    label = { Text("EQUIPO LOCAL", fontSize = 10.sp, fontWeight = FontWeight.Black) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocal) },
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
                ExposedDropdownMenu(expanded = expandedLocal, onDismissRequest = { expandedLocal = false }, modifier = Modifier.background(FichaFondo)) {
                    equipos.forEach { eq ->
                        DropdownMenuItem(text = { Text(eq.nombre.uppercase(), color = Blanco, fontWeight = FontWeight.Bold) }, onClick = {
                            if (eq.idEquipo == idVisita) {
                                errorMsg = "NO PUEDES SELECCIONAR EL MISMO EQUIPO."
                            } else {
                                idLocal = eq.idEquipo
                                errorMsg = null
                            }
                            expandedLocal = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector Visitante
            ExposedDropdownMenuBox(expanded = expandedVisita, onExpandedChange = { expandedVisita = !expandedVisita }) {
                OutlinedTextField(
                    value = (equipos.find { it.idEquipo == idVisita }?.nombre ?: "SELECCIONAR VISITANTE").uppercase(),
                    onValueChange = {}, readOnly = true,
                    label = { Text("EQUIPO VISITANTE", fontSize = 10.sp, fontWeight = FontWeight.Black) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVisita) },
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
                ExposedDropdownMenu(expanded = expandedVisita, onDismissRequest = { expandedVisita = false }, modifier = Modifier.background(FichaFondo)) {
                    equipos.forEach { eq ->
                        DropdownMenuItem(text = { Text(eq.nombre.uppercase(), color = Blanco, fontWeight = FontWeight.Bold) }, onClick = {
                            if (eq.idEquipo == idLocal) {
                                errorMsg = "NO PUEDES SELECCIONAR EL MISMO EQUIPO."
                            } else {
                                idVisita = eq.idEquipo
                                errorMsg = null
                            }
                            expandedVisita = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("DETALLES DEL LUGAR Y FECHA", color = TextoSec, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(16.dp))

            FormTextField(
                label = "ESTADIO / DIRECCIÓN",
                value = estadio,
                onValueChange = { newVal: String -> estadio = newVal },
                icon = Icons.Default.LocationOn
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() }) {
                OutlinedTextField(
                    value = fechaStr,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("FECHA DEL PARTIDO", fontSize = 10.sp, fontWeight = FontWeight.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = Verde, modifier = Modifier.size(20.dp)) },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = FichaFondo,
                        disabledBorderColor = Color.Transparent,
                        disabledTextColor = Blanco,
                        disabledLabelColor = TextoSec
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("MARCADOR FINAL", color = TextoSec, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FormTextField(
                    label = "GOLES LOC.",
                    value = golesLocal,
                    onValueChange = { newVal: String -> if (newVal.all { c: Char -> c.isDigit() }) golesLocal = newVal },
                    icon = Icons.Default.Add,
                    modifier = Modifier.weight(1f)
                )
                FormTextField(
                    label = "GOLES VIS.",
                    value = golesVisita,
                    onValueChange = { newVal: String -> if (newVal.all { c: Char -> c.isDigit() }) golesVisita = newVal },
                    icon = Icons.Default.Add,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { validar() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verde),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("CONFIRMAR PARTIDO", fontWeight = FontWeight.Black, color = Blanco, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("CANCELAR", color = TextoSec, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

