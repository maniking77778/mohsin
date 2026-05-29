package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActiveScreen
import com.example.data.AppViewModel
import com.example.localization.Loc
import com.example.ui.components.GlassyCard
import com.example.ui.components.GlassyTextField
import com.example.ui.components.PremiumGradientButton
import com.example.ui.components.SolidCard
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal

@Composable
fun QrScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.appLanguage.collectAsState()

    var qrTextInput by remember { mutableStateOf("INV-2026-401") }
    var mockScannedData by remember { mutableStateOf<String?>(value = null) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scannerLineOffset by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Appbar Back
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { viewModel.activeScreen.value = ActiveScreen.DASHBOARD }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Loc.tr("qr_system", lang),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Generator Box
        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.QrCode, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = Loc.tr("qr_generator", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Text(text = Loc.tr("qr_desc", lang), fontSize = 11.sp, color = Color(0xFF94A3B8))

                    GlassyTextField(
                        value = qrTextInput,
                        onValueChange = { qrTextInput = it },
                        label = "Type Device data or Bill ID (e.g. IMEI)",
                        leadingIcon = Icons.Default.Info
                    )

                    // Draw the QR Code directly on Canvas
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .size(140.dp)
                                .background(Color.White, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            val qrSize = size.width
                            val step = qrSize / 15f // 15x15 matrix

                            // Seed a deterministic matrix pattern depending on text input string hash
                            val seed = qrTextInput.hashCode()

                            // Draw three classic Corner finder squares in QR codes
                            // Top Left Finder
                            drawQrFinderPattern(0f, 0f, step)
                            // Top Right Finder
                            drawQrFinderPattern(qrSize - step * 5f, 0f, step)
                            // Bottom Left Finder
                            drawQrFinderPattern(0f, qrSize - step * 5f, step)

                            // Helper pixel data fill
                            for (row in 0 until 15) {
                                for (col in 0 until 15) {
                                    // Skip corner finder cells
                                    if (row < 5 && col < 5) continue
                                    if (row < 5 && col >= 10) continue
                                    if (row >= 10 && col < 5) continue

                                    // Render pseudo-data pixels based on string hash bit-patterns
                                    val factor = (row * 37 + col * 19) + seed.coerceAtLeast(1)
                                    if (factor % 2 == 0) {
                                        drawRect(
                                            color = Color.Black,
                                            topLeft = Offset(col * step, row * step),
                                            size = Size(step, step)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Scanner Simulation Box
        item {
            SolidCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = Loc.tr("qr_camera", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    // Simulated Camera frame view finder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Drawing corner focus lines and red laser scan animation Line
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Finder rectangle borders
                            val fw = w * 0.7f
                            val fh = h * 0.7f
                            val tx = (w - fw) / 2
                            val ty = (h - fh) / 2

                            drawRect(
                                color = Color(0x333B82F6),
                                topLeft = Offset(tx, ty),
                                size = Size(fw, fh)
                            )

                            // Red laser vertical scan line
                            val laserY = ty + (fh * scannerLineOffset)
                            drawLine(
                                color = Color(0xFFEF4444),
                                start = Offset(tx, laserY),
                                end = Offset(tx + fw, laserY),
                                strokeWidth = 3.dp.toPx()
                            )
                        }

                        Text(
                            text = "[LIVER_SCANNER_READY]",
                            color = PrimaryBlue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }

                    PremiumGradientButton(
                        text = Loc.tr("scan_mock", lang),
                        icon = Icons.Default.QrCodeScanner,
                        onClick = {
                            mockScannedData = "Wiped S23 Ultra | IMEI: 358762104523918 | Ticket: INV-2026-401"
                        }
                    )

                    mockScannedData?.let { result ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SecondaryTeal.copy(alpha = 0.12f))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(text = Loc.tr("scanned_code", lang) + ":", fontSize = 11.sp, color = SecondaryTeal, fontWeight = FontWeight.Bold)
                                Text(text = result, fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Draw the corner locator blocks
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawQrFinderPattern(
    x: Float,
    y: Float,
    step: Float
) {
    // Large solid outline (5x5 steps)
    drawRect(
        color = Color.Black,
        topLeft = Offset(x, y),
        size = Size(step * 5f, step * 5f)
    )
    // White hollow core (3x3 steps)
    drawRect(
        color = Color.White,
        topLeft = Offset(x + step, y + step),
        size = Size(step * 3f, step * 3f)
    )
    // Solid black center dot (1x1 step)
    drawRect(
        color = Color.Black,
        topLeft = Offset(x + step * 2f, y + step * 2f),
        size = Size(step, step)
    )
}
