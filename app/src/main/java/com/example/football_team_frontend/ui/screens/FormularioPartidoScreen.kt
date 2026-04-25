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

    // Sincronizar estados si el partido cambia (ej. carga asíncrona) o al entrar en modo edición
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (!isEditMode) "NUEVO ENCUENTRO" else "EDITAR ENCUENTRO", color = Blanco, fontSize = 18.sp, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.background(Color.White.copy(0.1f), CircleShape).size(36.dp)
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = Blanco, modifier = Modifier.size(16.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = VerdeOscuro)
            )
        },
        containerColor = VerdeOscuro
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (errorMsg != null || mensajeExterno != null) {
                Text(
                    text = errorMsg ?: mensajeExterno ?: "",
                    color = Color(0xFFFF4D4D),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("EQUIPOS", color = Verde, fontSize = 12.sp, fontWeight = FontWeight.Black)

            // Selector Local
            ExposedDropdownMenuBox(expanded = expandedLocal, onExpandedChange = { expandedLocal = !expandedLocal }) {
                OutlinedTextField(
                    value = equipos.find { it.idEquipo == idLocal }?.nombre ?: "Seleccionar Local",
                    onValueChange = {}, readOnly = true,
                    label = { Text("Equipo Local", color = GrisClaro) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1F4A43), 
                        unfocusedContainerColor = Color(0xFF1F4A43), 
                        focusedTextColor = Blanco, 
                        unfocusedTextColor = Blanco,
                        disabledTextColor = Blanco,
                        disabledContainerColor = Color(0xFF1F4A43)
                    )
                )
                ExposedDropdownMenu(expanded = expandedLocal, onDismissRequest = { expandedLocal = false }, modifier = Modifier.background(Color(0xFF1F4A43))) {
                    equipos.forEach { eq ->
                        DropdownMenuItem(text = { Text(eq.nombre, color = Blanco) }, onClick = { 
                            if (eq.idEquipo == idVisita) {
                                errorMsg = "No puedes seleccionar el mismo equipo."
                            } else {
                                idLocal = eq.idEquipo
                                errorMsg = null
                            }
                            expandedLocal = false 
                        })
                    }
                }
            }

            // Selector Visitante
            ExposedDropdownMenuBox(expanded = expandedVisita, onExpandedChange = { expandedVisita = !expandedVisita }) {
                OutlinedTextField(
                    value = equipos.find { it.idEquipo == idVisita }?.nombre ?: "Seleccionar Visitante",
                    onValueChange = {}, readOnly = true,
                    label = { Text("Equipo Visitante", color = GrisClaro) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1F4A43), 
                        unfocusedContainerColor = Color(0xFF1F4A43), 
                        focusedTextColor = Blanco, 
                        unfocusedTextColor = Blanco,
                        disabledTextColor = Blanco,
                        disabledContainerColor = Color(0xFF1F4A43)
                    )
                )
                ExposedDropdownMenu(expanded = expandedVisita, onDismissRequest = { expandedVisita = false }, modifier = Modifier.background(Color(0xFF1F4A43))) {
                    equipos.forEach { eq ->
                        DropdownMenuItem(text = { Text(eq.nombre, color = Blanco) }, onClick = { 
                            if (eq.idEquipo == idLocal) {
                                errorMsg = "No puedes seleccionar el mismo equipo."
                            } else {
                                idVisita = eq.idEquipo
                                errorMsg = null
                            }
                            expandedVisita = false 
                        })
                    }
                }
            }

            Text("DETALLES", color = Verde, fontSize = 12.sp, fontWeight = FontWeight.Black)

            OutlinedTextField(
                value = estadio,
                onValueChange = { estadio = it },
                label = { Text("Estadio / Dirección", color = GrisClaro) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Verde) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFF1F4A43), unfocusedContainerColor = Color(0xFF1F4A43), focusedTextColor = Blanco, unfocusedTextColor = Blanco)
            )

            OutlinedTextField(
                value = fechaStr,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha", color = GrisClaro) },
                modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() },
                enabled = false,
                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Verde) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(disabledContainerColor = Color(0xFF1F4A43), disabledTextColor = Blanco, disabledBorderColor = Color.Transparent, disabledLabelColor = GrisClaro)
            )

            Text("MARCADOR", color = Verde, fontSize = 12.sp, fontWeight = FontWeight.Black)

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = golesLocal,
                    onValueChange = { golesLocal = it },
                    label = { Text("Goles Loc.", color = GrisClaro) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFF1F4A43), unfocusedContainerColor = Color(0xFF1F4A43), focusedTextColor = Blanco, unfocusedTextColor = Blanco)
                )
                OutlinedTextField(
                    value = golesVisita,
                    onValueChange = { golesVisita = it },
                    label = { Text("Goles Vis.", color = GrisClaro) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFF1F4A43), unfocusedContainerColor = Color(0xFF1F4A43), focusedTextColor = Blanco, unfocusedTextColor = Blanco)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (idLocal == null || idVisita == null || estadio.isBlank() || fechaStr.isBlank()) {
                        errorMsg = "Por favor completa todos los campos."
                        return@Button
                    }
                    if (idLocal == idVisita) {
                        errorMsg = "Un equipo no puede jugar contra sí mismo."
                        return@Button
                    }
                    
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
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verde),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("CONFIRMAR PARTIDO", fontWeight = FontWeight.ExtraBold, color = Blanco)
            }
        }
    }
}
