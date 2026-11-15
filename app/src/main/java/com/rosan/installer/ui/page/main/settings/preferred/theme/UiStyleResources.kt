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
}

fun UiStyle.descriptionRes(): Int = when (this) {
    UiStyle.CURRENT -> R.string.ui_style_current_desc
    UiStyle.LIQUID_GLASS -> R.string.ui_style_glass_desc
    UiStyle.MATERIAL_EXPRESSIVE -> R.string.ui_style_material_desc
    UiStyle.MIXED -> R.string.ui_style_mixed_desc
}
