package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrightNeonGreen
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.TextPrimaryWhite
import com.example.ui.theme.TextSecondaryGray

@Composable
fun InstagramLogoIcon(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeWidth = w * 0.09f
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
            radius = w * 0.23f,
            center = center,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
        )

        // Top right flash dot
        drawCircle(
            color = Color.White,
            radius = strokeWidth * 0.7f,
            center = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.25f)
        )
    }
}

@Composable
fun InstagramFooter(
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val context = LocalContext.current
    val instagramUrl = "https://www.instagram.com/aminlgeek/"

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
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFE1306C),
                        BrightNeonGreen
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
                    context.startActivity(intent)
                } catch (_: Exception) {
                }
            }
            .testTag("instagram_profile_btn"),
        color = Color(0xF00A1F13),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (compact) 10.dp else 16.dp,
                vertical = if (compact) 5.dp else 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Instagram icon badge with authentic gradient background & vector glyph
            Box(
                modifier = Modifier
                    .size(if (compact) 22.dp else 26.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(instagramGradient),
                contentAlignment = Alignment.Center
            ) {
                InstagramLogoIcon(modifier = Modifier.size(if (compact) 13.dp else 16.dp))
            }

            Spacer(modifier = Modifier.width(6.dp))

            if (!compact) {
                Text(
                    text = "Instagram:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondaryGray
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = "@aminlgeek",
                fontSize = if (compact) 12.sp else 13.sp,
                fontWeight = FontWeight.Bold,
                color = BrightNeonGreen
            )
        }
    }
}
