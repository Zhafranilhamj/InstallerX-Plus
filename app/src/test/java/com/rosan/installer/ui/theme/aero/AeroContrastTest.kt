// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.ui.graphics.luminance
import kotlin.test.Test
import kotlin.test.assertTrue

class AeroContrastTest {
    @Test
    fun coreColorPairsMeetNormalTextContrast() {
        val palette = AeroColors
        val pairs = listOf(
            palette.onBackground to palette.background,
            palette.onSurface to palette.surfaceContainer,
            palette.onSurfaceVariant to palette.surfaceVariant,
            palette.onPrimary to palette.primary,
            palette.onPrimaryContainer to palette.primaryContainer,
            palette.onSecondaryContainer to palette.secondaryContainer,
            palette.onTertiary to palette.tertiary,
        )
        for ((foreground, background) in pairs) {
            val light = maxOf(foreground.luminance(), background.luminance())
            val dark = minOf(foreground.luminance(), background.luminance())
            val ratio = (light + 0.05f) / (dark + 0.05f)
            assertTrue(ratio >= 4.5f, "Contrast between $foreground and $background is $ratio")
        }
    }
}
