// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.domain.settings.model.preferences.theme

/** Presets override the appearance without changing the user's original settings. */
enum class UiStyle {
    CURRENT,
    LIQUID_GLASS,
    MATERIAL_EXPRESSIVE,
    MIXED,
    FRUTIGER_AERO,
    ;

    companion object {
        fun fromValue(value: String?): UiStyle = entries.firstOrNull { it.name == value } ?: CURRENT
    }

    fun useMiuix(current: Boolean): Boolean = when (this) {
        CURRENT -> current
        LIQUID_GLASS -> true
        MATERIAL_EXPRESSIVE, MIXED, FRUTIGER_AERO -> false
    }

    fun useBlur(current: Boolean, supported: Boolean): Boolean = supported && when (this) {
        CURRENT -> current
        LIQUID_GLASS, MIXED -> true
        MATERIAL_EXPRESSIVE, FRUTIGER_AERO -> false
    }

    fun useFloatingBar(current: Boolean): Boolean = when (this) {
        CURRENT -> current
        LIQUID_GLASS, MIXED, FRUTIGER_AERO -> true
        MATERIAL_EXPRESSIVE -> false
    }
}
