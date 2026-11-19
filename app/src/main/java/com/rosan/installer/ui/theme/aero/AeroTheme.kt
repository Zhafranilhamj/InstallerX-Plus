// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.rosan.installer.R

internal val LocalFrutigerAero = staticCompositionLocalOf { false }

private val OceanInk = Color(0xFF073653)
private val WaterInk = Color(0xFF184B62)

internal val AeroColors = lightColorScheme(
    primary = Color(0xFF005988),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA3E9FF),
    onPrimaryContainer = OceanInk,
    secondary = Color(0xFF006357),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFAEF2DC),
    onSecondaryContainer = Color(0xFF063B34),
    tertiary = Color(0xFF42601C),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFDBF3AD),
    onTertiaryContainer = Color(0xFF24390E),
    background = Color(0xFFE4F8FF),
    onBackground = OceanInk,
    surface = Color(0xFFEDFAFF),
    onSurface = OceanInk,
    surfaceVariant = Color(0xFFD9F4FA),
    onSurfaceVariant = WaterInk,
    surfaceDim = Color(0xFFBEE6F2),
    surfaceBright = Color(0xFFF5FDFF),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF2FBFF),
    surfaceContainer = Color(0xFFE5F7FF),
    surfaceContainerHigh = Color(0xFFDCF5FF),
    surfaceContainerHighest = Color(0xFFC6EEFA),
    outline = Color(0xFF417787),
    outlineVariant = Color(0xFFA4CDDA),
    inverseSurface = Color(0xFF153F55),
    inverseOnSurface = Color(0xFFECFAFF),
    inversePrimary = Color(0xFF9AE2FF),
)

internal val AeroDarkColors = darkColorScheme(
    primary = Color(0xFF88DFFF),
    onPrimary = OceanInk,
    primaryContainer = Color(0xFF155371),
    onPrimaryContainer = Color(0xFFC7EEFF),
    secondary = Color(0xFF87DFC9),
    onSecondary = Color(0xFF00382F),
    secondaryContainer = Color(0xFF105247),
    onSecondaryContainer = Color(0xFFADF4DF),
    tertiary = Color(0xFFC2E699),
    onTertiary = Color(0xFF24390E),
    background = Color(0xFF061722),
    onBackground = Color(0xFFE0F6FF),
    surface = Color(0xFF0B212F),
    onSurface = Color(0xFFE0F6FF),
    surfaceVariant = Color(0xFF213E4D),
    onSurfaceVariant = Color(0xFFBCD7E2),
    surfaceContainerLowest = Color(0xFF031018),
    surfaceContainerLow = Color(0xFF0B202D),
    surfaceContainer = Color(0xFF102A3A),
    surfaceContainerHigh = Color(0xFF193747),
    surfaceContainerHighest = Color(0xFF234554),
)

internal val AeroShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(34.dp),
)

private val AeroFont = FontFamily(Font(R.font.aero_nunito))

private fun TextStyle.aeroText(reflection: Boolean = false) = copy(
    fontFamily = AeroFont,
    shadow = if (reflection) Shadow(Color.White.copy(alpha = 0.65f), Offset(0f, 1f), 2f) else null,
)

internal val AeroTypography = Typography().run {
    copy(
        displayLarge = displayLarge.aeroText(reflection = true),
        displayMedium = displayMedium.aeroText(reflection = true),
        displaySmall = displaySmall.aeroText(reflection = true),
        headlineLarge = headlineLarge.aeroText(reflection = true),
        headlineMedium = headlineMedium.aeroText(reflection = true),
        headlineSmall = headlineSmall.aeroText(reflection = true),
        titleLarge = titleLarge.aeroText(reflection = true),
        titleMedium = titleMedium.aeroText(),
        titleSmall = titleSmall.aeroText(),
        bodyLarge = bodyLarge.aeroText(),
        bodyMedium = bodyMedium.aeroText(),
        bodySmall = bodySmall.aeroText(),
        labelLarge = labelLarge.aeroText(),
        labelMedium = labelMedium.aeroText(),
        labelSmall = labelSmall.aeroText(),
    )
}
