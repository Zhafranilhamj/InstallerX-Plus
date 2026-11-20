// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.page.main.installer.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.rosan.installer.domain.engine.model.install.InstallPhase
import com.rosan.installer.domain.settings.model.preferences.theme.UiStyle
import com.rosan.installer.ui.page.main.installer.InstallerStage
import com.rosan.installer.ui.theme.InstallerTheme
import com.rosan.installer.ui.theme.installerMaterial3BlurEffect
import com.rosan.installer.ui.theme.installerMiuixBlurEffect
import com.rosan.installer.ui.theme.rememberMaterial3BlurBackdrop
import com.rosan.installer.ui.theme.rememberMiuixBlurBackdrop
import kotlin.math.PI
import kotlin.math.sin
import top.yukonga.miuix.kmp.blur.layerBackdrop

// A single package has no measurable progress while Android verifies and commits it.
internal fun InstallerStage.Installing.measuredProgress(): Float? = progress.takeIf {
    it.isFinite() && it > 0f && (total > 1 || phase == InstallPhase.WRITING)
}?.coerceAtMost(1f)

@Composable
internal fun LiquidInstallGauge(style: UiStyle, progress: Float?, tint: Color, useBlur: Boolean, modifier: Modifier = Modifier) {
    val aero = style.isAero
    val mixed = style == UiStyle.MIXED
    val dark = InstallerTheme.isDark
    val water = if (aero) Color(if (dark) 0xFF1AAFA9 else 0xFF007DB8) else tint
    val shape = RoundedCornerShape(if (mixed) 6.dp else 24.dp)
    val backdrop = when (style) {
        UiStyle.MIXED -> rememberMaterial3BlurBackdrop(useBlur)
        UiStyle.LIQUID_GLASS -> rememberMiuixBlurBackdrop(useBlur)
        else -> null
    }
    val frost = when (style) {
        UiStyle.MIXED -> Modifier.installerMaterial3BlurEffect(backdrop, blurRadius = 12f, shape = shape)
        UiStyle.LIQUID_GLASS -> Modifier.installerMiuixBlurEffect(backdrop, blurRadius = 12f, shape = shape)
        else -> Modifier
    }
    val level by animateFloatAsState(progress ?: 0f, label = "Measured fill")
    val cycle by rememberInfiniteTransition(label = "Liquid").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2800, easing = LinearEasing)),
        label = "Surface current",
    )
    Box(
        modifier.fillMaxWidth().height(if (mixed) 18.dp else 26.dp)
            .then(if (progress == null) Modifier.progressSemantics() else Modifier.progressSemantics(progress))
            .clip(shape).border(1.dp, water.copy(alpha = 0.55f), shape),
    ) {
        // Record only the underlying tint field; the liquid and its sharp reflections sit above the frost.
        Box(
            Modifier.fillMaxSize().then(backdrop?.let { Modifier.layerBackdrop(it) } ?: Modifier)
                .background(Brush.horizontalGradient(listOf(water.copy(alpha = 0.24f), Color.Transparent, water.copy(alpha = 0.36f)))),
        )
        Canvas(Modifier.fillMaxSize().then(frost)) {
            val w = size.width
            val h = size.height
            val theta = 2 * PI * cycle
            val travel = w * 1.5f * cycle
            val start = if (progress == null) travel - w * 0.5f else -h
            val end = if (progress == null) travel else w * level
            scale(if (layoutDirection == LayoutDirection.Rtl) -1f else 1f, 1f) {
                val bend = sin(theta).toFloat() * minOf(h * 0.35f, end.coerceAtLeast(0f), (w - end).coerceAtLeast(0f))
                val fluid = Path().apply {
                    moveTo(start, 0f)
                    lineTo(end, 0f)
                    cubicTo(end + bend, h * 0.3f, end - bend, h * 0.7f, end, h)
                    lineTo(start, h)
                    close()
                }
                clipPath(fluid) {
                    drawRect(Brush.verticalGradient(listOf(water.copy(alpha = 0.72f), water, water.copy(alpha = 0.67f))))
                    if (aero) {
                        repeat(22) { i ->
                            val rise = (cycle + i * 0.319f) % 1f
                            val x = (i * 0.618f % 1f) * w + sin(theta + i).toFloat() * h * 0.05f
                            val radius = h * (0.06f + i % 3 * 0.025f)
                            val center = Offset(x, h + radius - (h + 2 * radius) * rise)
                            drawCircle(Color.White.copy(alpha = 0.7f), radius, center, style = Stroke(0.7.dp.toPx()))
                            drawCircle(Color.White, radius * 0.25f, center - Offset(radius * 0.4f, radius * 0.4f))
                        }
                    }
                }
                drawRect(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.55f), Color.Transparent), endY = h * 0.48f))
                drawRect(Color.White.copy(alpha = 0.28f), Offset(0f, h * 0.84f), Size(w, 1.dp.toPx()))
            }
        }
    }
}
