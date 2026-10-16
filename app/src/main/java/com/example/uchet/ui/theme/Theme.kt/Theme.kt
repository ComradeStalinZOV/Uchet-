package com.example.uchet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF7C4DFF),
    secondary = Color(0xFF00E5FF),
    background = Color(0xFF0E0E12),
    surface = Color(0xFF1A1A22),
    onPrimary = Color.White,
    onBackground = Color(0xFFE6E6E6)
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFF7F7FB),
    surface = Color.White
)

@Composable
fun UchetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        content = content
    )
}
