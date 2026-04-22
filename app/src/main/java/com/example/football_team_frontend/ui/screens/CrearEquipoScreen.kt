package com.example.football_team_frontend.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.football_team_frontend.ui.theme.Blanco
import com.example.football_team_frontend.ui.theme.CardColor
import com.example.football_team_frontend.ui.theme.GrisClaro
import com.example.football_team_frontend.ui.theme.Verde
import com.example.football_team_frontend.ui.theme.VerdeOscuro

@Composable

fun CrearEquipoScreen(
    modifier: Modifier = Modifier,
    nombre: String,
    ciudad: String,
    fundacion: String,
    onNombreChange: (String) -> Unit,
    onCiudadChange: (String) -> Unit,
    onFundacionChange: (String) -> Unit,
    onBackClick: () -> Unit = {},
    onGuardarClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VerdeOscuro)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Encabezado superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = CardColor,
                            shape = CircleShape
                        )
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Volver",
                        tint = Blanco,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Nuevo Equipo",
                    color = Blanco,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card decorativa superior
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1F4A43)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                color = Verde.copy(alpha = 0.18f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Equipo",
                            tint = Verde,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Crear nuevo equipo",
                        color = Blanco,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campo nombre
            FormField(
                value = nombre,
                onValueChange = onNombreChange,
                label = "Nombre del equipo",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Nombre",
                        tint = Verde
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo ciudad
            FormField(
                value = ciudad,
                onValueChange = onCiudadChange,
                label = "Ciudad",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ciudad",
                        tint = Verde
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo fundación
            FormField(
                value = fundacion,
                onValueChange = onFundacionChange,
                label = "Fundación",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Fundación",
                        tint = Verde
                    )
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Botón guardar
            Button(
                onClick = onGuardarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Verde,
                    contentColor = Blanco
                )
            ) {
                Text(
                    text = "Guardar equipo",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                text = label,
                color = GrisClaro
            )
        },
        leadingIcon = leadingIcon,
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF1F4A43),
            unfocusedContainerColor = Color(0xFF1F4A43),
            focusedBorderColor = Verde,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = Blanco,
            focusedTextColor = Blanco,
            unfocusedTextColor = Blanco,
            focusedLabelColor = Verde,
            unfocusedLabelColor = GrisClaro
        )
    )
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CrearEquipoScreenPreview() {

    CrearEquipoScreen(
        nombre = "Barcelona FC",
        ciudad = "Barcelona",
        fundacion = "1899",

        onNombreChange = {},
        onCiudadChange = {},
        onFundacionChange = {},

        onBackClick = {},
        onGuardarClick = {}
    )
}