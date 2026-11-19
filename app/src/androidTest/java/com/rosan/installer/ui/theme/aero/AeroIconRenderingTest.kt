// SPDX-License-Identifier: GPL-3.0-only
// Copyright (C) 2026 InstallerX Revived contributors
package com.rosan.installer.ui.theme.aero

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon as MaterialIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.math.abs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AeroIconRenderingTest {
    @get:Rule
    val compose = createComposeRule()

    private val backdrop = Color(0xFFE9F5E9)

    @Test
    fun glossyFinishChangesTheGlyphButNeverPaintsItsBackgroundOrHole() {
        val aero = mutableStateOf(false)
        compose.setContent {
            MaterialTheme(colorScheme = AeroColors) {
                CompositionLocalProvider(LocalFrutigerAero provides aero.value) {
                    Box(Modifier.background(backdrop)) {
                        InstallerIcon(Icons.Filled.Settings, "Settings", Modifier.size(64.dp).testTag("icon"))
                    }
                }
            }
        }
        val plain = capture("icon")
        compose.runOnIdle { aero.value = true }
        val glossy = capture("icon")
        assertTrue(difference(plain, glossy) > 10f)
        val pixels = glossy.toPixelMap()
        assertColorNear(backdrop, pixels[1, 1])
        assertColorNear(backdrop, pixels[pixels.width - 2, pixels.height - 2])
        assertColorNear(backdrop, pixels[pixels.width / 2, pixels.height / 2])
        compose.onNodeWithContentDescription("Settings").assertExists()
    }

    @Test
    fun disabledOpacityIsAppliedToTheWholeSculptureExactlyOnce() {
        val tint = mutableStateOf(Color(0xFFBE2525))
        compose.setContent {
            MaterialTheme(colorScheme = AeroColors) {
                CompositionLocalProvider(LocalFrutigerAero provides true) {
                    Box(Modifier.background(backdrop)) {
                        InstallerIcon(Icons.Filled.Favorite, null, Modifier.size(64.dp).testTag("icon"), tint.value)
                    }
                }
            }
        }
        val enabled = capture("icon")
        compose.runOnIdle { tint.value = tint.value.copy(alpha = 0.38f) }
        val disabled = capture("icon")
        val ratio = distanceFromBackdrop(disabled) / distanceFromBackdrop(enabled)
        assertTrue("Disabled opacity should remain 38%, not be lost or applied twice: $ratio", ratio in 0.33f..0.43f)
    }

    @Test
    fun otherStylesDelegateToTheUnchangedMaterialIcon() {
        compose.setContent {
            MaterialTheme(colorScheme = AeroColors) {
                CompositionLocalProvider(LocalFrutigerAero provides false) {
                    Column(Modifier.background(backdrop)) {
                        InstallerIcon(Icons.Filled.Settings, null, Modifier.size(48.dp).testTag("wrapper"), Color.Red)
                        MaterialIcon(Icons.Filled.Settings, null, Modifier.size(48.dp).testTag("material"), Color.Red)
                    }
                }
            }
        }
        assertEquals(0f, difference(capture("wrapper"), capture("material")), 0.001f)
    }

    @Test
    fun autoMirroredVectorsStillReverseTheirSilhouetteInRtl() {
        val direction = mutableStateOf(LayoutDirection.Ltr)
        compose.setContent {
            MaterialTheme(colorScheme = AeroDarkColors) {
                CompositionLocalProvider(LocalFrutigerAero provides true, LocalLayoutDirection provides direction.value) {
                    Box(Modifier.background(backdrop)) {
                        InstallerIcon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", Modifier.size(64.dp).testTag("icon"))
                    }
                }
            }
        }
        val left = capture("icon").toPixelMap()
        compose.runOnIdle { direction.value = LayoutDirection.Rtl }
        val right = capture("icon").toPixelMap()
        // Compare silhouette rather than lighting: the light source deliberately stays top-left.
        var mismatch = 0
        var glyphPixels = 0
        for (y in 0 until left.height) {
            for (x in 0 until left.width) {
                val l = colorDistance(left[x, y], backdrop) > 0.12f
                val r = colorDistance(right[right.width - x - 1, y], backdrop) > 0.12f
                if (l || r) glyphPixels++
                if (l != r) mismatch++
            }
        }
        assertTrue("RTL should mirror the source vector", mismatch.toFloat() / glyphPixels < 0.15f)
        compose.onNodeWithContentDescription("Back").assertExists()
    }

    @Test
    fun bitmapAppIconsKeepTheirOriginalColorsUnderTheClearCoat() {
        val bitmap = android.graphics.Bitmap.createBitmap(32, 32, android.graphics.Bitmap.Config.ARGB_8888)
        for (y in 0 until 32) {
            for (x in 0 until 32) bitmap.setPixel(x, y, if (x < 16) android.graphics.Color.RED else android.graphics.Color.BLUE)
        }
        val artwork = bitmap.asImageBitmap()
        compose.setContent {
            MaterialTheme(colorScheme = AeroColors) {
                CompositionLocalProvider(LocalFrutigerAero provides true) {
                    InstallerIconImage(artwork, "App", Modifier.size(64.dp).testTag("icon"))
                }
            }
        }
        val pixels = capture("icon").toPixelMap()
        val red = pixels[pixels.width / 4, pixels.height / 2]
        val blue = pixels[pixels.width * 3 / 4, pixels.height / 2]
        assertTrue(red.red > red.blue + 0.4f)
        assertTrue(blue.blue > blue.red + 0.4f)
    }

    private fun capture(tag: String): ImageBitmap = compose.onNodeWithTag(tag).captureToImage()

    private fun difference(first: ImageBitmap, second: ImageBitmap): Float {
        assertEquals(first.width, second.width)
        assertEquals(first.height, second.height)
        val a = first.toPixelMap()
        val b = second.toPixelMap()
        var sum = 0f
        for (y in 0 until a.height) {
            for (x in 0 until a.width) sum += colorDistance(a[x, y], b[x, y])
        }
        return sum
    }

    private fun distanceFromBackdrop(image: ImageBitmap): Float {
        val pixels: PixelMap = image.toPixelMap()
        var sum = 0f
        for (y in 0 until pixels.height) {
            for (x in 0 until pixels.width) sum += colorDistance(pixels[x, y], backdrop)
        }
        return sum
    }

    private fun colorDistance(a: Color, b: Color): Float = abs(a.red - b.red) + abs(a.green - b.green) + abs(a.blue - b.blue)

    private fun assertColorNear(expected: Color, actual: Color) {
        assertTrue("Expected $expected, got $actual", colorDistance(expected, actual) < 0.025f)
    }
}
