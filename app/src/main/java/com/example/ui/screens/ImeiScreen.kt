package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActiveScreen
import com.example.data.AppViewModel
import com.example.data.ImeiHistory
import com.example.localization.Loc
import com.example.ui.components.GlassyCard
import com.example.ui.components.GlassyTextField
import com.example.ui.components.PremiumGradientButton
import com.example.ui.components.SolidCard
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ImeiScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.appLanguage.collectAsState()
    val imeiInput by viewModel.imeiInput.collectAsState()
    val validationResult by viewModel.imeiValidationResult.collectAsState()
    val history by viewModel.recentImeiChecks.collectAsState()
    val context = LocalContext.current

    var toastMessage by remember { mutableStateOf<String?>(value = null) }

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
                    text = Loc.tr("imei_system_title", lang),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Input Form
        item {
            GlassyCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = Loc.tr("imei_input_placeholder", lang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    GlassyTextField(
                        value = imeiInput,
                        onValueChange = { input ->
                            if (input.length <= 15 && input.all { it.isDigit() }) {
                                viewModel.imeiInput.value = input
                            }
                        },
                        label = "15-Digit numerical value",
                        leadingIcon = Icons.Default.Info,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    PremiumGradientButton(
                        text = Loc.tr("validate_imei", lang),
                        icon = Icons.Default.CheckCircle,
                        onClick = {
                            if (imeiInput.length == 15) {
                                viewModel.runImeiCheck()
                                toastMessage = null
                            } else {
                                toastMessage = Loc.tr("imei_format_error", lang)
                            }
                        }
                    )

                    if (toastMessage != null) {
                        Text(
                            text = toastMessage!!,
                            color = Color(0xFFE11D48),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Selected / Current Scan Details
        item {
            AnimatedVisibility(
                visible = validationResult != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                validationResult?.let { result ->
                    ResultDisplayCard(result = result, lang = lang, viewModel = viewModel)
                }
            }
        }

        // Audit Logs (History) Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Loc.tr("imei_history", lang),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (history.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.clearHistory() }
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = Loc.tr("clear_ledger", lang), fontSize = 12.sp, color = Color(0xFF94A3B8))
                    }
                }
            }
        }

        // History list
        if (history.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No previous audits on record",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(history) { record ->
                HistoryItemRow(record = record) {
                    viewModel.imeiValidationResult.value = record
                    viewModel.imeiInput.value = record.imei
                }
            }
        }
    }
}

@Composable
fun ResultDisplayCard(
    result: ImeiHistory,
    lang: com.example.localization.AppLanguage,
    viewModel: AppViewModel
) {
    val reportText = """
        IMEI REPORT AUDIT
        IMEI: ${result.imei}
        Status: ${result.status}
        Identity: ${result.brand} ${result.model}
        PTA Level: ${result.ptaStatus}
        Tax Est: PKR ${result.taxAmount}
        Spec Detail: ${result.deviceDetails}
    """.trimIndent()

    GlassyCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (result.isValid) Icons.Default.CheckCircle else Icons.Default.Error,
                        contentDescription = null,
                        tint = if (result.isValid) SecondaryTeal else Color(0xFFE11D48),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (result.isValid) Loc.tr("validation_success", lang) else Loc.tr("validation_fail", lang),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (result.isValid) SecondaryTeal else Color(0xFFE11D48)
                    )
                }

                Row {
                    IconButton(onClick = {
                        viewModel.copyTextToClipboard(reportText)
                    }) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White)
                    }
                    IconButton(onClick = {
                        // In a real device we can trigger intent, here we copy to clipboard and notify
                        viewModel.copyTextToClipboard(reportText)
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = SecondaryTeal)
                    }
                }
            }

            RowDetail(label = "IMEI ID", value = result.imei)
            RowDetail(label = Loc.tr("brand_model", lang), value = "${result.brand} ${result.model}")
            RowDetail(label = Loc.tr("blacklist_status", lang), value = result.status, highlightColor = if (result.isValid) SecondaryTeal else Color(0xFFE11D48))
            RowDetail(label = Loc.tr("pta_tax_est", lang), value = "PKR " + java.text.DecimalFormat("##,###").format(result.taxAmount))
            RowDetail(label = "Diagnostics", value = result.deviceDetails)
        }
    }
}

@Composable
fun HistoryItemRow(
    record: ImeiHistory,
    onClick: () -> Unit
) {
    val dateStr = try {
        val sdf = SimpleDateFormat("dd MMM hh:mm a", Locale.getDefault())
        sdf.format(Date(record.timestamp))
    } catch (e: Exception) {
        "Just now"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(com.example.ui.theme.CardBlue)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.3f)) {
                Text(text = record.imei, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "${record.brand} - ${record.model}", fontSize = 12.sp, color = Color(0xFF94A3B8))
            }
            Column(
                modifier = Modifier.weight(0.7f),
                horizontalAlignment = Alignment.End
            ) {
                Text(text = dateStr, fontSize = 11.sp, color = Color(0xFF64748B))
                Text(
                    text = if (record.isValid) "Pass" else "Fail",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (record.isValid) SecondaryTeal else Color(0xFFE11D48)
                )
            }
        }
    }
}

@Composable
fun RowDetail(label: String, value: String, highlightColor: Color? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = Color(0xFF94A3B8))
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = highlightColor ?: Color.White
        )
    }
}
