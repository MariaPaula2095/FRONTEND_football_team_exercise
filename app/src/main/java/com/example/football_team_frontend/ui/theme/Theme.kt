package com.example.football_team_frontend.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Verde,
    secondary = VerdeOscuro,
    background = Negro,
    surface = CardColor,
    onPrimary = Blanco,
    onBackground = Blanco,
    onSurface = Blanco
)

@Composable
fun FootballTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}