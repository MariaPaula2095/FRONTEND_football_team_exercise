package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.ui.theme.Blanco
import com.example.football_team_frontend.ui.theme.CardColor
import com.example.football_team_frontend.ui.theme.GrisClaro
import com.example.football_team_frontend.ui.theme.Verde
import com.example.football_team_frontend.ui.theme.VerdeOscuro

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun InicioScreen(
    modifier: Modifier = Modifier,
    onEquiposClick: () -> Unit = {},
    onJugadoresClick: () -> Unit = {},
    onEntrenadoresClick: () -> Unit = {},
    onPartidosClick: () -> Unit = {},
    onEstadisticasClick: () -> Unit = {},
    onConsultasClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF041C17),
                        VerdeOscuro,
                        Color(0xFF03110E)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            HeroHeader()

            // Contenedor de tarjetas
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Fila 1: Equipos y Jugadores
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max), // Hace que ambas tarjetas midan lo mismo
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeCard(
                        title = "Equipos",
                        subtitle = "Gestiona tus equipos",
                        icon = Icons.Default.Shield,
                        iconTint = Color(0xFF4BE37A),
                        onClick = onEquiposClick,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )

                    HomeCard(
                        title = "Jugadores",
                        subtitle = "Administra los jugadores",
                        icon = Icons.Default.Person,
                        iconTint = Color(0xFF38D6B2),
                        onClick = onJugadoresClick,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }

                // Fila 2: Entrenadores y Partidos
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeCard(
                        title = "Entrenadores",
                        subtitle = "Gestiona el cuerpo técnico",
                        icon = Icons.Default.SportsSoccer,
                        iconTint = Color(0xFFFFC83D),
                        onClick = onEntrenadoresClick,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )

                    HomeCard(
                        title = "Partidos",
                        subtitle = "Calendario y resultados",
                        icon = Icons.Default.CalendarMonth,
                        iconTint = Color(0xFFB8D1FF),
                        onClick = onPartidosClick,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }

                // Tarjeta ancha: Estadísticas
                WideCard(
                    title = "Estadísticas",
                    subtitle = "Rendimiento y estadísticas detalladas",
                    icon = Icons.Default.Analytics,
                    iconTint = Color(0xFFB57CFF),
                    onClick = onEstadisticasClick
                )
            }

            AppFooter()

        }
    }
}

@Composable
private fun HeroHeader() {
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FOOTBALL",
            color = Blanco,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "MANAGER",
            color = Verde,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Gestiona tu equipo de fútbol",
            color = GrisClaro,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HomeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .heightIn(min = 160.dp) // Altura mínima, pero puede crecer si hay mucho texto
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                color = Blanco,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                color = GrisClaro,
                fontSize = 13.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )


        }
    }
}

@Composable
private fun WideCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp) // Dinámico
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(44.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = title,
                    color = Blanco,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = subtitle,
                    color = GrisClaro,
                    fontSize = 13.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}