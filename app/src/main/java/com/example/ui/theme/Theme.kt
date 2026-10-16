package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ShikhiKidsColorScheme = lightColorScheme(
    primary = KidAmberOrange,
    onPrimary = Color.White,
    primaryContainer = KidSunnyYellow.copy(alpha = 0.35f),
    onPrimaryContainer = KidDeepBlue,
    secondary = KidSkyBlue,
    onSecondary = Color.White,
    secondaryContainer = KidSkyBlue.copy(alpha = 0.2f),
    onSecondaryContainer = KidDeepBlue,
    tertiary = KidMintGreen,
    onTertiary = Color.White,
    tertiaryContainer = KidMintGreen.copy(alpha = 0.2f),
    onTertiaryContainer = KidDeepBlue,
    background = KidCreamBackground,
    onBackground = KidTextPrimary,
    surface = KidCardSurface,
    onSurface = KidTextPrimary,
    surfaceVariant = KidCardSurfaceVariant,
    onSurfaceVariant = KidTextSecondary,
    error = KidCoralRed,
    onError = Color.White
)

val KidShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun ShikhiKidsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ShikhiKidsColorScheme,
        typography = Typography,
        shapes = KidShapes,
        content = content
    )
}
