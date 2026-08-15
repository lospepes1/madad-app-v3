package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun openInstagramProfile(context: Context) {
    val username = "aminlgeek"
    val appUri = Uri.parse("http://instagram.com/_u/$username")
    val webUri = Uri.parse("https://www.instagram.com/$username/")

    val appIntent = Intent(Intent.ACTION_VIEW, appUri).apply {
        setPackage("com.instagram.android")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    try {
        context.startActivity(appIntent)
    } catch (_: Exception) {
        val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(webIntent)
        } catch (_: Exception) {}
    }
}

@Composable
fun InstagramLogoIcon(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeWidth = w * 0.11f
        val cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.28f, h * 0.28f)

        // Outer rounded rectangle
        drawRoundRect(
            color = Color.White,
            topLeft = androidx.compose.ui.geometry.Offset(strokeWidth / 2, strokeWidth / 2),
            size = androidx.compose.ui.geometry.Size(w - strokeWidth, h - strokeWidth),
            cornerRadius = cornerRadius,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
        )

        // Center lens circle
        drawCircle(
            color = Color.White,
            radius = w * 0.22f,
            center = center,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
        )

        // Top right flash dot
        drawCircle(
            color = Color.White,
            radius = strokeWidth * 0.7f,
            center = androidx.compose.ui.geometry.Offset(w * 0.74f, h * 0.26f)
        )
    }
}

/**
 * Minimal, clean Instagram icon button that uses deep linking to open the Instagram app
 * with fallback to the web browser.
 */
@Composable
fun InstagramIconButton(
    modifier: Modifier = Modifier,
    size: Dp = 38.dp,
    iconSize: Dp = 20.dp
) {
    val context = LocalContext.current

    val instagramGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF833AB4), // Purple
            Color(0xFFE1306C), // Pink / Rose
            Color(0xFFFD1D1D), // Red
            Color(0xFFF77737), // Orange
            Color(0xFFFCB045)  // Yellow
        )
    )

    Surface(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(Color(0xFFE1306C).copy(alpha = 0.6f), Color(0xFFFCB045).copy(alpha = 0.6f))
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                openInstagramProfile(context)
            }
            .testTag("instagram_profile_btn"),
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(instagramGradient),
            contentAlignment = Alignment.Center
        ) {
            InstagramLogoIcon(modifier = Modifier.size(iconSize))
        }
    }
}

/**
 * Backward compatibility alias so existing calls render the single minimal clean icon button.
 */
@Composable
fun InstagramFooter(
    modifier: Modifier = Modifier,
    compact: Boolean = true
) {
    InstagramIconButton(modifier = modifier)
}
