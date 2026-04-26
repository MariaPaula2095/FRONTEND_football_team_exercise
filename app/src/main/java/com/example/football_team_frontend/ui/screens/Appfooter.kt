package com.example.football_team_frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.ui.theme.Verde

@Composable
fun AppFooter(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Divider(
            color     = Verde.copy(alpha = 0.25f),
            thickness = 1.dp,
            modifier  = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text       = "🎓 Ingeniería de Software I",
            color      = Verde,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center
        )

        Text(
            text      = "Universidad de Cundinamarca · 2026",
            color     = Color(0xFF7FB99A),
            fontSize  = 11.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text      = "Desarrollado por:",
            color     = Color(0xFF7FB99A),
            fontSize  = 11.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text       = "Mónica Poveda & Paula Ramírez",
            color      = Color.White,
            fontSize   = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign  = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D2B1C)
@Composable
fun AppFooterPreview() {
    AppFooter()
}