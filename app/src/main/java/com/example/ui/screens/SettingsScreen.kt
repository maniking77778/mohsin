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
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActiveScreen
import com.example.data.AppViewModel
import com.example.localization.AppLanguage
import com.example.localization.Loc
import com.example.ui.components.GlassyCard
import com.example.ui.components.GlassyTextField
import com.example.ui.components.PremiumGradientButton
import com.example.ui.components.SolidCard
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal
import java.text.DecimalFormat

@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.appLanguage.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val isLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
    val loginErr by viewModel.loginError.collectAsState()

    val totalEarnings by viewModel.dashboardTotalEarnings.collectAsState()
    val activeJobs by viewModel.dashboardActiveJobsCount.collectAsState()
    val totalClients by viewModel.dashboardTotalClientsCount.collectAsState()

    val context = LocalContext.current

    var passcodeWord by remember { mutableStateOf("") }
    var actionNotification by remember { mutableStateOf<String?>(value = null) }
    var displayResetWarn by remember { mutableStateOf(false) }

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
                    text = Loc.tr("settings_title", lang),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // 1. Language Toggle
        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = Loc.tr("language_choice", lang), fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Row {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (lang == AppLanguage.ENGLISH) SecondaryTeal else Color(0x1F94A3B8))
                                .clickable { viewModel.appLanguage.value = AppLanguage.ENGLISH }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "ENG", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (lang == AppLanguage.URDU) SecondaryTeal else Color(0x1F94A3B8))
                                .clickable { viewModel.appLanguage.value = AppLanguage.URDU }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "اردو", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 2. High Contrast Theme Dark/Light
        item {
            SolidCard(modifier = Modifier.fillMaxWidth()) {
                 RowanDark(isDark = isDark, lang = lang) {
                     viewModel.isDarkMode.value = it
                 }
            }
        }

        // 3. Security Authenticator (PASSCODE)
        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = Loc.tr("admin_login", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text(text = Loc.tr("admin_desc", lang), fontSize = 11.sp, color = Color(0xFF64748B))

                    if (!isLoggedIn) {
                        GlassyTextField(
                            value = passcodeWord,
                            onValueChange = { passcodeWord = it },
                            label = Loc.tr("password", lang),
                            leadingIcon = Icons.Default.LockOpen,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        if (loginErr != null) {
                            Text(text = Loc.tr(loginErr!!, lang), color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        PremiumGradientButton(
                            text = Loc.tr("login", lang),
                            onClick = {
                                viewModel.attemptLogin(passcodeWord)
                                passcodeWord = ""
                            }
                        )

                        // Demo Quick login helper bypass
                        Text(
                            text = Loc.tr("admin_bypass", lang),
                            fontSize = 11.sp,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { viewModel.attemptLogin("admin123") }
                                .padding(vertical = 4.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SecondaryTeal.copy(alpha = 0.12f))
                                .padding(10.dp)
                        ) {
                            Text(text = Loc.tr("logged_in_as", lang), color = SecondaryTeal, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        PremiumGradientButton(
                            text = Loc.tr("logout", lang),
                            onClick = { viewModel.logout() },
                            backgroundColor = Color(0xFFE11D48),
                            accentColor = Color(0xFF991B1B)
                        )
                    }
                }
            }
        }

        // 4. Operational Accounting Reports System
        item {
            SolidCard(modifier = Modifier.fillMaxWidth()) {
                val parsedEarnings = DecimalFormat("##,###").format(totalEarnings)
                val reportStatement = """
                    ${Loc.tr("app_title", lang).uppercase()}
                    BUSINESS ACCOUNTING SUMMARY STATEMENT
                    ------------------------------------
                    Total Gross Earnings: PKR $parsedEarnings
                    Active Tickets in Pipeline: $activeJobs Repairs
                    Registered Customer Ledger Size: $totalClients Profiles
                    ------------------------------------
                    Operational clearance status: Excellent. Generated locally.
                """.trimIndent()

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = SecondaryTeal, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = Loc.tr("operations_report", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    RowReceiptSettings(label = Loc.tr("earnings", lang), value = "PKR $parsedEarnings")
                    RowReceiptSettings(label = Loc.tr("active_jobs", lang), value = "$activeJobs Repairs")
                    RowReceiptSettings(label = "Clients Pool", value = "$totalClients Users")

                    Divider(color = Color(0x1F94A3B8))

                    PremiumGradientButton(
                        text = Loc.tr("share_report", lang),
                        icon = Icons.Default.Share,
                        onClick = {
                            viewModel.copyTextToClipboard(reportStatement)
                            actionNotification = "Compiled Report written to Clipboard for share!"
                        }
                    )
                }
            }
        }

        // 5. Database Secure Backup Administration
        item {
            GlassyCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = Loc.tr("backup_restore", lang), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    // Backup Card buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val out = viewModel.backupDatabaseToJson(context)
                                actionNotification = Loc.tr(out, lang)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x1F3B82F6)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = PrimaryBlue)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Backup", color = PrimaryBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                val out = viewModel.restoreDatabaseFromJson(context)
                                actionNotification = Loc.tr(out, lang)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0x1F14B8A6)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.CloudDownload, contentDescription = null, tint = SecondaryTeal)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Restore", color = SecondaryTeal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Reset App Card Button
                    Button(
                        onClick = { displayResetWarn = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x1FE11D48)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, tint = Color(0xFFE11D48))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = Loc.tr("reset_btn", lang), color = Color(0xFFE11D48), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Notifications Box
                    AnimatedVisibility(visible = actionNotification != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SecondaryTeal.copy(alpha = 0.15f))
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = actionNotification!!, color = SecondaryTeal, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Reset warnings modal overlay
                    AnimatedVisibility(visible = displayResetWarn) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF450A0A))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(text = Loc.tr("reset_warning", lang), color = Color.White, fontSize = 11.sp)
                                Text(text = Loc.tr("action_not_undone", lang), color = Color.Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = Loc.tr("no", lang),
                                        color = Color.White,
                                        modifier = Modifier
                                            .clickable { displayResetWarn = false }
                                            .padding(10.dp),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = Loc.tr("yes", lang),
                                        color = Color.Red,
                                        modifier = Modifier
                                            .clickable {
                                                viewModel.performFactoryReset()
                                                displayResetWarn = false
                                                actionNotification = "Factory Wiped successfully."
                                            }
                                            .padding(10.dp),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RowanDark(
    isDark: Boolean,
    lang: AppLanguage,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.WbSunny, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = Loc.tr("theme_choice", lang), fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Switch(
            checked = isDark,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SecondaryTeal,
                checkedTrackColor = SecondaryTeal.copy(alpha = 0.3f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}

@Composable
fun RowReceiptSettings(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF94A3B8))
        Text(text = value, fontSize = 13.sp, color = SecondaryTeal, fontWeight = FontWeight.Bold)
    }
}
