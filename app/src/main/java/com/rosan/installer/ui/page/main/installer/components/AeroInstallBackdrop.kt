// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.page.main.installer.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rosan.installer.ui.theme.InstallerTheme
import kotlin.math.PI
import kotlin.math.sin

@Composable
internal fun Modifier.aeroInstallBackdrop(): Modifier {
    val dark = InstallerTheme.isDark
    val cycle by rememberInfiniteTransition(label = "Aquarium").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing)),
        label = "Ocean current",
    )
    return drawBehind {
        val angle = cycle * 2 * PI
        if (dark) {
            // Each light drifts upward and fades out before it wraps back to the seabed.
            repeat(24) { i ->
                val rise = (cycle + i * 0.371f) % 1f
                val x = (i * 0.618f % 1f + 0.025f * sin(angle + i).toFloat()) * size.width
                val center = Offset(x, (1f - rise) * size.height)
                val radius = (2 + i % 5).dp.toPx()
                val light = Color(0xFF62F7C5).copy(alpha = 0.48f * sin(rise * PI).toFloat())
                drawCircle(Brush.radialGradient(listOf(light, Color.Transparent), center, radius), radius, center)
            }
        } else {
            // Soft diagonal shafts sweep across the shallow water.
            repeat(4) { i ->
                val x = size.width * (i * 0.32f - 0.16f + 0.07f * sin(angle + i).toFloat())
                val width = size.width * 0.1f
                drawRect(
                    Brush.linearGradient(
                        listOf(Color.Transparent, Color(0x6670E0F2), Color.Transparent),
                        Offset(x, size.height / 2),
                        Offset(x + width, size.height / 2 + width * 0.42f),
                    ),
                )
            }
        }
    }
}
