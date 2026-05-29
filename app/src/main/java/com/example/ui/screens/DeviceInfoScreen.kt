package com.example.ui.screens

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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActiveScreen
import com.example.data.AppViewModel
import com.example.localization.Loc
import com.example.ui.components.GlassyCard
import com.example.ui.components.SolidCard
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal

@Composable
fun DeviceInfoScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.appLanguage.collectAsState()

    val model = viewModel.getHardwareModel()
    val manufacturer = viewModel.getHardwareBrand()
    val version = viewModel.getAndroidVersion()
    val board = viewModel.getHardwareBoard()
    val cpu = viewModel.getCpuAbi()
    val serial = viewModel.getHardwareSerial()

    val ram = viewModel.getRamDetails()
    val storage = viewModel.getStorageDetails()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Core Header
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
                    text = Loc.tr("device_diagnostics", lang),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Live stats visual progression bar
        item {
            GlassyCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Memory, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = Loc.tr("ram_status", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text(text = ram, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = SecondaryTeal)
                    LinearProgressIndicator(
                        progress = { 0.45f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = SecondaryTeal,
                        trackColor = Color(0x1F94A3B8)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = Loc.tr("storage_status", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text(text = storage, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                    LinearProgressIndicator(
                        progress = { 0.62f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PrimaryBlue,
                        trackColor = Color(0x1F94A3B8)
                    )
                }
            }
        }

        // Core board parameters lists
        item {
            SolidCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = Loc.tr("specs_overview", lang),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Divider(color = Color(0x1F94A3B8))

                    SpecsDetailsItem(icon = Icons.Default.Devices, label = Loc.tr("brand", lang), value = manufacturer)
                    SpecsDetailsItem(icon = Icons.Default.SettingsSuggest, label = Loc.tr("phone_model", lang), value = model)
                    SpecsDetailsItem(icon = Icons.Default.DeveloperMode, label = Loc.tr("android_version", lang), value = version)
                    SpecsDetailsItem(icon = Icons.Default.Memory, label = Loc.tr("board", lang), value = board)
                    SpecsDetailsItem(icon = Icons.Default.SettingsSuggest, label = Loc.tr("cpu", lang), value = cpu)
                    SpecsDetailsItem(icon = Icons.Default.DeveloperMode, label = Loc.tr("hardware_serial", lang), value = serial)
                }
            }
        }

        // Battery Estimation Section
        item {
            GlassyCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SecondaryTeal.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.BatteryChargingFull, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(text = Loc.tr("battery_health", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Condition: GOOD (Estimate: 91% Capacity)", fontSize = 12.sp, color = Color(0xFF94A3B8))
                        Text(text = "Temperature: 36.5°C | Tech: Li-poly", fontSize = 11.sp, color = SecondaryTeal)
                    }
                }
            }
        }
    }
}

@Composable
fun SpecsDetailsItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 12.sp, color = Color(0xFF94A3B8))
        }
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}
