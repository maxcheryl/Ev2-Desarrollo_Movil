package cl.duocuc.evaluacion2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MaxTealLight,
    secondary = MaxSlateLight,
    tertiary = MaxAmberLight,
    background = MaxBackgroundDark,
    surface = MaxBackgroundDark
)

private val LightColorScheme = lightColorScheme(
    primary = MaxTeal,
    secondary = MaxSlate,
    tertiary = MaxAmber,
    background = MaxBackgroundLight,
    surface = MaxBackgroundLight
)

/**
 * Tema corporativa de Agencia Max.
 * Se mantiene la paleta teal fija en claro y oscuro; no se usa
 * el color dinámico de Android 12 para preservar la identidad visual.
 */
@Composable
fun MaxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
