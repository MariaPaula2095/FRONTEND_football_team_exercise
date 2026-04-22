package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.R
import com.example.football_team_frontend.ui.theme.Blanco
import com.example.football_team_frontend.ui.theme.CardColor
import com.example.football_team_frontend.ui.theme.GrisClaro
import com.example.football_team_frontend.ui.theme.Verde
import com.example.football_team_frontend.ui.theme.VerdeOscuro

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    onEquiposClick: () -> Unit = {},
    onJugadoresClick: () -> Unit = {},
    onEntrenadoresClick: () -> Unit = {},
    onPartidosClick: () -> Unit = {},
    onEstadisticasClick: () -> Unit = {},
    onConsultasClick: () -> Unit = {}
) {
    // Fondo general oscuro de toda la pantalla
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF031B17))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header con imagen de estadio SOLO arriba
            HeaderSection()

            // Zona de cards
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(VerdeOscuro)
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeCard(
                        title = "Equipos",
                        subtitle = "Gestiona tus\nequipos",
                        iconRes = R.drawable.ic_equipos,
                        iconTint = Color(0xFF4BE37A),
                        onClick = onEquiposClick,
                        modifier = Modifier.weight(1f)
                    )

                    HomeCard(
                        title = "Jugadores",
                        subtitle = "Administra los\njugadores",
                        iconRes = R.drawable.ic_jugador,
                        iconTint = Color(0xFF37E0C4),
                        onClick = onJugadoresClick,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeCard(
                        title = "Entrenadores",
                        subtitle = "Gestiona el cuerpo\ntécnico",
                        iconRes = R.drawable.ic_entrenador,
                        iconTint = Color(0xFFFFC83D),
                        onClick = onEntrenadoresClick,
                        modifier = Modifier.weight(1f)
                    )

                    HomeCard(
                        title = "Partidos",
                        subtitle = "Calendario y\nresultados",
                        iconRes = R.drawable.ic_partidos,
                        iconTint = Color(0xFFB8C8FF),
                        onClick = onPartidosClick,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                StatsCard(
                    title = "Estadísticas",
                    subtitle = "Rendimiento y\nestadísticas",
                    iconTint = Color(0xFFB77CFF),
                    onClick = onEstadisticasClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                ReportsCard(
                    title = "Consultas / Reportes",
                    subtitle = "Consultas nativas y reportes avanzados",
                    onClick = onConsultasClick
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    // Sección superior con imagen real y overlay oscuro
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_estadio),
            contentDescription = "Estadio",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Oscurece la imagen para que el texto resalte
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 28.dp, end = 20.dp, top = 70.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "FOOTBALL",
                color = Blanco,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "MANAGER",
                color = Verde,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Gestiona tu equipo de fútbol",
                color = GrisClaro,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun HomeCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(155.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono centrado y con color sólido
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(46.dp),
                colorFilter = ColorFilter.tint(iconTint)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                color = Blanco,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                color = GrisClaro,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StatsCard(
    title: String,
    subtitle: String,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Si luego agregas ic_estadisticas.png, cámbialo aquí
            Image(
                painter = painterResource(id = R.drawable.ic_partidos),
                contentDescription = title,
                modifier = Modifier.size(44.dp),
                colorFilter = ColorFilter.tint(iconTint)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    color = Blanco,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    color = GrisClaro,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ReportsCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(18.dp),
                clip = false
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF066B35)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.QueryStats,
                contentDescription = title,
                tint = Color(0xFFC9FFD5),
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Blanco,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    color = Color(0xFFE0F7E8),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ir",
                tint = Blanco,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}