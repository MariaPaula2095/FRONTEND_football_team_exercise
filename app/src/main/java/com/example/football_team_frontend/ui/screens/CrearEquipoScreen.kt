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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.model.Equipo
import com.example.football_team_frontend.ui.components.FormTextField
import com.example.football_team_frontend.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioEquipoScreen(
    equipo: Equipo? = null,
    onBackClick: () -> Unit,
    onGuardarClick: (Equipo) -> Unit
) {
    var nombre              by remember { mutableStateOf(equipo?.nombre ?: "") }
    var ciudad              by remember { mutableStateOf(equipo?.ciudad ?: "") }
    var fechaFundacion      by remember { mutableStateOf(
        equipo?.fundacion?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it) } ?: ""
    )}
    var mostrarDatePicker   by remember { mutableStateOf(false) }
    var errorTexto          by remember { mutableStateOf<String?>(null) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    fun validar() {
        if (nombre.isBlank() || ciudad.isBlank() || fechaFundacion.isBlank()) {
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
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    formatter.timeZone = TimeZone.getTimeZone("UTC")
                    val fechaDate = formatter.parse(fechaFundacion) ?: Date()

                    onGuardarClick(
                        Equipo(
                            idEquipo  = equipo?.idEquipo ?: 0L,
                            nombre    = nombre,
                            ciudad    = ciudad,
                            fundacion = fechaDate
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

    // ── DatePicker ────────────────────────────────────────────────────────
    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        fechaFundacion = formatter.format(Date(millis))
                    }
                    mostrarDatePicker = false
                }) {
                    Text("ACEPTAR", color = Verde, fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) {
                    Text("CANCELAR", color = TextoSec)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
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
                    text = if (equipo == null) "NUEVO EQUIPO" else "EDITAR EQUIPO",
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

            // Avatar dinámico
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
                "INFORMACIÓN BÁSICA",
                color = TextoSec,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (errorTexto != null) {
                Text(
                    errorTexto!!,
                    color    = ErrorRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // ── Campos de entrada ─────────────────────────────────────────
            FormTextField(
                label         = "NOMBRE DEL EQUIPO",
                value         = nombre,
                onValueChange = { nombre = it },
                icon          = Icons.Default.Shield
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormTextField(
                label         = "CIUDAD SEDE",
                value         = ciudad,
                onValueChange = { ciudad = it },
                icon          = Icons.Default.LocationOn
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de fecha
            Box(modifier = Modifier.fillMaxWidth().clickable { mostrarDatePicker = true }) {
                OutlinedTextField(
                    value         = fechaFundacion,
                    onValueChange = {},
                    readOnly      = true,
                    enabled       = false,
                    label         = { Text("FECHA DE FUNDACIÓN", fontSize = 10.sp, fontWeight = FontWeight.Black) },
                    leadingIcon   = { Icon(Icons.Default.CalendarToday, null, tint = Verde, modifier = Modifier.size(20.dp)) },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(16.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = FichaFondo,
                        disabledBorderColor    = Color.Transparent,
                        disabledTextColor      = Color.White,
                        disabledLabelColor     = TextoSec,
                        focusedBorderColor     = Verde,
                        unfocusedBorderColor   = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Botón de acción principal
            Button(
                onClick  = { validar() },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Verde)
            ) {
                Icon(if(equipo==null) Icons.Default.Add else Icons.Default.Save, null)
                Spacer(Modifier.width(8.dp))
                Text(
                    text       = if (equipo == null) "GUARDAR EQUIPO" else "ACTUALIZAR EQUIPO",
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
