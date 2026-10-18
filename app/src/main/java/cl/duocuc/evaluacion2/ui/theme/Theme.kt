package cl.duocuc.evaluacion2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AtenasTealLight,
    secondary = AtenasSlateLight,
    tertiary = AtenasAmberLight,
    background = AtenasBackgroundDark,
    surface = AtenasBackgroundDark
)

private val LightColorScheme = lightColorScheme(
    primary = AtenasTeal,
    secondary = AtenasSlate,
    tertiary = AtenasAmber,
    background = AtenasBackgroundLight,
    surface = AtenasBackgroundLight
)

@Composable
fun AtenasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Color dinámico desactivado para mantener la identidad visual corporativa
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
