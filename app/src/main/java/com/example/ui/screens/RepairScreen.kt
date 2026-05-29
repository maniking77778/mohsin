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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.example.data.ActiveScreen
import com.example.data.AppViewModel
import com.example.data.RepairRecord
import com.example.localization.AppLanguage
import com.example.localization.Loc
import com.example.ui.components.GlassyCard
import com.example.ui.components.GlassyTextField
import com.example.ui.components.PremiumGradientButton
import com.example.ui.components.SolidCard
import com.example.ui.theme.CardBlue
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RepairScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.appLanguage.collectAsState()
    val repairsList by viewModel.repairsList.collectAsState()
    val searchQuery by viewModel.repairSearchQuery.collectAsState()
    val currentSelectedRepair by viewModel.currentSelectedRepair.collectAsState()

    var showFormScreen by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        if (showFormScreen) {
            AddRepairFormScreen(
                viewModel = viewModel,
                lang = lang,
                onDismiss = { showFormScreen = false }
            )
        } else if (currentSelectedRepair != null) {
            InvoiceDetailScreen(
                repair = currentSelectedRepair!!,
                viewModel = viewModel,
                lang = lang,
                onDismiss = { viewModel.currentSelectedRepair.value = null }
            )
        } else {
            // Standard repair list dashboard layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with Plus button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Loc.tr("repair_invoice", lang),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    IconButton(
                        onClick = { showFormScreen = true },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrimaryBlue)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Repair", tint = Color.White)
                    }
                }

                // Search Box
                GlassyTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.repairSearchQuery.value = it },
                    label = Loc.tr("search_label", lang),
                    leadingIcon = Icons.Default.Search
                )

                // Repairs ledger items scroll
                if (repairsList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No repair tickets found on search",
                            color = Color(0xFF64748B),
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(repairsList) { repair ->
                            RecentRepairItem(repair = repair, lang = lang) {
                                viewModel.currentSelectedRepair.value = repair
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddRepairFormScreen(
    viewModel: AppViewModel,
    lang: AppLanguage,
    onDismiss: () -> Unit
) {
    val customersList by viewModel.customersList.collectAsState()

    var selectedCustName by remember { mutableStateOf("") }
    var selectedCustId by remember { mutableStateOf<Long>(-1L) }
    var selectedCustPhone by remember { mutableStateOf("") }

    var imei by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var detailDesc by remember { mutableStateOf("") }
    var laborCostInput by remember { mutableStateOf("") }
    var selectedFault by remember { mutableStateOf("Broken Display Screen") }

    val faultPricing = mapOf(
        "Broken Display Screen" to 35000.0,
        "Battery Fault / Degrading" to 12000.0,
        "Charging Port Defect" to 5500.0,
        "Network IC Receiver repair" to 18000.0,
        "Software OS Flashing" to 3500.0,
        "Water Damage PCB Clean" to 8000.0
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(com.example.ui.theme.MidnightBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Appbar Back
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Loc.tr("add_repair_job", lang),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // 1. Client Search / Selector
        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Assign to Registered Client:", fontSize = 13.sp, color = SecondaryTeal, fontWeight = FontWeight.Bold)

                    if (selectedCustId == -1L) {
                        Text(text = "Please register or select from listed clients below:", fontSize = 12.sp, color = Color.White)
                        Divider(color = Color(0x0FFFFFFF))

                        customersList.take(4).forEach { customer ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x0AFFFFFF))
                                    .clickable {
                                        selectedCustId = customer.id
                                        selectedCustName = customer.name
                                        selectedCustPhone = customer.phone
                                        imei = customer.imei
                                        model = customer.deviceModel
                                    }
                                    .padding(8.dp)
                            ) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(text = customer.name, color = Color.White, fontSize = 12.sp)
                                    Text(text = customer.phone, color = Color(0xFF64748B), fontSize = 12.sp)
                                }
                            }
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(text = "$selectedCustName Selected", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = selectedCustPhone, color = Color(0xFF94A3B8), fontSize = 12.sp)
                            }
                            IconButton(onClick = { selectedCustId = -1L }) {
                                Icon(imageVector = Icons.Default.Cancel, contentDescription = "Clear", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }

        // 2. Hardware specs
        item {
            SolidCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Device specs details:", fontSize = 13.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)

                    GlassyTextField(
                        value = model,
                        onValueChange = { model = it },
                        label = "Device Model Name",
                        leadingIcon = Icons.Default.Build
                    )

                    GlassyTextField(
                        value = imei,
                        onValueChange = { input ->
                            if (input.length <= 15 && input.all { it.isDigit() }) {
                                imei = input
                            }
                        },
                        label = "Device 15-Digit IMEI",
                        leadingIcon = Icons.Default.Feed,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        }

        // 3. Fault category
        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = Loc.tr("fault_type", lang), fontSize = 13.sp, color = SecondaryTeal, fontWeight = FontWeight.Bold)

                    faultPricing.keys.forEach { fault ->
                        val isSel = fault == selectedFault
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) SecondaryTeal.copy(alpha = 0.2f) else Color.Transparent)
                                .clickable {
                                    selectedFault = fault
                                    laborCostInput = faultPricing[fault]?.toInt().toString()
                                }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = fault, fontSize = 12.sp, color = if (isSel) Color.White else Color(0xFF94A3B8))
                            Text(text = "PKR " + DecimalFormat("##,###").format(faultPricing[fault]), fontSize = 11.sp, color = SecondaryTeal)
                        }
                    }
                }
            }
        }

        // 4. Manual Cost and Description Override
        item {
            SolidCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = Loc.tr("cost_calculator", lang), fontSize = 13.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)

                    GlassyTextField(
                        value = detailDesc,
                        onValueChange = { detailDesc = it },
                        label = Loc.tr("issue_desc", lang),
                        leadingIcon = Icons.Default.Feed
                    )

                    GlassyTextField(
                        value = laborCostInput,
                        onValueChange = { laborCostInput = it },
                        label = Loc.tr("repair_cost", lang) + " (PKR)",
                        leadingIcon = Icons.Default.MonetizationOn,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        }

        // Submit Button
        item {
            PremiumGradientButton(
                text = Loc.tr("generate_invoice", lang),
                enabled = selectedCustId != -1L && model.isNotBlank() && laborCostInput.isNotBlank(),
                onClick = {
                    viewModel.saveRepairJob(
                        custId = selectedCustId,
                        custName = selectedCustName,
                        custPhone = selectedCustPhone,
                        imei = imei,
                        model = model,
                        fault = selectedFault,
                        desc = detailDesc.ifBlank { "Standard handset service for hardware adjustments." },
                        cost = laborCostInput.toDoubleOrNull() ?: 5000.0,
                        status = "Pending"
                    )
                    onDismiss()
                }
            )
        }
    }
}

@Composable
fun InvoiceDetailScreen(
    repair: RepairRecord,
    viewModel: AppViewModel,
    lang: AppLanguage,
    onDismiss: () -> Unit
) {
    val dateStr = try {
        val sdf = SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.getDefault())
        sdf.format(Date(repair.createdTimestamp))
    } catch (e: Exception) {
        "Just now"
    }

    var showPdfToast by remember { mutableStateOf(false) }

    val formattedShareText = """
        *${Loc.tr("app_title", lang).uppercase()}*
        *INVOICE REC_NO: ${repair.invoiceId}*
        ----------------------------------
        Customer: ${repair.customerName}
        Contact: ${repair.customerPhone}
        Device ID/Model: ${repair.deviceModel}
        IMEI No: ${repair.deviceImei}
        Fault diagnosed: ${repair.faultType}
        Issue Description: ${repair.issueDescription}
        ----------------------------------
        *TOTAL CHARGE: PKR ${DecimalFormat("##,###").format(repair.repairCost)}*
        Status: ${repair.status}
        Thank you for choosing our professional diagnostics center!
    """.trimIndent()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(com.example.ui.theme.MidnightBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Appbar Back
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${Loc.tr("invoice_id", lang)}: ${repair.invoiceId}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Beautiful Paper-Receipt styled Card (High Contrast)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Receipt header logo
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = Loc.tr("app_title", lang),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "SMARTPHONE REPAIR RECIEPT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Divider(color = Color(0xFFF1F5F9), thickness = 2.dp)
                    }

                    RowReceiptInfo(label = "Date/Time", value = dateStr)
                    RowReceiptInfo(label = "Receipt Invoice ID", value = repair.invoiceId)
                    Divider(color = Color(0xFFF1F5F9))

                    RowReceiptInfo(label = "Client Name", value = repair.customerName)
                    RowReceiptInfo(label = "Contact Mobile", value = repair.customerPhone)
                    Divider(color = Color(0xFFF1F5F9))

                    RowReceiptInfo(label = "Item Inspected", value = repair.deviceModel)
                    RowReceiptInfo(label = "Inspected IMEI", value = repair.deviceImei.ifBlank { "N/A" })
                    RowReceiptInfo(label = "Inspected Fault", value = repair.faultType)
                    RowReceiptInfo(label = "Diagnostic Remarks", value = repair.issueDescription)
                    Divider(color = Color(0xFFF1F5F9), thickness = 2.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "GRAND TOTAL:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "PKR " + DecimalFormat("##,###").format(repair.repairCost),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2563EB)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "INSURANCE STATUS:",
                            fontSize = 10.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF2563EB).copy(alpha = 0.1f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = repair.status.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF2563EB)
                            )
                        }
                    }
                }
            }
        }

        // Action Status Toggles (Change status of ticket)
        item {
            SolidCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Change Ticket Status Level:", fontSize = 13.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusTabButton(text = "Pending", active = repair.status == "Pending", color = Color(0xFFF59E0B), modifier = Modifier.weight(1f)) {
                            viewModel.updateRepairStatus(repair, "Pending")
                        }
                        StatusTabButton(text = "Active", active = repair.status == "In Progress", color = PrimaryBlue, modifier = Modifier.weight(1f)) {
                            viewModel.updateRepairStatus(repair, "In Progress")
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatusTabButton(text = "Pass", active = repair.status == "Completed", color = SecondaryTeal, modifier = Modifier.weight(1f)) {
                            viewModel.updateRepairStatus(repair, "Completed")
                        }
                        StatusTabButton(text = "Paid & Out", active = repair.status == "Delivered", color = Color(0xFF0D9488), modifier = Modifier.weight(1f)) {
                            viewModel.updateRepairStatus(repair, "Delivered")
                        }
                    }
                }
            }
        }

        // Action buttons (WhatsApp, PDF Export, print)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // WhatsApp
                PremiumGradientButton(
                    text = Loc.tr("whatsapp_share", lang),
                    icon = Icons.Default.Share,
                    backgroundColor = Color(0xFF25D366),
                    accentColor = Color(0xFF128C7E),
                    onClick = {
                        viewModel.copyTextToClipboard(formattedShareText)
                        // Simulation text copied warning indicator is made ready to hook
                        showPdfToast = true
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showPdfToast = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CardBlue),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Feed, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "PDF Bill", color = Color.White, fontSize = 13.sp)
                        }
                    }

                    Button(
                        onClick = { showPdfToast = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CardBlue),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Print, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Print ESC", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }

                AnimatedVisibility(visible = showPdfToast) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SecondaryTeal.copy(alpha = 0.15f))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "WhatsApp message compiled. Receipt printed to spooler!",
                            color = SecondaryTeal,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Delete Button
                Button(
                    onClick = {
                        viewModel.deleteRepairRecord(repair)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x1FE11D48)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Delete ticket permanently", color = Color(0xFFE11D48), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusTabButton(
    text: String,
    active: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (active) color.copy(alpha = 0.25f) else Color(0x0AFFFFFF))
            .clickable(onClick = onClick)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (active) color else Color(0xFF94A3B8),
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RowReceiptInfo(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 11.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
    }
}
