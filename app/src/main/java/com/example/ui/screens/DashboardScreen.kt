package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActiveScreen
import com.example.data.AppViewModel
import com.example.data.RepairRecord
import com.example.localization.AppLanguage
import com.example.localization.Loc
import com.example.ui.components.AnimatedStatusIndicator
import com.example.ui.components.GlassyCard
import com.example.ui.components.SolidCard
import com.example.ui.theme.CardBlue
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal
import java.text.DecimalFormat

@Composable
fun DashboardScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.appLanguage.collectAsState()
    val totalEarnings by viewModel.dashboardTotalEarnings.collectAsState()
    val activeJobsCount by viewModel.dashboardActiveJobsCount.collectAsState()
    val totalClients by viewModel.dashboardTotalClientsCount.collectAsState()
    val recentRepairs by viewModel.repairsList.collectAsState()
    val isLoggedIn by viewModel.isAdminLoggedIn.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome and Login Banner
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = Loc.tr("dashboard", lang),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isLoggedIn) Loc.tr("logged_in_as", lang) else Loc.tr("restricted_access", lang),
                        fontSize = 13.sp,
                        color = if (isLoggedIn) SecondaryTeal else Color(0xFFF59E0B)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isLoggedIn) SecondaryTeal.copy(alpha = 0.2f) else Color(0x1F94A3B8))
                        .clickable {
                            if (isLoggedIn) viewModel.logout()
                            else viewModel.activeScreen.value = ActiveScreen.SETTINGS
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLoggedIn) Icons.Default.VpnKey else Icons.Default.VpnKey,
                        contentDescription = "Login Status",
                        tint = if (isLoggedIn) SecondaryTeal else Color.White
                    )
                }
            }
        }

        // Stats Row - Earnings (Glassmorphism), Active Jobs, Registers
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Earnings Card
                GlassyCard(
                    modifier = Modifier.weight(1.1f)
                ) {
                    Column(modifier = Modifier.padding(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = SecondaryTeal,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = Loc.tr("earnings", lang),
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "PKR " + DecimalFormat("##,###").format(totalEarnings),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Active Jobs Card
                SolidCard(
                    modifier = Modifier.weight(0.9f)
                ) {
                    Column(modifier = Modifier.padding(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AnimatedStatusIndicator(color = Color(0xFFF59E0B), size = 8.dp)
                            Text(
                                text = Loc.tr("active_jobs", lang),
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$activeJobsCount " + Loc.tr("repairs", lang),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Clients Card
                SolidCard(
                    modifier = Modifier.weight(0.9f)
                ) {
                    Column(modifier = Modifier.padding(2.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = Loc.tr("customers", lang),
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$totalClients Users",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Custom drawn Canvas graphical report
        item {
            GlassyCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = SecondaryTeal,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = Loc.tr("revenue_breakdown", lang),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "Live Trend",
                            fontSize = 12.sp,
                            color = SecondaryTeal,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Draw an organic earning curve representing the shop trajectory
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        val width = size.width
                        val height = size.height

                        val points = listOf(
                            Offset(0f, height * 0.9f),
                            Offset(width * 0.15f, height * 0.75f),
                            Offset(width * 0.35f, height * 0.8f),
                            Offset(width * 0.55f, height * 0.45f),
                            Offset(width * 0.75f, height * 0.60f),
                            Offset(width * 1f, height * 0.20f)
                        )

                        val path = Path().apply {
                            moveTo(points[0].x, points[0].y)
                            for (i in 1 until points.size) {
                                val pre = points[i - 1]
                                val cur = points[i]
                                cubicTo(
                                    (pre.x + cur.x) / 2, pre.y,
                                    (pre.x + cur.x) / 2, cur.y,
                                    cur.x, cur.y
                                )
                            }
                        }

                        // Fill under the line
                        val fillPath = Path().apply {
                            addPath(path)
                            lineTo(width, height)
                            lineTo(0f, height)
                            close()
                        }

                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(PrimaryBlue.copy(alpha = 0.3f), Color.Transparent)
                            )
                        )

                        // Outer stroke
                        drawPath(
                            path = path,
                            color = SecondaryTeal,
                            style = Stroke(width = 3.dp.toPx())
                        )

                        // Highlight current vertex
                        drawCircle(
                            color = Color.White,
                            radius = 5.dp.toPx(),
                            center = points.last()
                        )
                        drawCircle(
                            color = SecondaryTeal,
                            radius = 3.dp.toPx(),
                            center = points.last()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Mon", fontSize = 10.sp, color = Color(0xFF64748B))
                        Text("Tue", fontSize = 10.sp, color = Color(0xFF64748B))
                        Text("Wed", fontSize = 10.sp, color = Color(0xFF64748B))
                        Text("Thu", fontSize = 10.sp, color = Color(0xFF64748B))
                        Text("Fri", fontSize = 10.sp, color = Color(0xFF64748B))
                        Text("Today", fontSize = 10.sp, color = SecondaryTeal, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Feature Quick Actions Shortcuts
        item {
            Text(
                text = Loc.tr("tools_suite", lang),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickButton(
                        text = Loc.tr("imei_system_title", lang),
                        icon = Icons.Default.Analytics,
                        color = PrimaryBlue,
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.activeScreen.value = ActiveScreen.IMEI_SYSTEM
                    }

                    QuickButton(
                        text = Loc.tr("pta_tax_checker", lang),
                        icon = Icons.Default.Receipt,
                        color = SecondaryTeal,
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.activeScreen.value = ActiveScreen.PTA_SYSTEM
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickButton(
                        text = Loc.tr("device_diagnostics", lang),
                        icon = Icons.Default.Devices,
                        color = Color(0xFF9333EA),
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.activeScreen.value = ActiveScreen.DEVICE_INFO
                    }

                    QuickButton(
                        text = Loc.tr("qr_system", lang),
                        icon = Icons.Default.QrCodeScanner,
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.activeScreen.value = ActiveScreen.QR_BARCODE
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickButton(
                        text = Loc.tr("market_index", lang),
                        icon = Icons.Default.Landscape,
                        color = Color(0xFFE11D48),
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.activeScreen.value = ActiveScreen.PRICE_MARKET
                    }

                    QuickButton(
                        text = Loc.tr("device_hw_test", lang),
                        icon = Icons.Default.NetworkCheck,
                        color = Color(0xFF0D9488),
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.activeScreen.value = ActiveScreen.DIAGNOSTICS
                    }
                }
            }
        }

        // Active Repair list in shop
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Loc.tr("visit_history", lang),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = Loc.tr("view_all", lang),
                    fontSize = 13.sp,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        viewModel.activeScreen.value = ActiveScreen.REPAIR_RECORD_LIST
                    }
                )
            }
        }

        if (recentRepairs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Repair records in progress", color = Color(0xFF64748B), fontSize = 13.sp)
                }
            }
        } else {
            items(recentRepairs.take(3)) { repair ->
                RecentRepairItem(repair = repair, lang = lang) {
                    viewModel.currentSelectedRepair.value = repair
                    viewModel.activeScreen.value = ActiveScreen.REPAIR_RECORD_LIST
                }
            }
        }
    }
}

@Composable
fun QuickButton(
    text: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CardBlue)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RecentRepairItem(
    repair: RepairRecord,
    lang: AppLanguage,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBlue)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = repair.customerName + " - " + repair.deviceModel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = repair.faultType,
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PKR " + DecimalFormat("##,###").format(repair.repairCost),
                        fontSize = 12.sp,
                        color = SecondaryTeal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        when (repair.status) {
                            "Completed" -> SecondaryTeal.copy(alpha = 0.15f)
                            "In Progress" -> PrimaryBlue.copy(alpha = 0.15f)
                            "Delivered" -> Color(0xFF0D9488).copy(alpha = 0.15f)
                            else -> Color(0xFFF59E0B).copy(alpha = 0.15f)
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = repair.status,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (repair.status) {
                        "Completed" -> SecondaryTeal
                        "In Progress" -> PrimaryBlue
                        "Delivered" -> Color(0xFF14B8A6)
                        else -> Color(0xFFF59E0B)
                    }
                )
            }
        }
    }
}
