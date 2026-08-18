package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DeviceProfileHeaderCard
import com.example.ui.components.GamingTopAppBar
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

@Composable
fun DpiScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDevicePicker: () -> Unit
) {
    val deviceProfile by viewModel.deviceProfile.collectAsState()
    val dpiConfig by viewModel.dpiConfig.collectAsState()
    var selectedDpi by remember(dpiConfig.recommendedDpi) { mutableIntStateOf(dpiConfig.recommendedDpi) }

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "Android DPI Guidance",
                subtitle = "${deviceProfile.brand} ${deviceProfile.model}",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = NavyBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            DeviceProfileHeaderCard(
                device = deviceProfile,
                onChangeDeviceClick = onNavigateToDevicePicker
            )

            Spacer(modifier = Modifier.height(14.dp))

            // CRITICAL SAFE DISCLAIMER BANNER
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, DangerRed.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DangerRed.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Safe Policy Notice",
                        tint = DangerRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "SYSTEM SECURITY & POLICY NOTICE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = DangerRed
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "This app cannot and will not automatically change Android system DPI. Third-party apps lack root permissions to alter system display density. Use the step-by-step Developer Options guide below to adjust it safely on your phone.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = TextPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DPI Values & Slider Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, NavyCardBorder, RoundedCornerShape(14.dp)),
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
                                text = "RECOMMENDED STARTING DPI",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = TextSecondary
                            )
                            Text(
                                text = "Safe Range: ${dpiConfig.minSafeDpi} - ${dpiConfig.maxSafeDpi} dp",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = EmeraldPro
                            )
                        }

                        Surface(
                            color = CyanTech.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, CyanTech)
                        ) {
                            Text(
                                text = "$selectedDpi dp",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = CyanTech
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Slider(
                        value = selectedDpi.toFloat(),
                        onValueChange = {
                            selectedDpi = it.toInt()
                            viewModel.updateCustomDpi(selectedDpi)
                        },
                        valueRange = (dpiConfig.minSafeDpi - 20).toFloat()..(dpiConfig.maxSafeDpi + 40).toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = CyanTech,
                            activeTrackColor = CyanTech,
                            inactiveTrackColor = NavyCardBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dpi_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Default: ${dpiConfig.minSafeDpi} dp", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        Text("Max Safe: ${dpiConfig.maxSafeDpi} dp", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = dpiConfig.guidance,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.applyDpiGuidance(selectedDpi) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyanTech,
                            contentColor = NavyBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("apply_dpi_guide_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OPEN ANDROID DEVELOPER OPTIONS GUIDE",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step-by-Step Walkthrough Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, NavyCardBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "HOW TO SAFELY CHANGE DPI IN ANDROID",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = AmberFlameLight
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val steps = listOf(
                        "Open your phone's 'Settings' app.",
                        "Scroll to 'About Phone' or 'About Device'.",
                        "Locate 'Build Number' (or 'MIUI / HyperOS Version' on Xiaomi) and tap it 7 consecutive times until you see 'You are now a developer!'.",
                        "Go back to Settings ➔ System (or Additional Settings) ➔ 'Developer Options'.",
                        "Scroll down until you find 'Smallest Width' (or 'Minimum Width').",
                        "Write down your original default DPI (${dpiConfig.minSafeDpi}) so you can revert anytime.",
                        "Enter the recommended value: $selectedDpi dp.",
                        "Tap OK, restart Free Fire, and test your new camera drag smoothness."
                    )

                    steps.forEachIndexed { i, step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${i + 1}.",
                                fontWeight = FontWeight.Bold,
                                color = AmberFlame,
                                modifier = Modifier.width(24.dp)
                            )
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = TextPrimary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
