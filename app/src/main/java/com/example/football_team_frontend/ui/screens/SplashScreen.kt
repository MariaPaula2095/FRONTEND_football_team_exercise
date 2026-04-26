package com.example.football_team_frontend.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.football_team_frontend.R
import com.example.football_team_frontend.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {

    // ── Animaciones ───────────────────────────────────────────────────────
    val logoScale = remember { Animatable(0.3f) }
    val logoAlpha = remember { Animatable(0f) }
    val glowScale = remember { Animatable(0.5f) }
    val glowAlpha = remember { Animatable(0f) }

    val titleOffsetY  = remember { Animatable(60f) }
    val titleAlpha    = remember { Animatable(0f) }

    val sloganOffsetY = remember { Animatable(40f) }
    val sloganAlpha   = remember { Animatable(0f) }

    val lineScale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // 1. Glow aparece primero
        launch {
            glowAlpha.animateTo(0.6f, tween(600, easing = EaseOut))
            glowScale.animateTo(1.2f, tween(800, easing = EaseOutBack))
        }

        // 2. Logo entra con bounce
        delay(200)
        launch { logoAlpha.animateTo(1f, tween(400)) }
        logoScale.animateTo(
            targetValue   = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness    = Spring.StiffnessLow
            )
        )

        // 3. Línea decorativa
        delay(100)
        lineScale.animateTo(1f, tween(500, easing = EaseOutCubic))

        // 4. Título sube desde abajo
        delay(100)
        launch { titleAlpha.animateTo(1f, tween(500)) }
        titleOffsetY.animateTo(0f, tween(600, easing = EaseOutCubic))

        // 5. Slogan sube con delay
        delay(150)
        launch { sloganAlpha.animateTo(1f, tween(500)) }
        sloganOffsetY.animateTo(0f, tween(600, easing = EaseOutCubic))

        // 6. Navega
        delay(1200)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0D3524),
                        Color(0xFF061A10),
                        Color(0xFF020D08)
                    ),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // ── Círculos decorativos de fondo ─────────────────────────────────
        Box(
            modifier = Modifier
                .size(400.dp)
                .offset(x = (-80).dp, y = (-120).dp)
                .alpha(0.04f)
                .background(Verde, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = 100.dp, y = 180.dp)
                .alpha(0.03f)
                .background(Verde, CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Glow + Logo ───────────────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier         = Modifier.size(260.dp)
            ) {
                // Glow externo
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .scale(glowScale.value)
                        .alpha(glowAlpha.value)
                        .blur(30.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Verde.copy(alpha = 0.6f),
                                    Verde.copy(alpha = 0.0f)
                                )
                            ),
                            CircleShape
                        )
                )

                // Glow interno
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .scale(glowScale.value)
                        .alpha(glowAlpha.value * 0.7f)
                        .blur(20.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Verde.copy(alpha = 0.8f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )

                // Logo con bordes redondeados
                Image(
                    painter            = painterResource(id = R.drawable.ic_football_manager),
                    contentDescription = "Football Manager",
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(190.dp)
                        .scale(logoScale.value)
                        .alpha(logoAlpha.value)
                        .clip(RoundedCornerShape(32.dp))
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Línea decorativa ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .scale(scaleX = lineScale.value, scaleY = 1f)
                    .height(1.5.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Verde.copy(alpha = 0.8f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Título ────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .offset(y = titleOffsetY.value.dp)
                    .alpha(titleAlpha.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text          = "FOOTBALL",
                    color         = Color.White,
                    fontSize      = 34.sp,
                    fontWeight    = FontWeight.ExtraBold,
                    letterSpacing = 6.sp,
                    textAlign     = TextAlign.Center
                )
                Text(
                    text          = "MANAGER",
                    color         = Verde,
                    fontSize      = 38.sp,
                    fontWeight    = FontWeight.ExtraBold,
                    letterSpacing = 6.sp,
                    textAlign     = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Slogan ────────────────────────────────────────────────────
            Text(
                text          = "Tu equipo, tu estrategia, tu victoria.",
                color         = Color(0xFF7FB99A),
                fontSize      = 13.sp,
                fontWeight    = FontWeight.Medium,
                textAlign     = TextAlign.Center,
                letterSpacing = 0.5.sp,
                modifier      = Modifier
                    .offset(y = sloganOffsetY.value.dp)
                    .alpha(sloganAlpha.value)
                    .padding(horizontal = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF020D08, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onSplashFinished = {})
}