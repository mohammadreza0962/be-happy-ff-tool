package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navigation.NavRoutes
import com.example.ui.components.DeviceProfileHeaderCard
import com.example.ui.components.GamingTopAppBar
import com.example.ui.components.SensitivitySliderCard
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

@Composable
fun SensitivityScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDevicePicker: () -> Unit
) {
    val deviceProfile by viewModel.deviceProfile.collectAsState()
    val sensitivity by viewModel.sensitivity.collectAsState()
    var showSaveDialog by remember { mutableStateOf(false) }
    var favoriteCustomName by remember { mutableStateOf("") }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            containerColor = NavySurface,
            title = {
                Text(
                    text = "Save Sensitivity Preset",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Save this custom sensitivity profile to your local Favorites.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = favoriteCustomName,
                        onValueChange = { favoriteCustomName = it },
                        label = { Text("Preset Name", color = TextSecondary) },
                        placeholder = { Text("${deviceProfile.model} Pro Drag", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberFlame,
                            unfocusedBorderColor = NavyCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("save_favorite_name_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveSensitivityFavorite(favoriteCustomName)
                        showSaveDialog = false
                        favoriteCustomName = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmberFlame,
                        contentColor = TextPrimary
                    ),
                    modifier = Modifier.testTag("confirm_save_favorite_button")
                ) {
                    Text("Save Preset")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSaveDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "Sensitivity Preset",
                subtitle = "${deviceProfile.brand} ${deviceProfile.model} (${deviceProfile.ram})",
                showBackButton = true,
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = { viewModel.shareSensitivityPreset() },
                        modifier = Modifier.testTag("sensitivity_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = AmberFlameLight
                        )
                    }
                }
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
            // Header Device Card
            item {
                DeviceProfileHeaderCard(
                    device = deviceProfile,
                    onChangeDeviceClick = onNavigateToDevicePicker
                )
            }

            // Quick Actions Bar
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, NavyCardBorder, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = NavySurface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "PRESET ACTIONS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = AmberFlameLight
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.copyAllSensitivity() },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("copy_all_sensitivity_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberFlameLight),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AmberFlame.copy(alpha = 0.5f)),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy All", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = { showSaveDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("save_favorite_sensitivity_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldPro),
                                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPro.copy(alpha = 0.5f)),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp)
                            ) {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Save Fav", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.resetSensitivityToRecommendation() },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("reset_sensitivity_button"),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NavyCardBorder),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // Info note
            item {
                Surface(
                    color = NavySurfaceVariant,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = CyanTech,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Recommended starting point calibrated for ${deviceProfile.ram} RAM. Fine-tune sliders according to your drag speed.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextSecondary
                        )
                    }
                }
            }

            // 1. GENERAL SENSITIVITY
            item {
                SensitivitySliderCard(
                    title = "General",
                    value = sensitivity.general,
                    range = 0f..200f,
                    description = "Controls overall camera panning sensitivity and is the foundation for fast drag movements, 360 sweeps, and close-range shotgun headshots.",
                    onValueChange = { viewModel.updateGeneralSensitivity(it) },
                    onApplyClick = { viewModel.applyIndividualSensitivity("General", sensitivity.general) }
                )
            }

            // 2. RED DOT
            item {
                SensitivitySliderCard(
                    title = "Red Dot",
                    value = sensitivity.redDot,
                    range = 0f..200f,
                    description = "Fine-tunes non-scoped aiming and sight dot tracking. Critical for SMG (MP40, UMP) upward drag headshots.",
                    onValueChange = { viewModel.updateRedDotSensitivity(it) },
                    onApplyClick = { viewModel.applyIndividualSensitivity("Red Dot", sensitivity.redDot) }
                )
            }

            // 3. 2X SCOPE
            item {
                SensitivitySliderCard(
                    title = "2X Scope",
                    value = sensitivity.scope2x,
                    range = 0f..200f,
                    description = "Controls 2X magnification drag speed. Balances mid-range drag consistency on Assault Rifles (SCAR, M4A1, AK47).",
                    onValueChange = { viewModel.update2xScopeSensitivity(it) },
                    onApplyClick = { viewModel.applyIndividualSensitivity("2X Scope", sensitivity.scope2x) }
                )
            }

            // 4. 4X SCOPE
            item {
                SensitivitySliderCard(
                    title = "4X Scope",
                    value = sensitivity.scope4x,
                    range = 0f..200f,
                    description = "Calibrates long-range 4X magnification drag. Lower value prevents overshooting enemy heads at distance.",
                    onValueChange = { viewModel.update4xScopeSensitivity(it) },
                    onApplyClick = { viewModel.applyIndividualSensitivity("4X Scope", sensitivity.scope4x) }
                )
            }

            // 5. SNIPER SCOPE
            item {
                SensitivitySliderCard(
                    title = "Sniper Scope",
                    value = sensitivity.sniperScope,
                    range = 0f..200f,
                    description = "Dedicated tracking sensitivity for bolt-action rifles (AWM, M82B, Kar98k) for steady crosshair alignment and double-sniper switching.",
                    onValueChange = { viewModel.updateSniperSensitivity(it) },
                    onApplyClick = { viewModel.applyIndividualSensitivity("Sniper Scope", sensitivity.sniperScope) }
                )
            }

            // 6. FREE LOOK
            item {
                SensitivitySliderCard(
                    title = "Free Look",
                    value = sensitivity.freeLook,
                    range = 0f..200f,
                    description = "Independent 360-degree sprint eye camera sensitivity to scout enemy squad rotations while running without breaking direction.",
                    onValueChange = { viewModel.updateFreeLookSensitivity(it) },
                    onApplyClick = { viewModel.applyIndividualSensitivity("Free Look", sensitivity.freeLook) }
                )
            }
        }
    }
}
