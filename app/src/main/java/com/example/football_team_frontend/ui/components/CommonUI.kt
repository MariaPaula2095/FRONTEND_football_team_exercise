package com.example.football_team_frontend.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.ui.theme.*

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Black) },
        modifier = modifier.fillMaxWidth(),
        leadingIcon = { Icon(icon, null, tint = Verde, modifier = Modifier.size(20.dp)) },
        shape = RoundedCornerShape(16.dp),
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FichaFondo,
            unfocusedContainerColor = FichaFondo,
            focusedBorderColor = Verde,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = Blanco,
            unfocusedTextColor = Blanco,
            focusedLabelColor = Verde,
            unfocusedLabelColor = TextoSec,
            cursorColor = Verde
        )
    )
}

@Composable
fun MetricBox(
    modifier: Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = FichaFondo)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier         = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(label, color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                color      = Color.White,
                fontSize   = 18.sp,
                fontWeight = FontWeight.Black,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MetricBoxRow(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = FichaFondo)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier         = Modifier
                    .size(38.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(label, color = TextoSec, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(
                    value,
                    color      = Color.White,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ── Preview de los componentes que se usara como plantilla en jugadores,entrena,equipos.. ──────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF0D2B1C)
@Composable
fun CommonUIPreview() {
    Column(
        modifier = Modifier
            .background(SuperficieAlt)
            .padding(20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Componentes Reutilizables", color = Blanco, fontWeight = FontWeight.Black, fontSize = 20.sp)

        // FormTextField: Usado en formularios de creación/edición
        FormTextField(
            label = "NOMBRE DEL JUGADOR",
            value = "Lionel Messi",
            onValueChange = {},
            icon = Icons.Default.Person
        )

        // MetricBox: Usado para datos cortos en grilla (Dorsal, Nacionalidad)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricBox(
                modifier = Modifier.weight(1f),
                label = "DORSAL",
                value = "#10",
                icon = Icons.Default.Numbers,
                color = Verde
            )
            MetricBox(
                modifier = Modifier.weight(1f),
                label = "POSICIÓN",
                value = "DELANTERO",
                icon = Icons.Default.SportsSoccer,
                color = ErrorRed
            )
        }

        // MetricBoxRow: Usado para datos largos (Fechas, Clubes)
        MetricBoxRow(
            label = "FECHA DE NACIMIENTO",
            value = "1987-06-24",
            icon = Icons.Default.CalendarToday,
            color = WarningYellow
        )
        
        MetricBoxRow(
            label = "CLUB ACTUAL",
            value = "Inter Miami CF",
            icon = Icons.Default.Shield,
            color = InfoBlue
        )
    }
}


