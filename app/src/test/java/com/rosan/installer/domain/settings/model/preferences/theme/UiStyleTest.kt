// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.domain.settings.model.preferences.theme

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UiStyleTest {
    @Test
    fun currentPreservesExistingSettings() {
        for (value in listOf(false, true)) {
            assertEquals(value, UiStyle.CURRENT.useMiuix(value))
            assertEquals(value, UiStyle.CURRENT.useBlur(value, supported = true))
            assertEquals(value, UiStyle.CURRENT.useFloatingBar(value))
        }
    }

    @Test
    fun presetsChooseDistinctCombinations() {
        assertTrue(UiStyle.LIQUID_GLASS.useMiuix(false))
        assertTrue(UiStyle.LIQUID_GLASS.useBlur(false, supported = true))
        assertTrue(UiStyle.LIQUID_GLASS.useFloatingBar(false))
        assertFalse(UiStyle.MATERIAL_EXPRESSIVE.useMiuix(true))
        assertFalse(UiStyle.MATERIAL_EXPRESSIVE.useBlur(true, supported = true))
        assertFalse(UiStyle.MATERIAL_EXPRESSIVE.useFloatingBar(true))
        assertFalse(UiStyle.MIXED.useMiuix(true))
        assertTrue(UiStyle.MIXED.useBlur(false, supported = true))
        assertTrue(UiStyle.MIXED.useFloatingBar(false))
    }

    @Test
    fun frutigerAeroUsesFloatingMaterialControlsWithoutPlatformBlur() {
        val variants = listOf(UiStyle.FRUTIGER_AERO, UiStyle.FRUTIGER_METRO, UiStyle.FRUTIGER_AURORA, UiStyle.DARK_AERO, UiStyle.FRUTIGER_ECO, UiStyle.FRUTIGER_AQUA)
        for (style in variants) {
            assertEquals(style, UiStyle.fromValue(style.name))
            assertTrue(style.isAero)
            for (previousSetting in listOf(false, true)) {
                assertFalse(style.useMiuix(previousSetting))
                assertFalse(style.useBlur(previousSetting, supported = true))
                assertTrue(style.useFloatingBar(previousSetting))
            }
        }
        assertFalse(UiStyle.CURRENT.isAero)
    }

    @Test
    fun unsupportedBlurAndUnknownValuesFallBackSafely() {
        UiStyle.entries.forEach { assertFalse(it.useBlur(true, supported = false)) }
        assertEquals(UiStyle.CURRENT, UiStyle.fromValue(null))
        assertEquals(UiStyle.CURRENT, UiStyle.fromValue("unknown"))
        UiStyle.entries.forEach { assertEquals(it, UiStyle.fromValue(it.name)) }
    }
}
