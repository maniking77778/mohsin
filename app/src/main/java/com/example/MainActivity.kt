package com.example

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.data.ActiveScreen
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.AppViewModel
import com.example.localization.AppLanguage
import com.example.localization.Loc
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DeviceInfoScreen
import com.example.ui.screens.DiagnosticsScreen
import com.example.ui.screens.ImeiScreen
import com.example.ui.screens.PtaScreen
import com.example.ui.screens.CustomerScreen
import com.example.ui.screens.PriceScreen
import com.example.ui.screens.QrScreen
import com.example.ui.screens.RepairScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.CardBlue
import com.example.ui.theme.MidnightBlack
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Build local Room Database safely
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "imei_repair_super_tool_db"
        ).fallbackToDestructiveMigration().build()

        // 2. Build local clean architecture repositories
        val repository = AppRepository(
            database.customerDao(),
            database.repairDao(),
            database.imeiHistoryDao()
        )

        // 3. Setup custom ViewModel Factory for clean constructor injection
        val viewModelFactory = AppViewModelFactory(application, repository)

        setContent {
            val appViewModel: AppViewModel = viewModel(factory = viewModelFactory)
            val isDarkTheme by appViewModel.isDarkMode.collectAsState()

            MyApplicationTheme(darkTheme = isDarkTheme) {
                MainAppScaffold(viewModel = appViewModel)
            }
        }
    }
}

// Custom factory matching standard Room requirements
class AppViewModelFactory(
    private val application: Application,
    private val repository: AppRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun MainAppScaffold(viewModel: AppViewModel) {
    val activeScreen by viewModel.activeScreen.collectAsState()
    val isDarkTheme by viewModel.isDarkMode.collectAsState()
    val lang by viewModel.appLanguage.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkTheme) MidnightBlack else MaterialTheme.colorScheme.background),
        bottomBar = {
            // High fidelity bottom nav bar with custom notch edge padding
            BottomNavBar(activeScreen = activeScreen, lang = lang) { screen ->
                viewModel.activeScreen.value = screen
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            // Type-safe Screen Routing switcher
            when (activeScreen) {
                ActiveScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                ActiveScreen.IMEI_SYSTEM -> ImeiScreen(viewModel = viewModel)
                ActiveScreen.PTA_SYSTEM -> PtaScreen(viewModel = viewModel)
                ActiveScreen.DEVICE_INFO -> DeviceInfoScreen(viewModel = viewModel)
                ActiveScreen.REPAIR_RECORD_LIST -> RepairScreen(viewModel = viewModel)
                ActiveScreen.CUSTOMER_LIST -> CustomerScreen(viewModel = viewModel)
                ActiveScreen.QR_BARCODE -> QrScreen(viewModel = viewModel)
                ActiveScreen.PRICE_MARKET -> PriceScreen(viewModel = viewModel)
                ActiveScreen.DIAGNOSTICS -> DiagnosticsScreen(viewModel = viewModel)
                ActiveScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                else -> DashboardScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BottomNavBar(
    activeScreen: ActiveScreen,
    lang: AppLanguage,
    onTabSelected: (ActiveScreen) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(CardBlue)
            .navigationBarsPadding()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Default.Home,
            label = Loc.tr("home", lang),
            isSelected = activeScreen == ActiveScreen.DASHBOARD,
            onClick = { onTabSelected(ActiveScreen.DASHBOARD) }
        )

        BottomNavItem(
            icon = Icons.Default.Build,
            label = Loc.tr("repairs", lang),
            isSelected = activeScreen == ActiveScreen.REPAIR_RECORD_LIST || activeScreen == ActiveScreen.ADD_REPAIR,
            onClick = { onTabSelected(ActiveScreen.REPAIR_RECORD_LIST) }
        )

        BottomNavItem(
            icon = Icons.Default.Group,
            label = Loc.tr("customers", lang),
            isSelected = activeScreen == ActiveScreen.CUSTOMER_LIST || activeScreen == ActiveScreen.ADD_CUSTOMER,
            onClick = { onTabSelected(ActiveScreen.CUSTOMER_LIST) }
        )

        BottomNavItem(
            icon = Icons.Default.Settings,
            label = Loc.tr("settings", lang),
            isSelected = activeScreen == ActiveScreen.SETTINGS,
            onClick = { onTabSelected(ActiveScreen.SETTINGS) }
        )
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 44.dp, height = 28.dp)
                .clip(CircleShape)
                .background(if (isSelected) SecondaryTeal.copy(alpha = 0.2f) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) SecondaryTeal else Color(0xFF94A3B8),
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            color = if (isSelected) SecondaryTeal else Color(0xFF94A3B8),
            fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium
        )
    }
}
