package com.proyecto.petshopapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Colores claros
private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    secondary = PurpleGrey40,
    tertiary = Pink40

)

// Colores oscuros (estructura preparada para usar más adelante)
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80

)

@Composable
fun PetshopAppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) {
        DarkColorScheme //
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,  // Usa la fuente Poppins definida en Typography.kt
        content = content
    )
}
