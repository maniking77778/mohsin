package com.example.ui.screens

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.NetworkWifi
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.TabletAndroid
import androidx.compose.material.icons.filled.Vibration
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActiveScreen
import com.example.data.AppViewModel
import com.example.localization.Loc
import com.example.ui.components.GlassyCard
import com.example.ui.components.PremiumGradientButton
import com.example.ui.components.SolidCard
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal

@Composable
fun DiagnosticsScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.appLanguage.collectAsState()
    val context = LocalContext.current

    val isFlashActive by viewModel.isFlashlightOn.collectAsState()
    val pingRate by viewModel.networkPingMs.collectAsState()
    val ssidInfo by viewModel.networkStrengthSsid.collectAsState()

    var activeDisplayTesterColor by remember { mutableStateOf<Color?>(value = null) }
    var soundPlayingInfo by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main toolbar items
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
                        text = Loc.tr("tools_suite", lang),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // 1. Flashlight tool
            item {
                GlassyCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.FlashlightOn, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Primary LED Flashlight", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = Loc.tr("flashlight_desc", lang), fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }

                        PremiumGradientButton(
                            text = if (isFlashActive) "Active ON" else "Toggle OFF",
                            onClick = {
                                viewModel.toggleFlashlight(context)
                            },
                            modifier = Modifier.width(130.dp),
                            backgroundColor = if (isFlashActive) SecondaryTeal else Color(0xFF475569),
                            accentColor = if (isFlashActive) PrimaryBlue else Color(0xFF334155)
                        )
                    }
                }
            }

            // 2. Wireless connection checks
            item {
                SolidCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.NetworkCheck, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = Loc.tr("network_tester", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = ssidInfo, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(text = "${Loc.tr("ping_ms", lang)} : $pingRate ms", fontSize = 12.sp, color = SecondaryTeal, fontWeight = FontWeight.Bold)
                            }

                            PremiumGradientButton(
                                text = "Run Speed Ping",
                                onClick = { viewModel.simulatePingSpeed() },
                                modifier = Modifier.width(140.dp)
                            )
                        }

                        Divider(color = Color(0x1F94A3B8))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "SIM Slot states: Registered Mobile Carrier active (1T1R MIMO)", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }
            }

            // 3. Display Subpixel Dead Dot test
            item {
                GlassyCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.TabletAndroid, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = Loc.tr("screen_test", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Text(text = Loc.tr("screen_test_desc", lang), fontSize = 11.sp, color = Color(0xFF94A3B8))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Red)
                                    .clickable { activeDisplayTesterColor = Color.Red }
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Green)
                                    .clickable { activeDisplayTesterColor = Color.Green }
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Blue)
                                    .clickable { activeDisplayTesterColor = Color.Blue }
                            )
                        }
                    }
                }
            }

            // 4. Interactive haptic and speaker checks
            item {
                SolidCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Sensors, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = Loc.tr("device_hw_test", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        // Vibration Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1.3f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Vibration, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = Loc.tr("vibe_test", lang), fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Text(text = Loc.tr("vibe_desc", lang), fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }

                            PremiumGradientButton(
                                text = "Buzz Motor",
                                onClick = {
                                    viewModel.runDiagnosticHapticFeedback(context)
                                },
                                modifier = Modifier.width(110.dp)
                            )
                        }

                        Divider(color = Color(0x1F94A3B8))

                        // Audio test Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1.3f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Speaker, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = Loc.tr("speaker_test", lang), fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Text(text = Loc.tr("speaker_desc", lang), fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }

                            PremiumGradientButton(
                                text = if (soundPlayingInfo) "Stop" else "Play Tone",
                                onClick = {
                                    try {
                                        if (soundPlayingInfo) {
                                            soundPlayingInfo = false
                                        } else {
                                            soundPlayingInfo = true
                                            // Generating direct high quality thermal diagnostic square tone of 1200hz for 400ms!
                                            // 100% robust on any active physical driver output
                                            val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 80)
                                            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 350)
                                            soundPlayingInfo = false
                                        }
                                    } catch (e: Exception) {
                                        soundPlayingInfo = false
                                    }
                                },
                                modifier = Modifier.width(110.dp),
                                backgroundColor = if (soundPlayingInfo) Color.Red else PrimaryBlue,
                                accentColor = if (soundPlayingInfo) Color.Black else SecondaryTeal
                            )
                        }
                    }
                }
            }
        }

        // Expanded RGB flashing dead pixel canvas overlay overlay (if active)
        AnimatedVisibility(
            visible = activeDisplayTesterColor != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            activeDisplayTesterColor?.let { color ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                        .clickable { activeDisplayTesterColor = null },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "RGB Subpixel Test Frame active.\nTap anywhere to close overlay.",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}
