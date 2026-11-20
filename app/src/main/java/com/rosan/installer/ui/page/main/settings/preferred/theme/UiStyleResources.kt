// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.page.main.settings.preferred.theme

import com.rosan.installer.R
import com.rosan.installer.domain.settings.model.preferences.theme.UiStyle

fun UiStyle.titleRes(): Int = when (this) {
    UiStyle.CURRENT -> R.string.ui_style_current
    UiStyle.LIQUID_GLASS -> R.string.ui_style_glass
    UiStyle.MATERIAL_EXPRESSIVE -> R.string.ui_style_material
    UiStyle.MIXED -> R.string.ui_style_mixed
    UiStyle.FRUTIGER_METRO -> R.string.ui_style_metro
    UiStyle.FRUTIGER_AURORA -> R.string.ui_style_aurora
    UiStyle.DARK_AERO -> R.string.ui_style_dark_aero
    UiStyle.FRUTIGER_ECO -> R.string.ui_style_eco
    UiStyle.FRUTIGER_AQUA -> R.string.ui_style_aqua
    UiStyle.FRUTIGER_AERO -> R.string.ui_style_aero
}

fun UiStyle.descriptionRes(): Int = when (this) {
    UiStyle.CURRENT -> R.string.ui_style_current_desc
    UiStyle.LIQUID_GLASS -> R.string.ui_style_glass_desc
    UiStyle.MATERIAL_EXPRESSIVE -> R.string.ui_style_material_desc
    UiStyle.MIXED -> R.string.ui_style_mixed_desc
    UiStyle.FRUTIGER_METRO -> R.string.ui_style_metro_desc
    UiStyle.FRUTIGER_AURORA -> R.string.ui_style_aurora_desc
    UiStyle.DARK_AERO -> R.string.ui_style_dark_aero_desc
    UiStyle.FRUTIGER_ECO -> R.string.ui_style_eco_desc
    UiStyle.FRUTIGER_AQUA -> R.string.ui_style_aqua_desc
    UiStyle.FRUTIGER_AERO -> R.string.ui_style_aero_desc
}
