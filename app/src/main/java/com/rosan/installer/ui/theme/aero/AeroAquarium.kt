// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.rosan.installer.R
import com.rosan.installer.ui.theme.InstallerTheme
import kotlin.math.min
import kotlin.math.sin

private const val Turn = 6.2831855f

/**
 * Height of the water surface inside the aquarium backdrop, as a fraction of the image height.
 * The portrait and landscape backdrops share the same pond, so one constant describes both.
 */
private const val WaterSurface = 0.703f

private val SurfaceWash = Brush.verticalGradient(
    0f to Color(0xA8E7FAFF),
    0.58f to Color(0x65E7FAFF),
    1f to Color(0x22E2FFAE),
)

/** Pure decoration: one draw-phase clock, no input or accessibility nodes. */
@Composable
internal fun AeroAquarium() {
    val dark = InstallerTheme.isDark
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val painter = painterResource(R.drawable.aero_aquarium)
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        if (lifecycleState.isAtLeast(Lifecycle.State.RESUMED)) {
            val clock = rememberInfiniteTransition(label = "Aquarium")
            val phase = clock.animateFloat(0f, 1f, infiniteRepeatable(tween(28000, easing = LinearEasing)), label = "Drift")
            Canvas(Modifier.fillMaxSize()) { drawAquarium(phase.value, dark, painter.intrinsicSize) }
        } else {
            Canvas(Modifier.fillMaxSize()) { drawAquarium(0f, dark, painter.intrinsicSize) }
        }
    }
}

private fun DrawScope.drawAquarium(phase: Float, dark: Boolean, source: Size) {
    if (dark) drawRect(Color(0xDE031629)) else drawRect(SurfaceWash)
    val light = Offset(size.width * (0.19f + 0.07f * sin(phase * Turn)), size.height * 0.08f)
    drawCircle(
        brush = Brush.radialGradient(listOf(if (dark) Color(0x553CAFA9) else Color(0xC7FFFFDF), Color.Transparent), light, size.minDimension * 0.72f),
        center = light,
        radius = size.minDimension * 0.72f,
    )
    // Keep most moving detail close to the margins, away from labels and touch targets.
    repeat(14) { i ->
        val progress = (phase * (1 + i % 2) + i * 0.173f) % 1f
        val lane = if (i % 2 == 0) 0.035f + i * 0.018f else 0.98f - i * 0.015f
        val x = size.width * (lane + 0.018f * sin(progress * Turn * 2 + i))
        val y = size.height * (1.02f - progress)
        val radius = (4 + i * 7 % 23).dp.toPx()
        val opacity = minOf(progress * 8, (1f - progress) * 10, 0.68f)
        drawAeroBubble(Offset(x, y), radius, opacity)
        if (progress > 0.92f) {
            drawSparkle(Offset(x, y), radius * 0.6f, (1f - progress) * 7f)
        }
    }
    // The fish belong to the pond, so they are clipped to the water and never cross the surface.
    val surface = waterSurfaceRow(size, source)
    clipRect(top = surface) {
        drawFish(phase, 1f, surface, size.height, 0.34f, 1f, Color(0xFFFF9A3C))
        drawFish((phase + 0.34f) % 1f, -1f, surface, size.height, 0.6f, 0.78f, Color(0xFFFFD447))
        drawFish((phase + 0.63f) % 1f, 1f, surface, size.height, 0.84f, 0.62f, Color(0xFFFF74A6))
    }
}

/**
 * Maps the water surface of the backdrop to the canvas row.
 *
 * The backdrop is drawn with [ContentScale.Crop] and centered alignment, so the visible part of the
 * source image depends on the aspect ratio of the canvas. Reproducing that crop keeps the fish
 * aligned with the pond on every screen size and orientation.
 */
private fun waterSurfaceRow(canvas: Size, source: Size): Float {
    if (source.width <= 0f || source.height <= 0f || canvas.height <= 0f) return canvas.height * WaterSurface
    val sourceAspect = source.width / source.height
    val canvasAspect = canvas.width / canvas.height
    val scale = if (sourceAspect > canvasAspect) canvas.height / source.height else canvas.width / source.width
    val offsetY = (canvas.height - source.height * scale) / 2f
    return offsetY + source.height * WaterSurface * scale
}

internal fun DrawScope.drawAeroBubble(center: Offset, radius: Float, opacity: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            0.4f to Color(0x0DF6FFFF),
            0.78f to Color(0x2551CFE5),
            0.94f to Color(0xBBFBFFFF),
            1f to Color(0xB035A4C9),
            center = center,
            radius = radius,
        ),
        radius = radius,
        center = center,
        alpha = opacity,
    )
    drawOval(
        color = Color.White.copy(alpha = opacity * 0.9f),
        topLeft = center - Offset(radius * 0.52f, radius * 0.68f),
        size = Size(radius * 0.63f, radius * 0.23f),
    )
}

private fun DrawScope.drawSparkle(center: Offset, length: Float, opacity: Float) {
    val white = Color.White.copy(alpha = opacity)
    drawLine(white, center - Offset(length, 0f), center + Offset(length, 0f), strokeWidth = 1.dp.toPx())
    drawLine(white, center - Offset(0f, length), center + Offset(0f, length), strokeWidth = 1.dp.toPx())
}

private fun DrawScope.drawFish(
    phase: Float,
    direction: Float,
    waterTop: Float,
    waterBottom: Float,
    depth: Float,
    scale: Float,
    tint: Color,
) {
    val band = (waterBottom - waterTop).coerceAtLeast(1f)
    // Keep the fish small enough to stay inside the pond on short screens.
    val r = min(20.dp.toPx() * scale, band * 0.22f)
    val span = size.width + r * 4f
    val x = if (direction > 0f) -r * 2f + phase * span else size.width + r * 2f - phase * span
    val y = waterTop + band * depth + r * 0.3f * sin(phase * Turn * 2f)
    val tail = r * (0.45f + 0.12f * sin(phase * Turn * 20f))
    scale(direction, 1f, pivot = Offset(x, y)) {
        translate(x, y) {
            val fin = Path().apply {
                moveTo(-r * 0.45f, 0f)
                quadraticTo(-r * 1.4f, -tail, -r * 1.5f, -tail)
                quadraticTo(-r * 1.2f, 0f, -r * 1.5f, tail)
                quadraticTo(-r * 1.4f, tail, -r * 0.45f, 0f)
                close()
            }
            drawPath(fin, color = tint.copy(alpha = 0.5f))
            val bodySize = Size(r * 1.75f, r * 0.88f)
            val origin = Offset(-r * 0.8f, -r * 0.44f)
            // Lit from above: a dark back, a saturated flank and a pale belly.
            drawOval(
                brush = Brush.verticalGradient(
                    colors = listOf(tint.darken(0.38f), tint, tint.lighten(0.74f)),
                    startY = origin.y,
                    endY = origin.y + bodySize.height,
                ),
                topLeft = origin,
                size = bodySize,
            )
            drawOval(Color.White.copy(alpha = 0.45f), origin, bodySize, style = Stroke(0.7.dp.toPx()))
            drawCircle(Color(0xE0105472), r * 0.1f, Offset(r * 0.52f, -r * 0.08f))
        }
    }
}
