package eu.kanade.presentation.theme.colorscheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Liquid Glass (Midnight Dusk slot)
 * Navy background, purple accent, pink badges
 */
internal object MidnightDuskColorScheme : BaseColorScheme() {

    override val darkScheme = darkColorScheme(
        primary = Color(0xFFA78BFA),
        onPrimary = Color(0xFF1E1147),
        primaryContainer = Color(0xFF4C2FB3),
        onPrimaryContainer = Color(0xFFEDE7FF),
        inversePrimary = Color(0xFF7C3AED),
        secondary = Color(0xFFF472B6), // Unread badge
        onSecondary = Color(0xFF2A0A1C), // Unread badge text
        secondaryContainer = Color(0xFF3A2D6B), // Navigation bar selector pill
        onSecondaryContainer = Color(0xFFEDE7FF), // Navigation bar selector icon
        tertiary = Color(0xFF22D3EE), // Downloaded badge
        onTertiary = Color(0xFF04222A), // Downloaded badge text
        tertiaryContainer = Color(0xFF0E4F5C),
        onTertiaryContainer = Color(0xFFCFF7FF),
        background = Color(0xFF0B0B1F),
        onBackground = Color(0xFFEDEBFF),
        surface = Color(0xFF0B0B1F),
        onSurface = Color(0xFFEDEBFF),
        surfaceVariant = Color(0xFF1B1840), // Navigation bar background
        onSurfaceVariant = Color(0xFFB9B4DA),
        surfaceTint = Color(0xFFA78BFA),
        inverseSurface = Color(0xFFE6E1FF),
        inverseOnSurface = Color(0xFF1B1840),
        outline = Color(0xFF6E67A8),
        surfaceContainerLowest = Color(0xFF080817),
        surfaceContainerLow = Color(0xFF12122B),
        surfaceContainer = Color(0xFF1B1840), // Navigation bar background
        surfaceContainerHigh = Color(0xFF241F52),
        surfaceContainerHighest = Color(0xFF2E2863),
    )

    override val lightScheme = darkScheme
}
