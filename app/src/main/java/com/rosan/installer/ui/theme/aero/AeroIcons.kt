// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.foundation.Image
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon as MaterialIcon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale

/** One entry point for all action glyphs, including resource painters and auto-mirrored vectors. */
@Composable
internal fun InstallerIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    if (LocalFrutigerAero.current) {
        InstallerIcon(rememberVectorPainter(imageVector), contentDescription, modifier, tint)
    } else {
        MaterialIcon(imageVector, contentDescription, modifier, tint)
    }
}

@Composable
internal fun InstallerIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    if (LocalFrutigerAero.current) {
        MaterialIcon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier
                .alpha(if (tint.isSpecified) tint.alpha else 1f)
                .aeroIconRelief(aeroIconPigment(tint, MaterialTheme.colorScheme)),
            tint = if (tint.isSpecified) Color.White else Color.Unspecified,
        )
    } else {
        MaterialIcon(painter, contentDescription, modifier, tint)
    }
}

@Composable
internal fun InstallerIcon(
    bitmap: ImageBitmap,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    if (LocalFrutigerAero.current) {
        MaterialIcon(
            bitmap = bitmap,
            contentDescription = contentDescription,
            modifier = modifier
                .alpha(if (tint.isSpecified) tint.alpha else 1f)
                .aeroIconRelief(aeroIconPigment(tint, MaterialTheme.colorScheme)),
            tint = if (tint.isSpecified) Color.White else Color.Unspecified,
        )
    } else {
        MaterialIcon(bitmap, contentDescription, modifier, tint)
    }
}

/** Keep installed-app artwork recognizable; give it a clear glossy coat, not a replacement logo. */
@Composable
internal fun InstallerIconImage(
    bitmap: ImageBitmap,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = 1f,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = FilterQuality.Low,
) {
    Image(
        bitmap = bitmap,
        contentDescription = contentDescription,
        modifier = if (LocalFrutigerAero.current) modifier.aeroIconRelief(Color.Unspecified) else modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
    )
}

/** Neutral theme ink becomes aqua enamel. Explicit warning/success colors and disabled alpha survive. */
internal fun aeroIconPigment(tint: Color, scheme: ColorScheme): Color {
    if (!tint.isSpecified) return Color.Unspecified
    val opaque = tint.copy(alpha = 1f)
    val neutralInk = listOf(scheme.onSurface, scheme.onSurfaceVariant, scheme.onBackground, scheme.onPrimaryContainer)
    return if (neutralInk.any { it.copy(alpha = 1f) == opaque }) Color(0xFF008AB8) else opaque
}

/**
 * Sculpt the actual source silhouette, rather than putting a flat symbol on a shiny badge.
 * All masks use small, isolated saveLayers: SrcIn/DstOut must never reach a card or nearby icon.
 * Brushes and paints are cached by size; no bitmap allocation, network asset, or animation is needed.
 */
private fun Modifier.aeroIconRelief(pigment: Color): Modifier = drawWithCache {
    val bounds = Rect(Offset.Zero, size)
    val layerPaint = Paint()
    val erasePaint = Paint().apply { blendMode = BlendMode.DstOut }
    val edge = size.minDimension * 0.025f
    val depth = size.minDimension * 0.065f
    val ink = if (pigment.isSpecified) pigment else Color(0xFF087A9B)
    val body = Brush.verticalGradient(
        0f to lerp(ink, Color.White, 0.68f),
        0.22f to lerp(ink, Color.White, 0.25f),
        0.48f to ink,
        0.5f to lerp(ink, Color.Black, 0.18f),
        0.82f to ink,
        1f to lerp(ink, Color.White, 0.38f),
    )
    val side = Brush.verticalGradient(listOf(lerp(ink, Color.Black, 0.3f), lerp(ink, Color.Black, 0.58f)))
    val specular = Brush.radialGradient(
        listOf(Color(0xBFFFFFFF), Color(0x25FFFFFF), Color.Transparent),
        center = Offset(size.width * 0.31f, size.height * 0.12f),
        radius = size.minDimension * 0.8f,
    )
    val rim = Brush.verticalGradient(listOf(Color.White, Color(0x35FFFFFF)))
    val shadow = Brush.verticalGradient(listOf(Color(0x14001C37), Color(0x4D001C37)))

    onDrawWithContent {
        // Leave room for the extruded side inside the original measured icon/touch target.
        scale(0.88f, pivot = center) {
            translate(left = depth * 0.5f, top = depth * 1.8f) {
                iconLayer(bounds, layerPaint) {
                    drawContent()
                    drawRect(shadow, blendMode = BlendMode.SrcIn)
                }
            }
            for (step in 3 downTo 1) {
                translate(left = depth * step / 6f, top = depth * step / 3f) {
                    iconLayer(bounds, layerPaint) {
                        drawContent()
                        drawRect(side, blendMode = BlendMode.SrcIn)
                    }
                }
            }
            iconLayer(bounds, layerPaint) {
                drawContent()
                if (pigment.isSpecified) drawRect(body, blendMode = BlendMode.SrcIn)
                drawRect(specular, blendMode = BlendMode.SrcAtop)
            }
            // Difference between the silhouette and its offset makes a true, glyph-shaped bevel.
            iconLayer(bounds, layerPaint) {
                drawContent()
                iconLayer(bounds, erasePaint) {
                    translate(left = edge, top = edge) { drawContent() }
                }
                drawRect(rim, blendMode = BlendMode.SrcIn)
            }
        }
    }
}

private inline fun ContentDrawScope.iconLayer(bounds: Rect, paint: Paint, content: ContentDrawScope.() -> Unit) {
    val canvas = drawContext.canvas
    canvas.saveLayer(bounds, paint)
    try {
        content()
    } finally {
        canvas.restore()
    }
}
