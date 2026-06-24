package com.example.darkhumor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private fun createColorScheme(primary: Color, secondary: Color, tertiary: Color, isDark: Boolean) =
    if (isDark) {
        darkColorScheme(
            primary = primary,
            secondary = secondary,
            tertiary = tertiary,
            background = Color.Black,
            surface = Color(0xFF121212),
            surfaceVariant = Color(0xFF1E1E1E)
        )
    } else {
        lightColorScheme(
            primary = primary,
            secondary = secondary,
            tertiary = tertiary,
            background = White,
            surface = LightGray
        )
    }

val LocalAppTheme = staticCompositionLocalOf { AppTheme.PURPLE }

@Composable
fun DarkHumorTheme(
    appTheme: AppTheme = AppTheme.PURPLE,
    content: @Composable () -> Unit
) {
    val colorScheme = createColorScheme(
        primary = appTheme.primaryColor,
        secondary = appTheme.primaryColor.copy(alpha = 0.8f),
        tertiary = appTheme.primaryColor.copy(alpha = 0.6f),
        isDark = true
    )

    CompositionLocalProvider(LocalAppTheme provides appTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
