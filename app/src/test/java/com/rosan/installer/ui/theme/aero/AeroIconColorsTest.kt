// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class AeroIconColorsTest {
    @Test
    fun neutralThemeInkBecomesAquaInBothAppearanceModes() {
        for (scheme in listOf(AeroColors, AeroDarkColors)) {
            for (ink in listOf(scheme.onSurface, scheme.onSurfaceVariant, scheme.onBackground, scheme.onPrimaryContainer)) {
                assertEquals(Color(0xFF008AB8), aeroIconPigment(ink, scheme))
            }
        }
    }

    @Test
    fun explicitSafetyAndSelectionColorsAreNotReplacedWithAqua() {
        for (scheme in listOf(AeroColors, AeroDarkColors)) {
            for (ink in listOf(scheme.error, Color(0xFFB26A00), Color(0xFF158345), scheme.primary, Color.White)) {
                assertEquals(ink, aeroIconPigment(ink, scheme))
            }
        }
    }

    @Test
    fun disabledOpacityIsAppliedOnceToTheFinishedIconNotBakedIntoThePigment() {
        val warning = Color(0xFFB26A00)
        assertEquals(warning, aeroIconPigment(warning.copy(alpha = 0.38f), AeroColors))
        assertEquals(Color(0xFF008AB8), aeroIconPigment(AeroColors.onSurface.copy(alpha = 0.38f), AeroColors))
    }

    @Test
    fun untintedArtworkKeepsItsOwnColors() {
        assertEquals(Color.Unspecified, aeroIconPigment(Color.Unspecified, AeroColors))
        assertEquals(Color.Unspecified, aeroIconPigment(Color.Unspecified, AeroDarkColors))
    }
}
