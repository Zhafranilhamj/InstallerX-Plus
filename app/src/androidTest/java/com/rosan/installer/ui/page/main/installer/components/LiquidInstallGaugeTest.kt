// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.page.main.installer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.rosan.installer.domain.settings.model.preferences.theme.UiStyle
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LiquidInstallGaugeTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun halfwayFillIsVisibleAndCommitDoesNotAnnounceAFalsePercentage() {
        val value = mutableStateOf<Float?>(0.5f)
        val style = mutableStateOf(UiStyle.FRUTIGER_AERO)
        compose.setContent {
            Box(Modifier.width(200.dp).background(Color.White)) {
                LiquidInstallGauge(style.value, value.value, Color.Blue, false, Modifier.testTag("gauge"))
            }
        }
        val node = compose.onNodeWithTag("gauge")
        for (preset in listOf(UiStyle.FRUTIGER_AERO, UiStyle.LIQUID_GLASS, UiStyle.MIXED)) {
            compose.runOnIdle { style.value = preset }
            node.assert(SemanticsMatcher.expectValue(SemanticsProperties.ProgressBarRangeInfo, ProgressBarRangeInfo(0.5f, 0f..1f)))
            val pixels = node.captureToImage().toPixelMap()

            // Compare broad samples, so a passing bubble or pixel rounding cannot hide the fill.
            fun meanRed(start: Int) = (start until start + pixels.width / 6).map { pixels[it, pixels.height / 2].red }.average()
            assertTrue("Filled side must be visibly tinted in $preset", meanRed(pixels.width / 6) + 0.15 < meanRed(pixels.width * 2 / 3))
        }
        compose.runOnIdle { value.value = null }
        node.assert(SemanticsMatcher.expectValue(SemanticsProperties.ProgressBarRangeInfo, ProgressBarRangeInfo.Indeterminate))
    }
}
