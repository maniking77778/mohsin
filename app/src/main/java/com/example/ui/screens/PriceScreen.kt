package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import java.text.DecimalFormat

@Composable
fun PriceScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.appLanguage.collectAsState()

    val modelName by viewModel.priceMarketModel.collectAsState()
    val conditionGrade by viewModel.priceMarketCondition.collectAsState()
    val includeBox by viewModel.priceMarketIncludeBox.collectAsState()
    val ptaApproved by viewModel.priceMarketIsPtaApproved.collectAsState()
    val basePrice by viewModel.priceMarketBaseNewPrice.collectAsState()
    val estimatedOutput by viewModel.priceMarketCalculatedEst.collectAsState()

    val grades = listOf("Grade A (Like New)", "Grade B (Minor Scratches)", "Grade C (Heavy Wear)")

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
                    text = Loc.tr("market_index", lang),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Valuation Input Form Layout
        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Calculate, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = Loc.tr("used_calculator", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    GlassyTextField(
                        value = modelName,
                        onValueChange = { viewModel.priceMarketModel.value = it },
                        label = "Smartphone Model Name (e.g. S22 Plus)",
                        leadingIcon = Icons.Default.Info
                    )

                    GlassyTextField(
                        value = basePrice,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                viewModel.priceMarketBaseNewPrice.value = input
                            }
                        },
                        label = Loc.tr("base_market_price", lang) + " (PKR)",
                        leadingIcon = Icons.Default.Label,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Divider(color = Color(0x1F94A3B8))

                    // Integrity selectors
                    Text(text = Loc.tr("condition_grade", lang) + ":", fontSize = 12.sp, color = Color(0xFF94A3B8))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        grades.forEach { grade ->
                            val isSelected = grade == conditionGrade
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) PrimaryBlue.copy(alpha = 0.25f) else Color(0x0AFFFFFF))
                                    .clickable {
                                        viewModel.priceMarketCondition.value = grade
                                        viewModel.runHandsetPriceEvaluation()
                                    }
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = grade, fontSize = 12.sp, color = if (isSelected) Color.White else Color(0xFF94A3B8))
                                    if (isSelected) {
                                        Icon(imageVector = Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    Divider(color = Color(0x1F94A3B8))

                    // Accessories options toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Original Box + Accessories", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "Unopened accessories increase values by 15%", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                        Switch(
                            checked = includeBox,
                            onCheckedChange = {
                                viewModel.priceMarketIncludeBox.value = it
                                viewModel.runHandsetPriceEvaluation()
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = SecondaryTeal, checkedTrackColor = SecondaryTeal.copy(alpha = 0.3f))
                        )
                    }

                    // PTA approval toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Registered is PTA Approved", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "Sim status validation adds 30% appraisal values", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                        Switch(
                            checked = ptaApproved,
                            onCheckedChange = {
                                viewModel.priceMarketIsPtaApproved.value = it
                                viewModel.runHandsetPriceEvaluation()
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = SecondaryTeal, checkedTrackColor = SecondaryTeal.copy(alpha = 0.3f))
                        )
                    }

                    // Calculation execution
                    PremiumGradientButton(
                        text = "Calculate Appraisal Valuation",
                        icon = Icons.Default.Calculate,
                        onClick = { viewModel.runHandsetPriceEvaluation() }
                    )

                    // Output Box
                    AnimatedVisibility(visible = estimatedOutput > 0.0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SecondaryTeal.copy(alpha = 0.12f))
                                .padding(14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = Loc.tr("estimated_value", lang), fontSize = 12.sp, color = Color(0xFF94A3B8))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "PKR " + DecimalFormat("##,###").format(estimatedOutput),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SecondaryTeal
                                )
                                Text(text = "Appraisal based on local dukan trends 2026", fontSize = 10.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Market Rate Index Listing Tracker
        item {
            SolidCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = Loc.tr("market_trends", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Divider(color = Color(0x1F94A3B8))

                    TrendRow(brand = "iPhone 15 Pro Max", rate = "PKR 415,000", delta = "▲ 2.5%")
                    TrendRow(brand = "Samsung Galaxy S24 Ultra", rate = "PKR 355,000", delta = "▼ 1.2%")
                    TrendRow(brand = "Redmi Note 13 Pro", rate = "PKR 74,500", delta = "▲ 4.1%")
                    TrendRow(brand = "Infinix Note 40", rate = "PKR 54,000", delta = "Stable")
                    TrendRow(brand = "Techno Spark 20", rate = "PKR 38,500", delta = "▲ 0.8%")
                }
            }
        }
    }
}

@Composable
fun TrendRow(brand: String, rate: String, delta: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = brand, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = "Dukan Average", fontSize = 10.sp, color = Color(0xFF64748B))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = rate, fontSize = 12.sp, color = SecondaryTeal, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = delta,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (delta.contains("▲")) Color(0xFF10B981) else if (delta.contains("▼")) Color(0xFFEF4444) else Color(0xFF64748B)
            )
        }
    }
}
