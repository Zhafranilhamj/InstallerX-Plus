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
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.rosan.installer.R
import kotlin.math.sin

private const val Turn = 6.2831855f
private val SurfaceWash = Brush.verticalGradient(
    0f to Color(0xA8E7FAFF),
    0.58f to Color(0x65E7FAFF),
    1f to Color(0x22E2FFAE),
)

/** Pure decoration: one draw-phase clock, no input or accessibility nodes. */
@Composable
internal fun AeroAquarium() {
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.aero_aquarium),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        if (lifecycleState.isAtLeast(Lifecycle.State.RESUMED)) {
            val clock = rememberInfiniteTransition(label = "Aquarium")
            val phase = clock.animateFloat(0f, 1f, infiniteRepeatable(tween(28000, easing = LinearEasing)), label = "Drift")
            Canvas(Modifier.fillMaxSize()) { drawAquarium(phase.value) }
        } else {
            Canvas(Modifier.fillMaxSize()) { drawAquarium(0f) }
        }
    }
}

private fun DrawScope.drawAquarium(phase: Float) {
    drawRect(SurfaceWash)
    val light = Offset(size.width * (0.19f + 0.07f * sin(phase * Turn)), size.height * 0.08f)
    drawCircle(
        brush = Brush.radialGradient(listOf(Color(0xC7FFFFDF), Color.Transparent), light, size.minDimension * 0.72f),
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
    drawFish(phase, 0.66f, 0.84f, Color(0xFF11A3B9))
    drawFish((phase + 0.34f) % 1f, 0.78f, 1.1f, Color(0xFF75BC48))
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

private fun DrawScope.drawFish(phase: Float, lane: Float, scale: Float, tint: Color) {
    val r = 18.dp.toPx() * scale
    val x = size.width * phase + (2 * phase - 1) * r * 2
    val y = size.height * lane + r * sin(phase * Turn * 2)
    val tail = r * (0.5f + 0.15f * sin(phase * Turn * 22))
    translate(x, y) {
        val fin = Path().apply {
            moveTo(-r * 0.45f, 0f)
            quadraticTo(-r * 1.4f, -tail, -r * 1.5f, -tail)
            quadraticTo(-r * 1.2f, 0f, -r * 1.5f, tail)
            quadraticTo(-r * 1.4f, tail, -r * 0.45f, 0f)
            close()
        }
        drawPath(fin, color = tint.copy(alpha = 0.45f))
        val bodySize = Size(r * 1.75f, r * 0.88f)
        val origin = Offset(-r * 0.8f, -r * 0.44f)
        drawOval(
            brush = Brush.verticalGradient(listOf(Color(0xE5FCFFDA), tint.copy(alpha = 0.64f), Color(0xB40D789E)), origin.y, -origin.y),
            topLeft = origin,
            size = bodySize,
        )
        drawOval(Color.White.copy(alpha = 0.5f), origin, bodySize, style = Stroke(0.7.dp.toPx()))
        drawCircle(Color(0xE0105472), r * 0.095f, Offset(r * 0.53f, -r * 0.07f))
    }
}
