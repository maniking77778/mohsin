package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActiveScreen
import com.example.data.AppViewModel
import com.example.localization.Loc
import com.example.ui.components.GlassyCard
import com.example.ui.components.GlassyTextField
import com.example.ui.components.PremiumGradientButton
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal
import java.text.DecimalFormat

@Composable
fun PtaScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.appLanguage.collectAsState()
    val ptaImeiInput by viewModel.ptaImeiInput.collectAsState()
    val passportDiscount by viewModel.ptaPassportDiscount.collectAsState()
    val taxAmount by viewModel.ptaTaxCalculatedAmount.collectAsState()
    val activeCategory by viewModel.ptaSelectedCategory.collectAsState()
    val resultText by viewModel.ptaTaxResultString.collectAsState()

    val categories = listOf(
        "Entry Smart (Under \$100)",
        "Mid-Range Smart (\$100-\$200)",
        "Sub-Flagship (\$200-\$350)",
        "Flagship (\$350-\$500)",
        "High-End Flagship (\$500+)"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back Header
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
                    text = Loc.tr("pta_tax_checker", lang),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Calculator Cards
        item {
            GlassyCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        text = "PTA Assessment Directory",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // IMEI verification input inside tax screen
                    GlassyTextField(
                        value = ptaImeiInput,
                        onValueChange = { input ->
                            if (input.length <= 15 && input.all { it.isDigit() }) {
                                viewModel.ptaImeiInput.value = input
                            }
                        },
                        label = "Device IMEI Verification (15 Digits)",
                        leadingIcon = Icons.Default.PhoneAndroid
                    )

                    // Device category selector lists
                    Text(
                        text = "Select Mobile Value range:",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = cat == activeCategory
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) PrimaryBlue.copy(alpha = 0.25f) else Color(0x0AFFFFFF))
                                    .clickable {
                                        viewModel.ptaSelectedCategory.value = cat
                                        viewModel.calculatePtaTax()
                                    }
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = cat, fontSize = 12.sp, color = if (isSelected) Color.White else Color(0xFF94A3B8))
                                    if (isSelected) {
                                        Icon(imageVector = Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Concession Switch
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x0AFFFFFF))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = Loc.tr("passport_registered", lang), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(text = "Applies a flat 20% tax concession rate", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                        Switch(
                            checked = passportDiscount,
                            onCheckedChange = {
                                viewModel.ptaPassportDiscount.value = it
                                viewModel.calculatePtaTax()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SecondaryTeal,
                                checkedTrackColor = SecondaryTeal.copy(alpha = 0.3f),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.DarkGray
                            )
                        )
                    }

                    // Calculation Display Output
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SecondaryTeal.copy(alpha = 0.12f))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Calculated Total Tax due", fontSize = 12.sp, color = Color(0xFF94A3B8))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "PKR " + DecimalFormat("##,###").format(taxAmount),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = SecondaryTeal
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "Status: Approved on Tax Clearance", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // PTA Official Guidelines table rules
        item {
            GlassyCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = Loc.tr("tax_table", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "In compliance with Pakistan Customs DIRBS policies, all imported GSM/eSIM devices must be registered within 60 days of arrival. Unregistered devices will experience complete network cellular disruption.",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8),
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = Color(0x1F94A3B8))
                    GuidelineRow(tier = "High Term (\$500+)", taxModel = "Est. PKR 85,000")
                    GuidelineRow(tier = "Premium (\$350-\$500)", taxModel = "Est. PKR 62,000")
                    GuidelineRow(tier = "Standard (\$200-\$350)", taxModel = "Est. PKR 40,000")
                    GuidelineRow(tier = "Lower (\$100-\$200)", taxModel = "Est. PKR 25,000")
                    GuidelineRow(tier = "Entry (Under \$100)", taxModel = "Est. PKR 11,500")
                }
            }
        }

        // Simulated API Gateways Status indicator
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0F172A))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Loc.tr("payment_api_ready", lang),
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }
    }
}

@Composable
fun GuidelineRow(tier: String, taxModel: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = tier, fontSize = 11.sp, color = Color.White)
        Text(text = taxModel, fontSize = 11.sp, color = SecondaryTeal, fontWeight = FontWeight.Bold)
    }
}
