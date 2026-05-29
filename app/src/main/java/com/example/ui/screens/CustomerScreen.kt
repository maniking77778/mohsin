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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.data.ActiveScreen
import com.example.data.AppViewModel
import com.example.data.Customer
import com.example.localization.AppLanguage
import com.example.localization.Loc
import com.example.ui.components.GlassyCard
import com.example.ui.components.GlassyTextField
import com.example.ui.components.PremiumGradientButton
import com.example.ui.components.SolidCard
import com.example.ui.theme.CardBlue
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CustomerScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.appLanguage.collectAsState()
    val list by viewModel.customersList.collectAsState()
    val searchQuery by viewModel.customerSearchQuery.collectAsState()
    val selectedCustomer by viewModel.currentSelectedCustomer.collectAsState()

    var showAddForm by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        if (showAddForm) {
            AddCustomerForm(
                viewModel = viewModel,
                lang = lang,
                onDismiss = { showAddForm = false }
            )
        } else if (selectedCustomer != null) {
            CustomerDetailScreen(
                customer = selectedCustomer!!,
                viewModel = viewModel,
                lang = lang,
                onDismiss = { viewModel.currentSelectedCustomer.value = null }
            )
        } else {
            // Main directory layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Loc.tr("customer_database", lang),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    IconButton(
                        onClick = { showAddForm = true },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrimaryBlue)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Customer", tint = Color.White)
                    }
                }

                // Search Bar
                GlassyTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.customerSearchQuery.value = it },
                    label = Loc.tr("search_label", lang),
                    leadingIcon = Icons.Default.Search
                )

                // List
                if (list.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No clients matching query",
                            color = Color(0xFF64748B),
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(list) { customer ->
                            CustomerItemRow(customer = customer) {
                                viewModel.currentSelectedCustomer.value = customer
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddCustomerForm(
    viewModel: AppViewModel,
    lang: AppLanguage,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var imei by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(com.example.ui.theme.MidnightBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Appbar Navigation Back
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
                    text = Loc.tr("add_new_customer", lang),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Customer Details input Box
        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Primary Demographic Record:", fontSize = 13.sp, color = SecondaryTeal, fontWeight = FontWeight.Bold)

                    GlassyTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = Loc.tr("customer_name", lang),
                        leadingIcon = Icons.Default.Person
                    )

                    GlassyTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = Loc.tr("phone_number", lang),
                        leadingIcon = Icons.Default.Phone,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    GlassyTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = Loc.tr("email_address", lang),
                        leadingIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }
            }
        }

        // Hardware Device properties Box
        item {
            SolidCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Primary Device details:", fontSize = 13.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)

                    GlassyTextField(
                        value = model,
                        onValueChange = { model = it },
                        label = "Handset Model",
                        leadingIcon = Icons.Default.PhoneAndroid
                    )

                    GlassyTextField(
                        value = imei,
                        onValueChange = { input ->
                            if (input.length <= 15 && input.all { it.isDigit() }) {
                                imei = input
                            }
                        },
                        label = "Handset IMEI",
                        leadingIcon = Icons.Default.Info,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        }

        // Submission
        item {
            PremiumGradientButton(
                text = Loc.tr("save_record", lang),
                enabled = name.isNotBlank() && phone.isNotBlank(),
                onClick = {
                    viewModel.createCustomer(name, phone, email, imei, model)
                    onDismiss()
                }
            )
        }
    }
}

@Composable
fun CustomerDetailScreen(
    customer: Customer,
    viewModel: AppViewModel,
    lang: AppLanguage,
    onDismiss: () -> Unit
) {
    val dateStr = try {
        val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        sdf.format(Date(customer.timestamp))
    } catch (e: Exception) {
        "Just now"
    }

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
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Close", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = customer.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Profile details Card
        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Client Profiling Matrix", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Divider(color = Color(0x1F94A3B8))

                    RowProfileDetail(field = Loc.tr("phone_number", lang), value = customer.phone)
                    RowProfileDetail(field = Loc.tr("email_address", lang), value = customer.email)
                    RowProfileDetail(field = "Device Link", value = customer.deviceModel.ifBlank { "No device bound" })
                    RowProfileDetail(field = "IMEI Bound", value = customer.imei.ifBlank { "Not bound" })
                    RowProfileDetail(field = "Visits Count", value = "${customer.visitCount} visits")
                    RowProfileDetail(field = "Registered Date", value = dateStr)
                }
            }
        }

        // Delete profile Button
        item {
            Button(
                onClick = {
                    viewModel.deleteCustomer(customer)
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
                    Text(text = "Delete record & wipe profile", color = Color(0xFFE11D48), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun CustomerItemRow(
    customer: Customer,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBlue)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlue.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Group, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = customer.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = customer.phone, fontSize = 12.sp, color = Color(0xFF94A3B8))
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x0FFFFFFF))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "${customer.visitCount} Jobs", fontSize = 10.sp, color = SecondaryTeal, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RowProfileDetail(field: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = field, fontSize = 12.sp, color = Color(0xFF64748B))
        Text(text = value, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}
