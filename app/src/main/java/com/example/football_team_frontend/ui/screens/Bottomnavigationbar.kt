package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.ui.theme.FondoOscuro
import com.example.football_team_frontend.ui.theme.TextoSec
import com.example.football_team_frontend.ui.theme.Verde

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onInicioClick: () -> Unit,
    onJugadoresClick: () -> Unit,
    onEquiposClick: () -> Unit,
    onEntrenadoresClick: () -> Unit
) {
    val items = listOf(
        NavItem("inicio",       "Inicio",       Icons.Default.Home,         onInicioClick),
        NavItem("jugadores",    "Jugadores",    Icons.Default.Person,       onJugadoresClick),
        NavItem("equipos",      "Equipos",      Icons.Default.Shield,       onEquiposClick),
        NavItem("entrenadores", "Entrenadores", Icons.Default.SportsSoccer, onEntrenadoresClick),
    )

    NavigationBar(
        containerColor = Color(0xFF0D2B1C),
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route ||
                    currentRoute?.startsWith(item.route + "/") == true

            NavigationBarItem(
                selected = selected,
                onClick  = item.onClick,
                icon = {
                    Icon(
                        imageVector        = item.icon,
                        contentDescription = item.label,
                        modifier           = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text       = item.label,
                        fontSize   = 8.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = Verde,
                    selectedTextColor   = Verde,
                    unselectedIconColor = Color(0xFF7FB99A),
                    unselectedTextColor = Color(0xFF7FB99A),
                    indicatorColor      = Verde.copy(alpha = 0.15f)
                )
            )
        }
    }
}

private data class NavItem(
    val route:  String,
    val label:  String,
    val icon:   androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit
)