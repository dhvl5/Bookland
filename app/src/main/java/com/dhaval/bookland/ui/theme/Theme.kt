package com.dhaval.bookland.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = BrunswickGreen,
    primaryVariant = Purple700,
    onPrimary = OceanGreen2,
    secondary = OceanGreen2,
    secondaryVariant = Color.Gray,
    onSecondary = Color.White,
    background = Color.Black,
    onBackground = Color.Black,
    surface = Color.Black,
    onSurface = Color.Black,
)

private val LightColorPalette = lightColors(
    primary = BrunswickGreen,
    primaryVariant = Purple700,
    onPrimary = IlluminatingEmerald,
    secondary = IlluminatingEmerald,
    secondaryVariant = Color.Gray,
    onSecondary = Color.Black,
    background = Color.White,
    onBackground = Color.White,
    surface = Color.White,
    onSurface = Color.White,
)

@Composable
fun BooklandTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}