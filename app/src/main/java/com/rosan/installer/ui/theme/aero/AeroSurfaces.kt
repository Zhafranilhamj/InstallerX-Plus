// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

private val GlassReflection = Brush.verticalGradient(
    0f to Color(0xD9FFFFFF),
    0.38f to Color(0x26FFFFFF),
    0.42f to Color.Transparent,
    0.71f to Color.Transparent,
    1f to Color(0x3059D5EE),
)
private val GelOrb = Brush.verticalGradient(
    0f to Color.White,
    0.43f to Color(0xFFA9F4FF),
    0.46f to Color(0xFF74DCED),
    1f to Color(0xFFC6F2BD),
)

/** Make full-page surfaces transparent without changing the color roles of controls or popups. */
@Composable
internal fun aeroPageColor(fallback: Color = MaterialTheme.colorScheme.background): Color =
    if (LocalFrutigerAero.current) Color.Transparent else fallback

/** Lens highlights are applied below the content on transparent surfaces. */
internal fun Modifier.aeroGlass(shape: Shape, tint: Color): Modifier = clip(shape)
    .background(tint)
    .background(GlassReflection)
    .border(1.dp, Color(0xB3FFFFFF), shape)

/** A light finish for surfaces which own their own fill (e.g. Material cards and popup windows). */
@Composable
internal fun Modifier.aeroChrome(shape: Shape): Modifier {
    if (!LocalFrutigerAero.current) return this
    return clip(shape).drawWithCache {
        val polish = Brush.verticalGradient(
            0f to Color(0x38FFFFFF),
            0.38f to Color.Transparent,
            0.8f to Color.Transparent,
            1f to Color(0x2423BCD9),
        )
        onDrawWithContent {
            drawContent()
            drawRect(polish)
        }
    }.border(1.dp, Color(0xCCFFFFFF), shape)
}

@Composable
internal fun Modifier.aeroRise(): Modifier {
    if (!LocalFrutigerAero.current) return this
    val progress = remember { Animatable(0f) }
    LaunchedEffect(progress) { progress.animateTo(1f, spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessLow)) }
    return graphicsLayer {
        translationY = (1f - progress.value) * 20.dp.toPx()
        alpha = progress.value.coerceIn(0f, 1f)
    }
}

/** Keep the recognizable action glyph; the glass orb around it is decorative. */
@Composable
internal fun AeroIcon(content: @Composable () -> Unit) {
    if (LocalFrutigerAero.current) {
        Box(
            modifier = Modifier.background(GelOrb, CircleShape).border(1.dp, Color(0xFFB4E2ED), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    } else {
        content()
    }
}

private val ToolbarSheen = Brush.verticalGradient(
    0f to Color(0xECF4FDFF),
    0.55f to Color(0xDFD2F5FF),
    0.57f to Color(0xDCAFE9F9),
    1f to Color(0xC6DFF9F9),
)

@Composable
internal fun Modifier.aeroToolbar(): Modifier = if (LocalFrutigerAero.current) background(ToolbarSheen) else this
