package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.AppLanguage
import com.example.localization.LanguageManager
import com.example.ui.theme.AppTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EmbeddedYouTubePlayer(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val html = remember(videoId) {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                html, body {
                    width: 100%;
                    height: 100%;
                    background-color: #000000;
                    overflow: hidden;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                }
                .video-box {
                    position: relative;
                    width: 100%;
                    height: 100%;
                }
                iframe {
                    position: absolute;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    border: 0;
                }
            </style>
        </head>
        <body>
            <div class="video-box">
                <iframe
                    src="https://www.youtube-nocookie.com/embed/$videoId?autoplay=0&playsinline=1&rel=0&modestbranding=1&enablejsapi=1"
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                    allowfullscreen>
                </iframe>
            </div>
        </body>
        </html>
        """.trimIndent()
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                try {
                    setLayerType(View.LAYER_TYPE_HARDWARE, null)
                } catch (_: Exception) {
                    setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                }
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    mediaPlaybackRequiresUserGesture = false
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()
                setBackgroundColor(android.graphics.Color.BLACK)
                loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null)
            }
        },
        update = { webView ->
            try {
                webView.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null)
            } catch (_: Exception) {}
        },
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFFF3333).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseTutorialBottomSheet(
    tutorial: ExerciseTutorialDetail,
    lang: AppLanguage,
    onDismiss: () -> Unit
) {
    val colors = AppTheme.colors
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = colors.background,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = colors.primaryAccent.copy(alpha = 0.6f)
            )
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tutorial.exerciseName,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = colors.primaryAccent.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = tutorial.muscleTarget,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.primaryAccent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        if (tutorial.setsAndReps.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = colors.cardBorder
                            ) {
                                Text(
                                    text = tutorial.setsAndReps,
                                    fontSize = 11.sp,
                                    color = colors.textSecondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.cardBorder)
                        .testTag("close_tutorial_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = LanguageManager.closeTutorialBtn(lang),
                        tint = colors.textPrimary
                    )
                }
            }

            // In-App Embedded YouTube Video Player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.Black)
            ) {
                EmbeddedYouTubePlayer(
                    videoId = tutorial.videoId,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Direct YouTube App launcher button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, Color(0xFFFF3333).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .clickable {
                        try {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/watch?v=${tutorial.videoId}")
                            )
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                    .testTag("open_external_youtube_button"),
                color = Color(0x22FF0000)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartDisplay,
                        contentDescription = "YouTube",
                        tint = Color(0xFFFF4444),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = LanguageManager.openInYoutube(lang),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF4444)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Subheader: Form instructions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = colors.primaryAccent,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = LanguageManager.tutorialModalTitle(lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.primaryAccent
                )
            }

            // Bullet Point Instruction Cards
            InstructionBulletCard(
                icon = Icons.Default.AccessibilityNew,
                title = LanguageManager.setupPositionLabel(lang),
                description = tutorial.setupTip,
                accentColor = Color(0xFF00E5FF)
            )

            Spacer(modifier = Modifier.height(10.dp))

            InstructionBulletCard(
                icon = Icons.Default.FrontHand,
                title = LanguageManager.gripLabel(lang),
                description = tutorial.gripTip,
                accentColor = Color(0xFFFFB300)
            )

            Spacer(modifier = Modifier.height(10.dp))

            InstructionBulletCard(
                icon = Icons.Default.Sync,
                title = LanguageManager.executionLabel(lang),
                description = tutorial.executionTip,
                accentColor = colors.primaryAccent
            )

            Spacer(modifier = Modifier.height(10.dp))

            InstructionBulletCard(
                icon = Icons.Default.Air,
                title = LanguageManager.breathingSafetyLabel(lang),
                description = tutorial.breathingTip,
                accentColor = Color(0xFFFF5252)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Close & Back Button
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .testTag("dismiss_tutorial_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primaryAccent,
                    contentColor = colors.background
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = LanguageManager.closeTutorialBtn(lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InstructionBulletCard(
    icon: ImageVector,
    title: String,
    description: String,
    accentColor: Color
) {
    val colors = AppTheme.colors
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
        color = colors.cardBackgroundOpaque
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• $description",
                    fontSize = 13.sp,
                    color = colors.textPrimary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
