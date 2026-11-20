// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.rosan.installer.domain.settings.model.preferences.theme.UiStyle
import com.rosan.installer.ui.theme.InstallerTheme
import com.rosan.installer.ui.theme.LocalUiStyle

/** Variant palettes retain Aero's contrast-tested surface roles while changing the accent colors. */
internal fun aeroVariantColors(style: UiStyle, dark: Boolean): ColorScheme {
    val base = if (dark) AeroDarkColors else AeroColors
    return when (style) {
        UiStyle.FRUTIGER_METRO -> if (dark) base.copy(
            primary = Color(0xFFFFB1D1), onPrimary = Color(0xFF59002E),
            primaryContainer = Color(0xFF7D174D), onPrimaryContainer = Color(0xFFFFD9E7),
            secondary = Color(0xFFFFD77D), onSecondary = Color(0xFF422C00),
        ) else base.copy(
            primary = Color(0xFF9F1759), onPrimary = Color.White,
            primaryContainer = Color(0xFFFFD9E7), onPrimaryContainer = Color(0xFF40001E),
            secondary = Color(0xFF765900), onSecondary = Color.White,
        )
        UiStyle.FRUTIGER_AURORA -> if (dark) base.copy(
            primary = Color(0xFFD0BCFF), onPrimary = Color(0xFF35265F),
            primaryContainer = Color(0xFF4C3C78), onPrimaryContainer = Color(0xFFEADDFF),
        ) else base.copy(
            primary = Color(0xFF6045A0), onPrimary = Color.White,
            primaryContainer = Color(0xFFEADDFF), onPrimaryContainer = Color(0xFF210C53),
        )
        UiStyle.DARK_AERO -> AeroDarkColors.copy(
            background = Color(0xFF090D12), surface = Color(0xFF11171E),
            surfaceContainerLowest = Color(0xFF05080B), surfaceContainerLow = Color(0xFF11171E),
            surfaceContainer = Color(0xFF19222A), surfaceContainerHigh = Color(0xFF243039),
            surfaceContainerHighest = Color(0xFF303D47),
        )
        UiStyle.FRUTIGER_ECO -> if (dark) base.copy(
            primary = Color(0xFF9CDB8D), onPrimary = Color(0xFF073A0A),
            primaryContainer = Color(0xFF285120), onPrimaryContainer = Color(0xFFB8F7A7),
        ) else base.copy(
            primary = Color(0xFF326A27), onPrimary = Color.White,
            primaryContainer = Color(0xFFB8F7A7), onPrimaryContainer = Color(0xFF0B3909),
        )
        UiStyle.FRUTIGER_AQUA -> if (dark) base.copy(
            primary = Color(0xFF6EDCF0), onPrimary = Color(0xFF00363F),
            primaryContainer = Color(0xFF004F5C), onPrimaryContainer = Color(0xFFB1ECF8),
        ) else base.copy(
            primary = Color(0xFF006876), onPrimary = Color.White,
            primaryContainer = Color(0xFF9DEFFF), onPrimaryContainer = Color(0xFF002027),
        )
        else -> base
    }
}

@Composable
internal fun AeroBackdrop() {
    val style = LocalUiStyle.current
    val dark = InstallerTheme.isDark
    if (style == UiStyle.FRUTIGER_AERO) {
        AeroAquarium()
    } else {
        Canvas(Modifier.fillMaxSize()) { drawVariantBackdrop(style, dark) }
    }
}

/** Decorative vector motifs stay underneath the app's accessible controls. */
private fun DrawScope.drawVariantBackdrop(style: UiStyle, dark: Boolean) {
    val colors = when (style) {
        UiStyle.FRUTIGER_METRO -> if (dark) listOf(Color(0xFF242039), Color(0xFF123C48)) else listOf(Color(0xFFFFF0F8), Color(0xFFD9F7FA))
        UiStyle.FRUTIGER_AURORA -> if (dark) listOf(Color(0xFF111A3D), Color(0xFF153C3C)) else listOf(Color(0xFFECE8FF), Color(0xFFCFF5E9))
        UiStyle.DARK_AERO -> listOf(Color(0xFF070B12), Color(0xFF142C39))
        UiStyle.FRUTIGER_ECO -> if (dark) listOf(Color(0xFF122D28), Color(0xFF203C26)) else listOf(Color(0xFFE9FAFF), Color(0xFFDDF5C7))
        else -> if (dark) listOf(Color(0xFF092736), Color(0xFF075062)) else listOf(Color(0xFFE7FAFF), Color(0xFF99DDE9))
    }
    drawRect(Brush.verticalGradient(colors))
    val w = size.width
    val h = size.height
    when (style) {
        UiStyle.FRUTIGER_METRO -> {
            drawCircle(Color(0x55FF5D9E), w * 0.32f, Offset(w * 0.95f, h * 0.12f))
            drawCircle(Color(0x5595CE37), w * 0.26f, Offset(w * 0.04f, h * 0.84f))
            drawRect(Color(0x558C77E8), Offset(w * 0.75f, h * 0.65f), androidx.compose.ui.geometry.Size(w * 0.25f, h * 0.12f))
        }
        UiStyle.FRUTIGER_AURORA, UiStyle.DARK_AERO -> {
            val ribbon = Path().apply {
                moveTo(0f, h * 0.27f)
                cubicTo(w * 0.25f, h * 0.06f, w * 0.62f, h * 0.43f, w, h * 0.12f)
                lineTo(w, h * 0.25f)
                cubicTo(w * 0.62f, h * 0.55f, w * 0.25f, h * 0.2f, 0f, h * 0.39f)
                close()
            }
            drawPath(ribbon, Brush.verticalGradient(listOf(Color(0x5587F8DF), Color(0x334F6DFF), Color.Transparent)))
            drawCircle(Color(0x4483F5D7), w * 0.12f, Offset(w * 0.86f, h * 0.76f))
        }
        UiStyle.FRUTIGER_ECO -> {
            val hill = Path().apply {
                moveTo(0f, h * 0.86f)
                quadraticTo(w * 0.35f, h * 0.64f, w * 0.7f, h * 0.84f)
                quadraticTo(w * 0.87f, h * 0.92f, w, h * 0.78f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(hill, Color(0x5571C956))
            drawCircle(Color(0x55FFED93), w * 0.16f, Offset(w * 0.85f, h * 0.1f))
        }
        UiStyle.FRUTIGER_AQUA -> {
            repeat(13) { i ->
                val x = w * (if (i % 2 == 0) 0.07f + (i % 3) * 0.06f else 0.93f - (i % 3) * 0.06f)
                val y = h * ((i * 0.173f) % 1f)
                drawAeroBubble(Offset(x, y), w * (0.012f + (i % 4) * 0.008f), 0.65f)
            }
            repeat(3) { i ->
                val wave = Path().apply {
                    moveTo(0f, h * (0.74f + i * 0.06f))
                    cubicTo(w * 0.3f, h * (0.67f + i * 0.06f), w * 0.7f, h * (0.85f + i * 0.06f), w, h * (0.77f + i * 0.06f))
                }
                drawPath(wave, Color(0x66FFFFFF), style = Stroke(width = 2f))
            }
        }
        else -> Unit
    }
}
