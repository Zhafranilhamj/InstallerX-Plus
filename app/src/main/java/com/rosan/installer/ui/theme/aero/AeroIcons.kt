// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import com.rosan.installer.ui.theme.InstallerTheme

/** Lightens this color towards white while keeping its alpha. */
internal fun Color.lighten(fraction: Float): Color = Color(
    red = this.red + (1f - this.red) * fraction,
    green = this.green + (1f - this.green) * fraction,
    blue = this.blue + (1f - this.blue) * fraction,
    alpha = this.alpha,
)

/** Darkens this color towards black while keeping its alpha. */
internal fun Color.darken(fraction: Float): Color = Color(
    red = this.red * (1f - fraction),
    green = this.green * (1f - fraction),
    blue = this.blue * (1f - fraction),
    alpha = this.alpha,
)

/**
 * The single entry point for the icons of the app.
 *
 * Frutiger Aero swaps the flat Material glyph for a raised, glossy one. Every other style keeps
 * the plain Material icon, so the Material 3 Expressive and the Miuix families are left alone.
 */
@Composable
internal fun InstallerIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    if (LocalFrutigerAero.current) {
        AeroGlossyIcon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
    } else {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
    }
}

/**
 * Draws an icon as a raised, glossy object.
 *
 * The glyph is filled with a vertical gradient that is lit from above, a specular highlight
 * covers its upper half, the foot is shaded so it reads as a volume instead of a flat sticker and
 * a soft silhouette underneath lifts it off the surface. The gradient and the highlights are
 * painted inside an offscreen layer so they only ever touch the glyph itself.
 */
@Composable
internal fun AeroGlossyIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    val shadowColor = if (InstallerTheme.isDark) Color(0x99000612) else Color(0x5C073A55)
    val untinted = tint == Color.Unspecified
    Box(modifier = modifier) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = shadowColor,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { translationY = size.height * 0.055f },
        )
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = if (untinted) Color.Unspecified else Color.White,
            modifier = Modifier
                .graphicsLayer { compositionStrategy = CompositingStrategy.Offscreen }
                .drawWithCache {
                    val height = size.height.coerceAtLeast(1f)
                    val body = if (untinted) {
                        null
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(tint.lighten(0.55f), tint, tint.darken(0.34f)),
                            startY = 0f,
                            endY = height,
                        )
                    }
                    val gloss = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.85f),
                            Color.White.copy(alpha = 0.16f),
                            Color.Transparent,
                        ),
                        startY = 0f,
                        endY = (height * 0.66f).coerceAtLeast(1f),
                    )
                    val foot = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0x59000000),
                        ),
                        startY = 0f,
                        endY = height,
                    )
                    onDrawWithContent {
                        drawContent()
                        body?.let { drawRect(brush = it, blendMode = BlendMode.SrcIn) }
                        drawRect(brush = gloss, blendMode = BlendMode.SrcAtop)
                        drawRect(brush = foot, blendMode = BlendMode.SrcAtop)
                    }
                },
        )
    }
}
