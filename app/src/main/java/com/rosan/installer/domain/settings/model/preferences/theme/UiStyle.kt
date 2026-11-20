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
    FRUTIGER_METRO,
    FRUTIGER_AURORA,
    DARK_AERO,
    FRUTIGER_ECO,
    FRUTIGER_AQUA,
    ;

    companion object {
        fun fromValue(value: String?): UiStyle = entries.firstOrNull { it.name == value } ?: CURRENT
    }

    val isAero: Boolean
        get() = when (this) {
            FRUTIGER_AERO, FRUTIGER_METRO, FRUTIGER_AURORA, DARK_AERO, FRUTIGER_ECO, FRUTIGER_AQUA -> true
            else -> false
        }

    fun useMiuix(current: Boolean): Boolean = when (this) {
        CURRENT -> current
        LIQUID_GLASS -> true
        MATERIAL_EXPRESSIVE, MIXED, FRUTIGER_AERO, FRUTIGER_METRO, FRUTIGER_AURORA, DARK_AERO, FRUTIGER_ECO, FRUTIGER_AQUA -> false
    }

    fun useBlur(current: Boolean, supported: Boolean): Boolean = supported && when (this) {
        CURRENT -> current
        LIQUID_GLASS, MIXED -> true
        MATERIAL_EXPRESSIVE, FRUTIGER_AERO, FRUTIGER_METRO, FRUTIGER_AURORA, DARK_AERO, FRUTIGER_ECO, FRUTIGER_AQUA -> false
    }

    fun useFloatingBar(current: Boolean): Boolean = when (this) {
        CURRENT -> current
        LIQUID_GLASS, MIXED, FRUTIGER_AERO, FRUTIGER_METRO, FRUTIGER_AURORA, DARK_AERO, FRUTIGER_ECO, FRUTIGER_AQUA -> true
        MATERIAL_EXPRESSIVE -> false
    }
}
