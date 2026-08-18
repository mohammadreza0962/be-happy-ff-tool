package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeviceProfile
import com.example.ui.components.DeviceProfileHeaderCard
import com.example.ui.components.GamingTopAppBar
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

@Composable
fun OptimizationScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDevicePicker: () -> Unit
) {
    val deviceProfile by viewModel.deviceProfile.collectAsState()
    val optState by viewModel.optimizationState.collectAsState()

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "Device Optimization",
                subtitle = "${deviceProfile.brand} ${deviceProfile.model}",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = NavyBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                DeviceProfileHeaderCard(
                    device = deviceProfile,
                    onChangeDeviceClick = onNavigateToDevicePicker
                )
            }

            // Progress Header Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            if (optState.completedCount == optState.totalItems) EmeraldPro else AmberFlame.copy(alpha = 0.5f),
                            RoundedCornerShape(16.dp)
                        )
                        .testTag("optimization_progress_card"),
                    colors = CardDefaults.cardColors(containerColor = NavySurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "PRE-MATCH OPTIMIZATION READINESS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = TextSecondary
                                )
                                Text(
                                    text = "${optState.completedCount} of ${optState.totalItems} Steps Completed",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (optState.completedCount == optState.totalItems) EmeraldPro else TextPrimary
                                )
                            }

                            Surface(
                                color = if (optState.completedCount == optState.totalItems) EmeraldPro.copy(alpha = 0.2f) else AmberFlame.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (optState.completedCount == optState.totalItems) EmeraldPro else AmberFlame
                                )
                            ) {
                                Text(
                                    text = "${(optState.progressPercent * 100).toInt()}%",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                    color = if (optState.completedCount == optState.totalItems) EmeraldPro else AmberFlameLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { optState.progressPercent },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .testTag("optimization_progress_bar"),
                            color = if (optState.completedCount == optState.totalItems) EmeraldPro else AmberFlame,
                            trackColor = NavyCardBorder
                        )

                        AnimatedVisibility(visible = optState.completedCount == optState.totalItems) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                Text(
                                    text = "🎉 Optimization checklist completed! Your device RAM, touch polling, and thermal headroom are fully primed.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = EmeraldPro
                                )
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "INTERACTIVE GAMING CHECKLIST",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = AmberFlameLight
                    )

                    TextButton(
                        onClick = { viewModel.resetOptimizationChecklist() },
                        modifier = Modifier.testTag("reset_checklist_button")
                    ) {
                        Text("Reset Checklist", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }

            // Checklist Items
            item {
                OptimizationCheckItem(
                    title = "Close Heavy Background Apps",
                    description = "Clear social media, browsers, and streaming apps from recents to liberate RAM for Free Fire's rendering engine.",
                    icon = Icons.Default.CloseFullscreen,
                    isChecked = optState.closeBackgroundApps,
                    onToggle = { viewModel.toggleOptimizationItem("closeBackgroundApps") },
                    testTag = "opt_item_close_apps"
                )
            }

            item {
                OptimizationCheckItem(
                    title = "Ensure Sufficient Free Storage (> 8GB)",
                    description = "Flash storage needs at least 15% free space to write shader caches without causing in-game micro-stutters.",
                    icon = Icons.Default.Storage,
                    isChecked = optState.freeStorageSpace,
                    onToggle = { viewModel.toggleOptimizationItem("freeStorageSpace") },
                    testTag = "opt_item_storage"
                )
            }

            item {
                OptimizationCheckItem(
                    title = "Restart Device Before Ranked Sessions",
                    description = "Flushes temporary OS heap allocations and resets background daemon memory leaks on Android.",
                    icon = Icons.Default.RestartAlt,
                    isChecked = optState.restartBeforeSession,
                    onToggle = { viewModel.toggleOptimizationItem("restartBeforeSession") },
                    testTag = "opt_item_restart"
                )
            }

            item {
                OptimizationCheckItem(
                    title = "Enable Device Game Turbo / Ultra Game Mode",
                    description = "Activates hardware touch sampling boost (up to ${deviceProfile.touchSamplingHz}Hz) on your ${deviceProfile.brand}.",
                    icon = Icons.Default.RocketLaunch,
                    isChecked = optState.gameTurboEnabled,
                    onToggle = { viewModel.toggleOptimizationItem("gameTurboEnabled") },
                    testTag = "opt_item_turbo"
                )
            }

            item {
                OptimizationCheckItem(
                    title = "Disable Battery Saver / Power Saving Mode",
                    description = "Battery saver limits CPU governor clock speeds to 50%, causing sudden FPS drops during squad clashes.",
                    icon = Icons.Default.BatteryAlert,
                    isChecked = optState.batterySaverOff,
                    onToggle = { viewModel.toggleOptimizationItem("batterySaverOff") },
                    testTag = "opt_item_battery"
                )
            }

            item {
                OptimizationCheckItem(
                    title = "Enable Do Not Disturb (DND) / Block Notifications",
                    description = "Prevents incoming notification overlays and calls from interrupting your active drag swipes.",
                    icon = Icons.Default.DoNotDisturbOn,
                    isChecked = optState.dndNotificationOn,
                    onToggle = { viewModel.toggleOptimizationItem("dndNotificationOn") },
                    testTag = "opt_item_dnd"
                )
            }

            item {
                OptimizationCheckItem(
                    title = "Stabilize Wi-Fi / 5G Network (Low Ping)",
                    description = "Disconnect other heavy downloaders on local Wi-Fi to keep in-game ping stable below 40ms.",
                    icon = Icons.Default.Wifi,
                    isChecked = optState.stableWifiOrData,
                    onToggle = { viewModel.toggleOptimizationItem("stableWifiOrData") },
                    testTag = "opt_item_wifi"
                )
            }

            item {
                OptimizationCheckItem(
                    title = "Clean Screen & Apply Gaming Finger Sleeves",
                    description = "Eliminates finger sweat friction on the display for silky-smooth vertical drag flicks.",
                    icon = Icons.Default.CleaningServices,
                    isChecked = optState.cleanScreenTouch,
                    onToggle = { viewModel.toggleOptimizationItem("cleanScreenTouch") },
                    testTag = "opt_item_screen"
                )
            }
        }
    }
}

@Composable
fun OptimizationCheckItem(
    title: String,
    description: String,
    icon: ImageVector,
    isChecked: Boolean,
    onToggle: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                if (isChecked) EmeraldPro.copy(alpha = 0.5f) else NavyCardBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onToggle)
            .testTag(testTag),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) NavySurfaceVariant else NavySurface
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isChecked) EmeraldPro.copy(alpha = 0.2f) else NavyCardBorder.copy(alpha = 0.5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isChecked) EmeraldPro else TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isChecked) EmeraldPro else TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }

            Checkbox(
                checked = isChecked,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = EmeraldPro,
                    uncheckedColor = NavyCardBorder,
                    checkmarkColor = NavyBackground
                ),
                modifier = Modifier.testTag("${testTag}_checkbox")
            )
        }
    }
}
