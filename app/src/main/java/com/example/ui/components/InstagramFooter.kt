package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
        val strokeWidth = w * 0.12f
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
            radius = strokeWidth * 0.65f,
            center = androidx.compose.ui.geometry.Offset(w * 0.74f, h * 0.26f)
        )
    }
}

/**
 * Compact, borderless minimal Instagram icon that opens the Instagram profile: https://www.instagram.com/aminlgeek/
 */
@Composable
fun InstagramIconButton(
    modifier: Modifier = Modifier,
    size: Dp = 26.dp,
    iconSize: Dp = 15.dp
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

    Box(
        modifier = modifier
            .size(34.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 18.dp),
                onClick = { openInstagramProfile(context) }
            )
            .testTag("instagram_profile_btn"),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(7.dp))
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
