package com.dhaval.bookland.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = PrimaryDark,
    primaryVariant = PrimaryVariantDark,
    onPrimary = OnPrimaryDark,
    secondary = SecondaryDark,
    secondaryVariant = SecondaryVariantDark,
    onSecondary = OnSecondaryDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
)

private val LightColorPalette = lightColors(
    primary = PrimaryLight,
    primaryVariant = PrimaryVariantLight,
    onPrimary = OnPrimaryLight,
    secondary = SecondaryLight,
    secondaryVariant = SecondaryVariantLight,
    onSecondary = OnSecondaryLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
)

@Composable
fun BooklandTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()

    val colors: Colors

    if (darkTheme) {
        colors = DarkColorPalette
        systemUiController.setStatusBarColor(Color(0xFF252525))
        systemUiController.setNavigationBarColor(Color(0xFF252525))
    } else {
        colors = LightColorPalette
        systemUiController.setStatusBarColor(Color.White)
        systemUiController.setNavigationBarColor(Color.White)
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}