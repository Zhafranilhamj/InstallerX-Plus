// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rosan.installer.domain.settings.model.preferences.theme.PaletteStyle
import com.rosan.installer.domain.settings.model.preferences.theme.ThemeColorSpec
import com.rosan.installer.domain.settings.model.preferences.theme.ThemeMode
import com.rosan.installer.domain.settings.model.preferences.theme.UiStyle
import com.rosan.installer.ui.theme.InstallerTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AeroVariantsRenderingTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun variantsHaveDistinctPalettesAndDarkAeroStaysDark() {
        val variants = listOf(UiStyle.FRUTIGER_METRO, UiStyle.FRUTIGER_AURORA, UiStyle.DARK_AERO, UiStyle.FRUTIGER_ECO, UiStyle.FRUTIGER_AQUA)
        assertEquals(variants.size, variants.map { aeroVariantColors(it, it == UiStyle.DARK_AERO).primary }.distinct().size)
        compose.setContent {
            InstallerTheme(
                useMiuix = false,
                themeMode = ThemeMode.LIGHT,
                paletteStyle = PaletteStyle.Expressive,
                colorSpec = ThemeColorSpec.SPEC_2025,
                useDynamicColor = false,
                useMiuixMonet = false,
                seedColor = androidx.compose.ui.graphics.Color.Blue,
                uiStyle = UiStyle.DARK_AERO,
            ) {
                assertTrue(InstallerTheme.isDark)
                assertTrue(LocalFrutigerAero.current)
            }
        }
    }

    @Test
    fun eachVariantDrawsADistinctBackdrop() {
        val variants = listOf(UiStyle.FRUTIGER_METRO, UiStyle.FRUTIGER_AURORA, UiStyle.DARK_AERO, UiStyle.FRUTIGER_ECO, UiStyle.FRUTIGER_AQUA)
        val selected = androidx.compose.runtime.mutableStateOf(variants.first())
        compose.setContent {
            InstallerTheme(
                useMiuix = false,
                themeMode = ThemeMode.LIGHT,
                paletteStyle = PaletteStyle.Expressive,
                colorSpec = ThemeColorSpec.SPEC_2025,
                useDynamicColor = false,
                useMiuixMonet = false,
                seedColor = androidx.compose.ui.graphics.Color.Blue,
                uiStyle = selected.value,
            ) {
                Box(Modifier.size(160.dp).testTag("backdrop")) { AeroBackdrop() }
            }
        }
        val samples = variants.map { style ->
            compose.runOnIdle { selected.value = style }
            val image = compose.onNodeWithTag("backdrop").captureToImage().toPixelMap()
            image[image.width / 2, image.height / 2]
        }
        assertEquals(variants.size, samples.distinct().size)
        assertNotEquals(samples.first(), samples.last())
    }
}
