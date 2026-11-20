// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.rosan.installer.domain.settings.model.preferences.theme.UiStyle
import com.rosan.installer.ui.theme.InstallerTheme
import com.rosan.installer.ui.theme.LocalUiStyle
import kotlinx.coroutines.flow.collectLatest

internal val AquaGelInk = Color(0xFF073D60)

private val DeepGel = Brush.verticalGradient(
    0f to Color(0xFF216C8D),
    0.45f to Color(0xFF146080),
    0.48f to Color(0xFF00456C),
    1f to Color(0xFF147A86),
)
private val LightGel = Brush.verticalGradient(
    0f to Color.White,
    0.44f to Color(0xFFCEF5FF),
    0.47f to Color(0xFF9CDEF6),
    1f to Color(0xFFD0F5CF),
)

@Composable
private fun Modifier.aeroGel(activated: Boolean, source: MutableInteractionSource, filled: Boolean): Modifier {
    if (!activated) return this
    val style = LocalUiStyle.current
    val accent = if (InstallerTheme.isDark) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary
    val variantGel = if (style == UiStyle.FRUTIGER_METRO) {
        Brush.verticalGradient(listOf(accent, accent))
    } else {
        Brush.verticalGradient(listOf(lerp(accent, Color.White, 0.18f), accent, lerp(accent, Color.Black, 0.2f)))
    }
    val pressed by source.collectIsPressedAsState()
    val hovered by source.collectIsHoveredAsState()
    val scale = animateFloatAsState(
        targetValue = if (pressed) 0.955f else 1f,
        animationSpec = spring(dampingRatio = 0.56f, stiffness = Spring.StiffnessMediumLow),
        label = "Gel press",
    )
    val shine = remember { Animatable(1f) }
    val burst = remember { Animatable(1f) }
    LaunchedEffect(source) {
        source.interactions.collectLatest { interaction ->
            if (interaction is PressInteraction.R) {
                burst.snapTo(0f)
                burst.animateTo(1f, tween(520))
            }
        }
    }
    LaunchedEffect(hovered) {
        if (hovered) {
            shine.snapTo(0f)
            shine.animateTo(1f, tween(680))
        } else {
            shine.snapTo(1f)
        }
    }
    return graphicsLayer {
        scaleX = 1f - (1f - scale.value) / 2f
        scaleY = scale.value
    }.clip(CircleShape).drawWithCache {
        val sheen = Brush.horizontalGradient(
            listOf(Color.Transparent, Color(0x3DFFFFFF), Color.Transparent),
            endX = size.height * 1.8f,
        )
        val rim = CornerRadius(size.minDimension / 2f)
        val stroke = 1.dp.toPx()
        onDrawWithContent {
            drawRect(
                if (!filled) {
                    LightGel
                } else if (style == UiStyle.FRUTIGER_AERO) {
                    DeepGel
                } else {
                    variantGel
                },
            )
            drawRoundRect(Color(0xA1FFFFFF), cornerRadius = rim, style = Stroke(2 * stroke))
            if (shine.value < 1f) {
                val width = size.height * 1.8f
                val x = (size.width + width) * shine.value - width
                translate(left = x) { drawRect(sheen, size = size.copy(width = width)) }
            }
            val t = burst.value
            if (t < 1f) {
                val opacity = 1f - t
                drawCircle(Color.White.copy(alpha = opacity * 0.5f), size.height * t, center, style = Stroke(stroke))
                repeat(5) { i ->
                    val x = size.width * (0.18f + i * 0.16f)
                    val y = size.height * (0.85f - t * (0.55f + i % 2 * 0.28f))
                    drawAeroBubble(Offset(x, y), (3 + i % 3).dp.toPx(), opacity)
                }
            }
            drawContent()
        }
    }
}

@Composable
internal fun InstallerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val aero = LocalFrutigerAero.current
    val source = interactionSource ?: if (aero) remember { MutableInteractionSource() } else null
    Button(
        onClick = onClick,
        modifier = if (source != null) modifier.aeroGel(aero && enabled, source, filled = true) else modifier,
        enabled = enabled,
        shape = if (aero) CircleShape else shape,
        colors = if (aero) colors.copy(containerColor = Color.Transparent, contentColor = Color.White) else colors,
        elevation = if (aero) null else elevation,
        border = if (aero) null else border,
        contentPadding = contentPadding,
        interactionSource = source,
        content = content,
    )
}

@Composable
internal fun InstallerTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.textShape,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val aero = LocalFrutigerAero.current
    val source = interactionSource ?: if (aero) remember { MutableInteractionSource() } else null
    TextButton(
        onClick = onClick,
        modifier = if (source != null) modifier.aeroGel(aero && enabled, source, filled = false) else modifier,
        enabled = enabled,
        shape = if (aero) CircleShape else shape,
        colors = if (aero) colors.copy(containerColor = Color.Transparent, contentColor = AquaGelInk) else colors,
        elevation = if (aero) null else elevation,
        border = if (aero) null else border,
        contentPadding = contentPadding,
        interactionSource = source,
        content = content,
    )
}

@Composable
internal fun InstallerOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(enabled),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val aero = LocalFrutigerAero.current
    val source = interactionSource ?: if (aero) remember { MutableInteractionSource() } else null
    OutlinedButton(
        onClick = onClick,
        modifier = if (source != null) modifier.aeroGel(aero && enabled, source, filled = false) else modifier,
        enabled = enabled,
        shape = if (aero) CircleShape else shape,
        colors = if (aero) colors.copy(containerColor = Color.Transparent, contentColor = AquaGelInk) else colors,
        elevation = if (aero) null else elevation,
        border = if (aero && enabled) BorderStroke(1.dp, Color(0xFF73B5C9)) else border,
        contentPadding = contentPadding,
        interactionSource = source,
        content = content,
    )
}
